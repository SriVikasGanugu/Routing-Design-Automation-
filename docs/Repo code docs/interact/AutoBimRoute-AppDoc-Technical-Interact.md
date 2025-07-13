# interactive Folder

The interactive folder contains all the classes that handle the graphical user interface interactivity. It registers all the user input sent from the interface to perform the necessary calculations and send the results back to the interface.

### I. InteractiveState.java

This class serves as the base class of all interactive states within the graphical user interface. It is the controller behind the application’s interface, which handles all of the user inputs sent from the front end to the back end.

**Key Methods**

### II. BoardHandling.java

This class serves as the controller behind the graphical user interface. It serves as a connection between the interface and the board database by handling all of the user inputs with the board design rendered in the interface. Since this class inherits from the **BoardHandlingHeadless.java** class, it has access to all its variables and methods.

**Key Methods**

```
 public void draw(Graphics p_graphics)
 {
   if (board == null)
   {
     return;
   }
   board.draw(p_graphics, graphics_context);


   if (ratsnest != null)
   {
     ratsnest.draw(p_graphics, graphics_context);
   }
   if (clearance_violations != null)
   {
     clearance_violations.draw(p_graphics, graphics_context);
   }
   if (interactive_state != null)
   {
     interactive_state.draw(p_graphics);
   }
   if (interactive_action_thread != null)
   {
     interactive_action_thread.draw(p_graphics);
   }
 }
```
- This method is draws the board design with all the configurations from the **WindowsSelectParameter.java** class.

```
 public void set_layer_visibility(int p_layer, double p_value)
```
- This method modifies the board layer visibility displayed in the graphical user interface.
- The p_value is expected to be between 0 and 1.

```
 public void generate_snapshot()
```
- This method calls the routing board within BoardHandlingHeadless.java class to generate a snapshot for undoing and redoing.
- The snapshot will contain the board design’s saved state to allow the user to revisit should they need to correct the design.
- This method calls generate_snapshot() within BoardHandlingHeadless.java’s RoutingBoard board variable, which inherits from the BasicBoard.java class.
- BasicBoard.java’s generate_snapshot() method will be called via BoardHandlingHeadless.java’s RoutingBoard board.

```
 public void repaint()
```
- This method repaints the board panel in the graphical user interface.

```
 public void repaint(Rectangle p_rect)
```
- By using a Rectangle object, this method repaints the rectangular boarder of the board panel in the graphical user interface.

```
 public void generate_snapshot()
 {
   if (board_is_read_only)
   {
     return;
   }
   board.generate_snapshot();
   activityReplayFile.start_scope(ActivityReplayFileScope.GENERATE_SNAPSHOT);
 }
```

- This method generates snapshots for the user to restore previous or most recent actions performed on the board.
- The snapshot won’t be generated if the board is restricted to read-only.

```
 public void undo()
```

- This method restores the situation before the previous snapshot generated.
- This method is called when the user clicks on the toolbar_undo_button in the BoardToolbar.java class.

```
 public void redo()
```
- This method restores the situation before the last undone action.
- This method is called when the user clicks on the toolbar_redo_button in the BoardToolbar.java class.

```
 public boolean loadFromBinary(ObjectInputStream p_design)
```
- This method loads a frb file from the file explorer upon selecting a specific frb file.

```
 public DsnFile.ReadResult loadFromSpecctraDsn(InputStream p_design, BoardObservers p_observers, IdNoGenerator p_item_id_no_generator)
```
- This method loads a dsn file from the file explorer upon selecting a specific dsn file.
- The method will return false if the file is corrupted and unreadable.


### III. BoardHandlingHeadless.java

This class is similar to the **BoardHandling.java** class, except that it is primarily used to handle the board interactions in case the graphical user interface is not available to use. It serves as a parent class to the **BoardHandling.java** class.

**Key Methods**
```
 @Override
 public RoutingBoard get_routing_board()
 {
   return this.board;
 }
```

- This method retrieves the current routing board loaded when it is called.

```
 @Override
 public void create_board(IntBox p_bounding_box, LayerStructure p_layer_structure, PolylineShape[] p_outline_shapes,
                            String p_outline_clearance_class_name, BoardRules p_rules, Communication p_board_communication)
```

- This method creates a new circuit board design when it is called.
- This method will be overridden by the **create_board()** method defined in the **BoardHandling.java** class.