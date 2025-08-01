package app.freerouting.interactive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import app.freerouting.autoroute.BoardUpdateStrategy;
import app.freerouting.autoroute.ItemSelectionStrategy;
import app.freerouting.board.BoardObservers;
import app.freerouting.board.Communication;
import app.freerouting.board.CoordinateTransform;
import app.freerouting.board.LayerStructure;
import app.freerouting.board.RoutingBoard;
import app.freerouting.datastructures.IdNoGenerator;
import app.freerouting.designforms.specctra.DsnFile;
import app.freerouting.designforms.specctra.SessionToEagle;
import app.freerouting.designforms.specctra.SpecctraSesFileWriter;
import app.freerouting.geometry.planar.IntBox;
import app.freerouting.geometry.planar.PolylineShape;
import app.freerouting.gui.DesignFile;
import app.freerouting.logger.FRLogger;
import app.freerouting.management.FRAnalytics;
import app.freerouting.rules.BoardRules;
import app.freerouting.rules.DefaultItemClearanceClasses;

/**
 * Base implementation for headless mode
 */
public class BoardHandlingHeadless implements IBoardHandling
{
  /**
   * The file used for logging interactive action, so that they can be replayed later
   */
  public final ActivityReplayFile activityReplayFile = new ActivityReplayFile();
  /**
   * The current settings for interactive actions on the board
   */
  public Settings settings;
    /**
   * For transforming coordinates between the user and the board coordinate space
   */
  public CoordinateTransform coordinate_transform;
  /**
   * The listener for the autorouter thread
   */
  public ThreadActionListener autorouter_listener;
  /**
   * The board database used in this interactive handling.
   */
  protected RoutingBoard board;
  protected Locale locale;
  protected boolean save_intermediate_stages;
  protected float optimization_improvement_threshold;
  private byte[] serializedBoard;

  private boolean board_is_read_only = false;
  private long originalBoardChecksum = 0;

  // private InteractiveActionThreadHeadless interactive_action_thread;
  private InteractiveActionThread interactive_action_thread;
  InteractiveState interactive_state;

  private int num_threads;
  private BoardUpdateStrategy board_update_strategy;
  private String hybrid_ratio;
  private ItemSelectionStrategy item_selection_strategy;

  public BoardHandlingHeadless() {

  }

  public BoardHandlingHeadless(Locale p_locale, boolean p_save_intermediate_stages, float p_optimization_improvement_threshold)
  {
    this.locale = p_locale;
    this.save_intermediate_stages = p_save_intermediate_stages;
    this.optimization_improvement_threshold = p_optimization_improvement_threshold;
  }

  /**
   * Gets the routing board of this board handling.
   */
  @Override
  public RoutingBoard get_routing_board()
  {
    return this.board;
  }

  public synchronized void update_routing_board(RoutingBoard routing_board)
  {
    this.board = routing_board;
    serializedBoard = null;
  }

  public synchronized RoutingBoard deep_copy_routing_board()
  {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    try
    {
      if (serializedBoard == null) // cache the board byte array
      {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(bos);

        oos.writeObject(this.board); // serialize this.board
        oos.flush();

        serializedBoard = bos.toByteArray();
      }

      ByteArrayInputStream bin = new ByteArrayInputStream(serializedBoard);
      ois = new ObjectInputStream(bin);

      RoutingBoard board_copy = (RoutingBoard) ois.readObject();

      // board_copy.clear_autoroute_database();
      board_copy.clear_all_item_temporary_autoroute_data();
      board_copy.finish_autoroute();

      return board_copy;
    } catch (Exception e)
    {
      System.err.println("Exception in deep_copy_routing_board = " + e);
      return null;
    } finally
    {
      try
      {
        if (oos != null)
        {
          oos.close();
        }
        if (ois != null)
        {
          ois.close();
        }
      } catch (Exception e)
      {
      }
    }
  }

  @Override
  public Settings get_settings()
  {
    return settings;
  }

  /**
   * Initializes the manual trace widths from the default trace widths in the board rules.
   */
  @Override
  public void initialize_manual_trace_half_widths()
  {
    for (int i = 0; i < settings.manual_trace_half_width_arr.length; ++i)
    {
      settings.manual_trace_half_width_arr[i] = this.board.rules.get_default_net_class().get_trace_half_width(i);
    }
  }

  @Override
  public void create_board(IntBox p_bounding_box, LayerStructure p_layer_structure, PolylineShape[] p_outline_shapes, String p_outline_clearance_class_name, BoardRules p_rules, Communication p_board_communication)
  {
    if (this.board != null)
    {
      FRLogger.warn(" BoardHandling.create_board: app.freerouting.board already created");
    }
    int outline_cl_class_no = 0;

    if (p_rules != null)
    {
      if (p_outline_clearance_class_name != null && p_rules.clearance_matrix != null)
      {
        outline_cl_class_no = p_rules.clearance_matrix.get_no(p_outline_clearance_class_name);
        outline_cl_class_no = Math.max(outline_cl_class_no, 0);
      }
      else
      {
        outline_cl_class_no = p_rules.get_default_net_class().default_item_clearance_classes.get(DefaultItemClearanceClasses.ItemClass.AREA);
      }
    }
    this.board = new RoutingBoard(p_bounding_box, p_layer_structure, p_outline_shapes, outline_cl_class_no, p_rules, p_board_communication);

    this.settings = new Settings(this.board, this.activityReplayFile);
  }

  @Override
  public Locale get_locale()
  {
    return this.locale;
  }
}