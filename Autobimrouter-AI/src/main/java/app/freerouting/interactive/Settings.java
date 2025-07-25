package app.freerouting.interactive;

import app.freerouting.board.ItemSelectionFilter;
import app.freerouting.board.RoutingBoard;
import app.freerouting.logger.FRLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Contains the values of the interactive settings of the board handling.
 */
public class Settings implements Serializable
{
  /**
   * The array of manual trace half widths, initially equal to the automatic trace half widths.
   */
  final int[] manual_trace_half_width_arr;
  public AutorouteSettings autoroute_settings;
  /**
   * the current layer
   */
  int layer;
  /**
   * allows pushing obstacles aside
   */
  boolean push_enabled;
  /**
   * allows dragging components with the route
   */
  boolean drag_components_enabled;
  /**
   * indicates if interactive selections are made on all visible layers or only on the current layer
   */
  boolean select_on_all_visible_layers;
  /**
   * Route mode: stitching or dynamic
   */
  boolean is_stitch_route;
  /**
   * Skip Pins
   */
  boolean is_skip_pins;
  /**
   * The width of the pull tight region of traces around the cursor
   */
  int trace_pull_tight_region_width;
  /**
   * The accuracy of the pull tight algorithm.
   */
  int trace_pull_tight_accuracy;
  /**
   * Via snaps to smd center, if attach smd is allowed.
   */
  boolean via_snap_to_smd_center;
  /**
   * The horizontal placement grid when moving components, if {@literal >} 0.
   */
  int horizontal_component_grid;
  /**
   * The vertical placement grid when moving components, if {@literal >} 0.
   */
  int vertical_component_grid;
  /**
   * If true, the trace width at static pins smaller the trace width will be lowered
   * automatically to the pin with, if necessary.
   */
  boolean automatic_neckdown;
  /**
   * Indicates if the routing rule selection is manual by the user or automatic by the net rules.
   */
  boolean manual_rule_selection;
  /**
   * If true, the current routing obstacle is hilightet in dynamic routing.
   */
  boolean hilight_routing_obstacle;
  /**
   * The index of the clearance class used for traces in interactive routing in the clearance
   * matrix, if manual_route_selection is on.
   */
  int manual_trace_clearance_class;
  /**
   * The index of the via rule used in routing in the board via rules if manual_route_selection is
   * on.
   */
  int manual_via_rule_index;
  /**
   * If true, the mouse wheel is used for zooming.
   */
  boolean zoom_with_wheel;
  /**
   * The filter used in interactive selection of board items.
   */
  ItemSelectionFilter item_selection_filter;
  /**
   * Defines the data of the snapshot selected for restoring.
   */
  SnapShot.Attributes snapshot_attributes;
  /**
   * Indicates, if the data of this class are not allowed to be changed in interactive board
   * editing.
   */
  private transient boolean read_only = false;
  /**
   * The file used for logging interactive action, so that they can be replayed later
   */
  private transient ActivityReplayFile activityReplayFile;

  /**
   * Creates a new interactive settings variable.
   */
  public Settings(RoutingBoard p_board, ActivityReplayFile p_activityReplayFile)
  {
    this.activityReplayFile = p_activityReplayFile;
    // Initialise with default values.
    layer = 0;
    push_enabled = true;
    drag_components_enabled = true;
    select_on_all_visible_layers = true; // else selection is only on the current layer
    is_stitch_route = false; // else interactive routing is dynamic
    is_skip_pins = true;
    trace_pull_tight_region_width = Integer.MAX_VALUE;
    trace_pull_tight_accuracy = 500;
    via_snap_to_smd_center = true;
    horizontal_component_grid = 0;
    vertical_component_grid = 0;
    automatic_neckdown = true;
    manual_rule_selection = false;
    hilight_routing_obstacle = false;
    manual_trace_clearance_class = 1;
    manual_via_rule_index = 0;
    zoom_with_wheel = true;
    manual_trace_half_width_arr = new int[p_board.get_layer_count()];
    Arrays.fill(manual_trace_half_width_arr, 1000);
    autoroute_settings = new AutorouteSettings(p_board);
    item_selection_filter = new ItemSelectionFilter();
    snapshot_attributes = new SnapShot.Attributes();
  }

  /**
   * Copy constructor
   */
  public Settings(Settings p_settings)
  {
    this.activityReplayFile = p_settings.activityReplayFile;
    this.read_only = p_settings.read_only;
    this.layer = p_settings.layer;
    this.push_enabled = p_settings.push_enabled;
    this.drag_components_enabled = p_settings.drag_components_enabled;
    this.select_on_all_visible_layers = p_settings.select_on_all_visible_layers;
    this.is_stitch_route = p_settings.is_stitch_route;
    this.is_skip_pins = p_settings.is_skip_pins;
    this.trace_pull_tight_region_width = p_settings.trace_pull_tight_region_width;
    this.trace_pull_tight_accuracy = p_settings.trace_pull_tight_accuracy;
    this.via_snap_to_smd_center = p_settings.via_snap_to_smd_center;
    this.horizontal_component_grid = p_settings.horizontal_component_grid;
    this.vertical_component_grid = p_settings.vertical_component_grid;
    this.automatic_neckdown = p_settings.automatic_neckdown;
    this.manual_rule_selection = p_settings.manual_rule_selection;
    this.hilight_routing_obstacle = p_settings.hilight_routing_obstacle;
    this.zoom_with_wheel = p_settings.zoom_with_wheel;
    this.manual_trace_clearance_class = p_settings.manual_trace_clearance_class;
    this.manual_via_rule_index = p_settings.manual_via_rule_index;
    this.manual_trace_half_width_arr = new int[p_settings.manual_trace_half_width_arr.length];
    System.arraycopy(p_settings.manual_trace_half_width_arr, 0, this.manual_trace_half_width_arr, 0, this.manual_trace_half_width_arr.length);
    this.autoroute_settings = new AutorouteSettings(p_settings.autoroute_settings);
    this.item_selection_filter = new ItemSelectionFilter(p_settings.item_selection_filter);
    this.snapshot_attributes = new SnapShot.Attributes(p_settings.snapshot_attributes);
  }

  public int get_layer()
  {
    return this.layer;
  }

  /**
   * allows pushing obstacles aside
   */
  public boolean get_push_enabled()
  {
    return this.push_enabled;
  }

  /**
   * Enables or disables pushing obstacles in interactive routing
   */
  public void set_push_enabled(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    push_enabled = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_PUSH_ENABLED, p_value);
  }

  /**
   * Route mode: stitching or dynamic
   */
  public boolean get_is_stitch_route()
  {
    return this.is_stitch_route;
  }
  /**
   * Skip pins
   */
  public boolean get_is_skip_pins()
  {
    return this.is_skip_pins;
  }

  /**
   * allows dragging components with the route
   */
  public boolean get_drag_components_enabled()
  {
    return this.drag_components_enabled;
  }

  /**
   * Enables or disables dragging components
   */
  public void set_drag_components_enabled(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    drag_components_enabled = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_DRAG_COMPONENTS_ENABLED, p_value);
  }

  /**
   * indicates if interactive selections are made on all visible layers or only on the current
   * layer.
   */
  public boolean get_select_on_all_visible_layers()
  {
    return this.select_on_all_visible_layers;
  }

  /**
   * Sets, if item selection is on all board layers or only on the current layer.
   */
  public void set_select_on_all_visible_layers(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    select_on_all_visible_layers = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_SELECT_ON_ALL_LAYER, p_value);
  }

  /**
   * Indicates if the routing rule selection is manual by the user or automatic by the net rules.
   */
  public boolean get_manual_rule_selection()
  {
    return this.manual_rule_selection;
  }

  /**
   * Via snaps to smd center, if attach smd is allowed.
   */
  public boolean get_via_snap_to_smd_center()
  {
    return this.via_snap_to_smd_center;
  }

  /**
   * Changes, if vias snap to smd center, if attach smd is allowed.
   */
  public void set_via_snap_to_smd_center(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    via_snap_to_smd_center = p_value;
  }

  /**
   * If true, the current routing obstacle is hilightet in dynamic routing.
   */
  public boolean get_hilight_routing_obstacle()
  {
    return this.hilight_routing_obstacle;
  }

  /**
   * If true, the current routing obstacle is hilightet in dynamic routing.
   */
  public void set_hilight_routing_obstacle(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    this.hilight_routing_obstacle = p_value;
  }

  /**
   * If true, the trace width at static pins smaller the trace width will be lowered
   * automatically to the pin with, if necessary.
   */
  public boolean get_automatic_neckdown()
  {
    return this.automatic_neckdown;
  }

  /**
   * If true, the trace width at static pins smaller the trace width will be lowered
   * automatically to the pin with, if necessary.
   */
  public void set_automatic_neckdown(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    this.automatic_neckdown = p_value;
  }

  /**
   * If true, the mouse wheel is used for zooming.
   */
  public boolean get_zoom_with_wheel()
  {
    return this.zoom_with_wheel;
  }

  /**
   * If true, the wheel is used for zooming.
   */
  public void set_zoom_with_wheel(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    if (zoom_with_wheel != p_value)
    {
      zoom_with_wheel = p_value;
      if (activityReplayFile != null)
      {
        activityReplayFile.start_scope(ActivityReplayFileScope.SET_ZOOM_WITH_WHEEL, p_value);
      }
    }
  }

  /**
   * The filter used in interactive selection of board items.
   */
  public ItemSelectionFilter get_item_selection_filter()
  {
    return this.item_selection_filter;
  }

  /**
   * The filter used in interactive selection of board items.
   */
  public void set_item_selection_filter(ItemSelectionFilter p_value)
  {
    if (read_only)
    {
      return;
    }
    this.item_selection_filter = p_value;
  }

  /**
   * The width of the pull tight region of traces around the cursor
   */
  public int get_trace_pull_tight_region_width()
  {
    return this.trace_pull_tight_region_width;
  }

  /**
   * The horizontal placement grid when moving components, if {@literal >} 0.
   */
  public int get_horizontal_component_grid()
  {
    return this.horizontal_component_grid;
  }

  /**
   * The horizontal placement grid when moving components, if {@literal >} 0.
   */
  public void set_horizontal_component_grid(int p_value)
  {
    if (read_only)
    {
      return;
    }
    this.horizontal_component_grid = p_value;
  }

  /**
   * The vertical placement grid when moving components, if {@literal >} 0.
   */
  public int get_vertical_component_grid()
  {
    return this.vertical_component_grid;
  }

  /**
   * The vertical placement grid when moving components, if {@literal >} 0.
   */
  public void set_vertical_component_grid(int p_value)
  {
    if (read_only)
    {
      return;
    }
    this.vertical_component_grid = p_value;
  }

  /**
   * The index of the clearance class used for traces in interactive routing in the clearance
   * matrix, if manual_route_selection is on.
   */
  public int get_manual_trace_clearance_class()
  {
    return this.manual_trace_clearance_class;
  }

  /**
   * The index of the clearance class used for traces in interactive routing in the clearance
   * matrix, if manual_route_selection is on.
   */
  public void set_manual_trace_clearance_class(int p_index)
  {
    if (read_only)
    {
      return;
    }
    manual_trace_clearance_class = p_index;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_MANUAL_TRACE_CLEARANCE_CLASS, p_index);
  }

  /**
   * The index of the via rule used in routing in the board via rules if manual_route_selection is
   * on.
   */
  public int get_manual_via_rule_index()
  {
    return this.manual_via_rule_index;
  }

  /**
   * The index of the via rule used in routing in the board via rules if manual_route_selection is
   * on.
   */
  public void set_manual_via_rule_index(int p_value)
  {
    if (read_only)
    {
      return;
    }
    this.manual_via_rule_index = p_value;
  }

  /**
   * The accuracy of the pull tight algorithm.
   */
  public int get_trace_pull_tight_accuracy()
  {
    return this.trace_pull_tight_accuracy;
  }

  /**
   * Defines the data of the snapshot selected for restoring.
   */
  public SnapShot.Attributes get_snapshot_attributes()
  {
    return this.snapshot_attributes;
  }

  /**
   * Get the trace half width in manual routing mode on layer p_layer_no
   */
  public int get_manual_trace_half_width(int p_layer_no)
  {
    if (p_layer_no < 0 || p_layer_no >= this.manual_trace_half_width_arr.length)
    {
      FRLogger.warn("Settings.get_manual_trace_half_width p_layer_no out of range");
      return 0;
    }
    return this.manual_trace_half_width_arr[p_layer_no];
  }

  /**
   * Route mode: stitching or dynamic
   */
  public void set_stitch_route(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    is_stitch_route = p_value;

    activityReplayFile.start_scope(ActivityReplayFileScope.SET_STITCH_ROUTE, p_value);
  }
  /**
   * Route mode: stitching or dynamic
   */
  public void set_skip_pins(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    is_skip_pins = p_value;

    //  activityReplayFile.start_scope(ActivityReplayFileScope.SET_STITCH_ROUTE, p_value);
  }

  /**
   * Changes the current width of the tidy region for traces.
   */
  public void set_current_pull_tight_region_width(int p_value)
  {
    if (read_only)
    {
      return;
    }
    trace_pull_tight_region_width = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_PULL_TIGHT_REGION_WIDTH, p_value);
  }

  /**
   * Changes the current width of the pull tight accuracy for traces.
   */
  public void set_current_pull_tight_accuracy(int p_value)
  {
    if (read_only)
    {
      return;
    }
    trace_pull_tight_accuracy = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_PULL_TIGHT_ACCURACY, p_value);
  }

  /**
   * Sets the current trace width selection to manual or automatic.
   */
  public void set_manual_tracewidth_selection(boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    manual_rule_selection = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_MANUAL_TRACEWIDTH_SELECTION, p_value);
  }

  /**
   * Sets the manual trace half width used in interactive routing.
   */
  public void set_manual_trace_half_width(int p_layer_no, int p_value)
  {
    if (read_only)
    {
      return;
    }
    manual_trace_half_width_arr[p_layer_no] = p_value;
    activityReplayFile.start_scope(ActivityReplayFileScope.SET_MANUAL_TRACE_HALF_WIDTH, p_layer_no);
    activityReplayFile.add_int(p_value);
  }

  /**
   * Changes the interactive selectability of p_item_type.
   */
  public void set_selectable(ItemSelectionFilter.SelectableChoices p_item_type, boolean p_value)
  {
    if (read_only)
    {
      return;
    }
    item_selection_filter.set_selected(p_item_type, p_value);

    activityReplayFile.start_scope(ActivityReplayFileScope.SET_SELECTABLE, p_item_type.ordinal());
    int logged_value;
    if (p_value)
    {
      logged_value = 1;
    }
    else
    {
      logged_value = 0;
    }
    activityReplayFile.add_int(logged_value);
  }

  /**
   * Defines, if the setting attributes are allowed to be changed interactively or not.
   */
  public void set_read_only(Boolean p_value)
  {
    this.read_only = p_value;
  }

  void set_logfile(ActivityReplayFile p_activityReplayFile)
  {
    this.activityReplayFile = p_activityReplayFile;
  }

  /**
   * Reads an instance of this class from a file
   */
  private void readObject(ObjectInputStream p_stream) throws IOException, ClassNotFoundException
  {
    p_stream.defaultReadObject();
    if (this.item_selection_filter == null)
    {
      FRLogger.warn("Settings.readObject: item_selection_filter is null");
      this.item_selection_filter = new ItemSelectionFilter();
    }
    if (this.snapshot_attributes == null)
    {
      FRLogger.warn("Settings.readObject: snapshot_attributes is null");
      this.snapshot_attributes = new SnapShot.Attributes();
    }
    this.read_only = false;
  }
}