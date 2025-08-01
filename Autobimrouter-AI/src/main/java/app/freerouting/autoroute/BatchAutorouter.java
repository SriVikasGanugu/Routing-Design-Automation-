package app.freerouting.autoroute;

import app.freerouting.board.*;
import app.freerouting.datastructures.TimeLimit;
import app.freerouting.datastructures.UndoableObjects;
import app.freerouting.geometry.planar.FloatLine;
import app.freerouting.geometry.planar.FloatPoint;
import app.freerouting.interactive.BoardHandling;
import app.freerouting.interactive.InteractiveActionThread;
import app.freerouting.interactive.InteractiveState;
import app.freerouting.logger.FRLogger;
import app.freerouting.management.TextManager;
import app.freerouting.rules.Net;

import java.util.*;

/**
 * Handles the sequencing of the auto-router passes.
 */
public class BatchAutorouter
{
  private static final int TIME_LIMIT_TO_PREVENT_ENDLESS_LOOP = 1000;
  private final InteractiveActionThread thread;
  private final BoardHandling hdlg;
  private final RoutingBoard routing_board;
  private final boolean remove_unconnected_vias;
  private final AutorouteControl.ExpansionCostFactor[] trace_cost_arr;
  private final boolean retain_autoroute_database;
  private final int start_ripup_costs;
  private final HashSet<String> already_checked_board_hashes = new HashSet<>();
  private final LinkedList<Integer> traceLengthDifferenceBetweenPasses = new LinkedList<>();
  private boolean is_interrupted = false;
  /**
   * Used to draw the airline of the current routed incomplete.
   */
  private FloatLine air_line;

  /**
   * Creates a new batch autorouter.
   */
  public BatchAutorouter(InteractiveActionThread p_thread, boolean p_remove_unconnected_vias, boolean p_with_preferred_directions, int p_start_ripup_costs)
  {
    this(p_thread, p_remove_unconnected_vias, p_with_preferred_directions, p_start_ripup_costs, null);
  }

  public BatchAutorouter(InteractiveActionThread p_thread, boolean p_remove_unconnected_vias, boolean p_with_preferred_directions, int p_start_ripup_costs, RoutingBoard updated_routing_board)
  {
    this.thread = p_thread;
    this.hdlg = p_thread.hdlg;
    this.routing_board = updated_routing_board != null ? updated_routing_board : this.hdlg.get_routing_board();
    this.remove_unconnected_vias = p_remove_unconnected_vias;
    if (p_with_preferred_directions)
    {
      this.trace_cost_arr = this.hdlg.get_settings().autoroute_settings.get_trace_cost_arr();
    }
    else
    {
      // remove preferred direction
      this.trace_cost_arr = new AutorouteControl.ExpansionCostFactor[this.routing_board.get_layer_count()];
      for (int i = 0; i < this.trace_cost_arr.length; ++i)
      {
        double curr_min_cost = this.hdlg.get_settings().autoroute_settings.get_preferred_direction_trace_costs(i);
        this.trace_cost_arr[i] = new AutorouteControl.ExpansionCostFactor(curr_min_cost, curr_min_cost);
      }
    }

    this.start_ripup_costs = p_start_ripup_costs;
    this.retain_autoroute_database = false;
  }

  /**
   * Autoroutes ripup passes until the board is completed or the autorouter is stopped by the user,
   * or if p_max_pass_count is exceeded. Is currently used in the optimize via batch pass. Returns
   * the number of passes to complete the board or p_max_pass_count + 1, if the board is not
   * completed.
   */
  public static int autoroute_passes_for_optimizing_item(InteractiveActionThread p_thread, int p_max_pass_count, int p_ripup_costs, boolean p_with_preferred_directions, RoutingBoard updated_routing_board)
  {
    BatchAutorouter router_instance = new BatchAutorouter(p_thread, true, p_with_preferred_directions, p_ripup_costs, updated_routing_board);
    boolean still_unrouted_items = true;
    int curr_pass_no = 1;
    while (still_unrouted_items && !router_instance.is_interrupted && curr_pass_no <= p_max_pass_count)
    {
      if (p_thread.is_stop_auto_router_requested())
      {
        router_instance.is_interrupted = true;
      }
      still_unrouted_items = router_instance.autoroute_pass(curr_pass_no, false);
      if (still_unrouted_items && !router_instance.is_interrupted && updated_routing_board == null)
      {
        p_thread.hdlg.get_settings().autoroute_settings.increment_pass_no();
      }
      ++curr_pass_no;
    }
    router_instance.remove_tails(Item.StopConnectionOption.NONE);
    if (!still_unrouted_items)
    {
      --curr_pass_no;
    }
    return curr_pass_no;
  }

  /**
   * Autoroutes ripup passes until the board is completed or the autorouter is stopped by the user.
   * Returns true if the board is completed.
   */
  public boolean autoroute_passes(boolean save_intermediate_stages)
  {
    TextManager tm = new TextManager(InteractiveState.class, hdlg.get_locale());

    boolean still_unrouted_items = true;
    int minimumPassCountBeforeImprovementCheck = 5;
    int numberOfPassesToAverage = minimumPassCountBeforeImprovementCheck;

    while (still_unrouted_items && !this.is_interrupted)
    {
      if (thread.is_stop_auto_router_requested())
      {
        this.is_interrupted = true;
      }

      String current_board_hash = this.routing_board.get_hash();
      if (already_checked_board_hashes.contains(current_board_hash))
      {
        // This board was already evaluated, so we stop auto-router to avoid the endless loop
        thread.request_stop_auto_router();
        break;
      }

      int curr_pass_no = hdlg.get_settings().autoroute_settings.get_start_pass_no();
      if (curr_pass_no > hdlg.get_settings().autoroute_settings.get_stop_pass_no())
      {
        thread.request_stop_auto_router();
        break;
      }

      String start_message = tm.getText("autorouter_started", Integer.toString(curr_pass_no));
      // hdlg.screen_messages.set_status_message(start_message);

      BasicBoard boardBefore = this.routing_board.clone();

      FRLogger.traceEntry("BatchAutorouter.autoroute_pass #" + curr_pass_no + " on board '" + current_board_hash + "' making {} changes");
      already_checked_board_hashes.add(this.routing_board.get_hash());
      still_unrouted_items = autoroute_pass(curr_pass_no, true);

      // let's check if there was enough track length change in the last few passes, because if it was too little we should stop
      // TODO: score the board based on the costs settings of trace length, corner and via count, unconnected ratsnets, etc.
      int traceLengthDifferences = this.routing_board.diff_traces(boardBefore);
      traceLengthDifferenceBetweenPasses.add(traceLengthDifferences);

      if (traceLengthDifferenceBetweenPasses.size() > numberOfPassesToAverage)
      {
        traceLengthDifferenceBetweenPasses.removeFirst();

        OptionalDouble averageTraceLengthDifferencePerPass = traceLengthDifferenceBetweenPasses.stream().mapToDouble(a -> a).average();

        // TODO: make the threshold based on the initial score (cost)
        if (averageTraceLengthDifferencePerPass.getAsDouble() < 20.0)
        {
          FRLogger.warn("There were only " + FRLogger.defaultFloatFormat.format(averageTraceLengthDifferencePerPass.getAsDouble()) + " track length increase in the last " + numberOfPassesToAverage + " passes, so it's very likely that autorouter can't improve the result further.");
          this.is_interrupted = true;
        }
        else
        {
          numberOfPassesToAverage = minimumPassCountBeforeImprovementCheck;
        }
      }
      double autorouter_pass_duration = FRLogger.traceExit("BatchAutorouter.autoroute_pass #" + curr_pass_no + " on board '" + current_board_hash + "' making {} changes", traceLengthDifferences);
      FRLogger.info("Auto-router pass #" + curr_pass_no + " on board '" + current_board_hash + "' was completed in " + FRLogger.formatDuration(autorouter_pass_duration));

      if (save_intermediate_stages)
      {
        this.thread.hdlg.get_panel().board_frame.save_intermediate_stage_file();
      }

      // check if there are still unrouted items
      if (still_unrouted_items && !is_interrupted)
      {
        hdlg.get_settings().autoroute_settings.increment_pass_no();
      }
    }
    if (!(this.remove_unconnected_vias || still_unrouted_items || this.is_interrupted))
    {
      // clean up the route if the board is completed and if fanout is used.
      remove_tails(Item.StopConnectionOption.NONE);
    }

    already_checked_board_hashes.clear();

    return !this.is_interrupted;
  }

  /**
   * Auto-routes one ripup pass of all items of the board. Returns false, if the board is already
   * completely routed.
   */
  private boolean autoroute_pass(int p_pass_no, boolean p_with_screen_message)
  {
    try
    {
      Collection<Item> autoroute_item_list = new LinkedList<>();
      Set<Item> handled_items = new TreeSet<>();
      Iterator<UndoableObjects.UndoableObjectNode> it = routing_board.item_list.start_read_object();
      for (; ; )
      {
        UndoableObjects.Storable curr_ob = routing_board.item_list.read_object(it);
        if (curr_ob == null)
        {
          break;
        }
        if (curr_ob instanceof Connectable && curr_ob instanceof Item curr_item)
        {
          // This is a connectable item, like PolylineTrace or Pin
          if (!curr_item.is_routable())
          {
            if (!handled_items.contains(curr_item))
            {

              // Let's go through all nets of this item
              for (int i = 0; i < curr_item.net_count(); ++i)
              {
                int curr_net_no = curr_item.get_net_no(i);
                Set<Item> connected_set = curr_item.get_connected_set(curr_net_no);
                for (Item curr_connected_item : connected_set)
                {
                  if (curr_connected_item.net_count() <= 1)
                  {
                    handled_items.add(curr_connected_item);
                  }
                }
                int net_item_count = routing_board.connectable_item_count(curr_net_no);

                // If the item is not connected to all other items of the net, we add it to the auto-router's to-do list
                if ((connected_set.size() < net_item_count) && (!curr_item.has_ignored_nets()))
                {
                  autoroute_item_list.add(curr_item);
                }
              }
            }
          }
        }
      }

      // If there are no items to route, we're done
      if (autoroute_item_list.isEmpty())
      {
        this.air_line = null;
        return false;
      }

      int items_to_go_count = autoroute_item_list.size();
      int ripped_item_count = 0;
      int not_found = 0;
      int routed = 0;

      // Update the GUI with the current status
      if (p_with_screen_message)
      {
        // hdlg.screen_messages.set_batch_autoroute_info(items_to_go_count, routed, ripped_item_count, not_found);
      }

      // Let's go through all items to route
      for (Item curr_item : autoroute_item_list)
      {
        // If the user requested to stop the auto-router, we stop it
        if (this.is_interrupted)
        {
          break;
        }

        // Let's go through all nets of this item
        for (int i = 0; i < curr_item.net_count(); ++i)
        {
          // If the user requested to stop the auto-router, we stop it
          if (this.thread.is_stop_auto_router_requested())
          {
            this.is_interrupted = true;
            break;
          }

          // We visually mark the area of the board, which is changed by the auto-router
          routing_board.start_marking_changed_area();

          // Do the auto-routing step for this item (typically PolylineTrace or Pin)
          SortedSet<Item> ripped_item_list = new TreeSet<>();
          if (autoroute_item(curr_item, curr_item.get_net_no(i), ripped_item_list, p_pass_no))
          {
            ++routed;
//            hdlg.repaint();
          }
          else
          {
            ++not_found;
          }
          --items_to_go_count;
          ripped_item_count += ripped_item_list.size();

          // Update the GUI with the current status
          if (p_with_screen_message)
          {
            // hdlg.screen_messages.set_batch_autoroute_info(items_to_go_count, routed, ripped_item_count, not_found);
          }
        }
      }

      if (this.remove_unconnected_vias)
      {
        remove_tails(Item.StopConnectionOption.NONE);
      }
      else
      {
        remove_tails(Item.StopConnectionOption.FANOUT_VIA);
      }

      // We are done with this pass
      this.air_line = null;
      return true;
    } catch (Exception e)
    {
      System.err.println("Exception during auto-routing: " + e.getMessage());

      // Print the full stack trace for debugging
      e.printStackTrace();
      // Something went wrong during the auto-routing
      this.air_line = null;
      return false;
    }
  }

  private void remove_tails(Item.StopConnectionOption p_stop_connection_option)
  {
    routing_board.start_marking_changed_area();
    routing_board.remove_trace_tails(-1, p_stop_connection_option);
    routing_board.opt_changed_area(new int[0], null, this.hdlg.get_settings().get_trace_pull_tight_accuracy(), this.trace_cost_arr, this.thread, TIME_LIMIT_TO_PREVENT_ENDLESS_LOOP);
  }

  // Tries to route an item on a specific net. Returns true, if the item is routed.
  private boolean autoroute_item(Item p_item, int p_route_net_no, SortedSet<Item> p_ripped_item_list, int p_ripup_pass_no)
  {
    try
    {
      boolean contains_plane = false;

      // Get the net
      Net route_net = routing_board.rules.nets.get(p_route_net_no);
      if (route_net != null)
      {
        contains_plane = route_net.contains_plane();
      }

      // Get the current via costs based on auto-router settings
      int curr_via_costs;
      if (contains_plane)
      {
        curr_via_costs = hdlg.get_settings().autoroute_settings.get_plane_via_costs();
      }
      else
      {
        curr_via_costs = hdlg.get_settings().autoroute_settings.get_via_costs();
      }

      // Get and calculate the auto-router settings based on the board and net we are working on
      AutorouteControl autoroute_control = new AutorouteControl(this.routing_board, p_route_net_no, hdlg.get_settings(), curr_via_costs, this.trace_cost_arr);
      autoroute_control.ripup_allowed = true;
      autoroute_control.ripup_costs = this.start_ripup_costs * p_ripup_pass_no;
      autoroute_control.remove_unconnected_vias = this.remove_unconnected_vias;

      // Check if the item is already routed
      Set<Item> unconnected_set = p_item.get_unconnected_set(p_route_net_no);
      if (unconnected_set.isEmpty())
      {
        return true; // p_item is already routed.
      }

      Set<Item> connected_set = p_item.get_connected_set(p_route_net_no);
      Set<Item> route_start_set;
      Set<Item> route_dest_set;
      if (contains_plane)
      {
        for (Item curr_item : connected_set)
        {
          if (curr_item instanceof ConductionArea)
          {
            return true; // already connected to plane
          }
        }
      }
      if (contains_plane)
      {
        route_start_set = connected_set;
        route_dest_set = unconnected_set;
      }
      else
      {
        route_start_set = unconnected_set;
        route_dest_set = connected_set;
      }

      // Calculate the shortest distance between the two sets of items
      calc_airline(route_start_set, route_dest_set);

      // Calculate the maximum time for this autoroute pass
      double max_milliseconds = 100000 * Math.pow(2, p_ripup_pass_no - 1);
      max_milliseconds = Math.min(max_milliseconds, Integer.MAX_VALUE);
      TimeLimit time_limit = new TimeLimit((int) max_milliseconds);

      // Initialize the auto-router engine
      AutorouteEngine autoroute_engine = routing_board.init_autoroute(p_route_net_no, autoroute_control.trace_clearance_class_no, this.thread, time_limit, this.retain_autoroute_database);

      // Do the auto-routing between the two sets of items
      AutorouteEngine.AutorouteResult autoroute_result = autoroute_engine.autoroute_connection(route_start_set, route_dest_set, autoroute_control, p_ripped_item_list);

      // Update the changed area of the board
      if (autoroute_result == AutorouteEngine.AutorouteResult.ROUTED)
      {
        routing_board.opt_changed_area(new int[0], null, this.hdlg.get_settings().get_trace_pull_tight_accuracy(), autoroute_control.trace_costs, this.thread, TIME_LIMIT_TO_PREVENT_ENDLESS_LOOP);
      }

      // Return true, if the item is routed
      return autoroute_result == AutorouteEngine.AutorouteResult.ROUTED || autoroute_result == AutorouteEngine.AutorouteResult.ALREADY_CONNECTED;
    } catch (Exception e)
    {
      return false;
    }
  }

  /**
   * Returns the airline of the current autorouted connection or null, if no such airline exists
   */
  public FloatLine get_air_line()
  {
    if (this.air_line == null)
    {
      return null;
    }
    if (this.air_line.a == null || this.air_line.b == null)
    {
      return null;
    }
    return this.air_line;
  }

  // Calculates the shortest distance between two sets of items, specifically between Pin and Via items (pins and vias are connectable DrillItems)
  private void calc_airline(Collection<Item> p_from_items, Collection<Item> p_to_items)
  {
    FloatPoint from_corner = null;
    FloatPoint to_corner = null;
    double min_distance = Double.MAX_VALUE;
    for (Item curr_from_item : p_from_items)
    {
      if (!(curr_from_item instanceof DrillItem))
      {
        continue;
      }
      FloatPoint curr_from_corner = ((DrillItem) curr_from_item).get_center().to_float();
      for (Item curr_to_item : p_to_items)
      {
        if (!(curr_to_item instanceof DrillItem))
        {
          continue;
        }
        FloatPoint curr_to_corner = ((DrillItem) curr_to_item).get_center().to_float();
        double curr_distance = curr_from_corner.distance_square(curr_to_corner);
        if (curr_distance < min_distance)
        {
          min_distance = curr_distance;
          from_corner = curr_from_corner;
          to_corner = curr_to_corner;
        }
      }
    }
    this.air_line = new FloatLine(from_corner, to_corner);
  }
}