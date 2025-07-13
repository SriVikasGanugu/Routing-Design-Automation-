# gui Folder

This folder contains all of the classes that make up the graphical user interface for the user to interact with and modify the loaded board design files in visual format.

### I. MainApplication.java

This class serves as the primary view window for the auto-router application’s graphical user interface. It creates new frames on the window whenever new board design files are created or loaded. It is also responsible for exporting board designs created in the application as individual files.

**Key Methods**

```
private static boolean InitializeGUI()
```

- This method intializes the graphical user interface whenever it is called.

```
private static BoardFrame create_board_frame(DesignFile p_design_file, JTextField p_message_field, boolean p_is_test_version, Locale p_locale,
                                                String p_design_rules_file,boolean p_save_intermediate_stages, float p_optimization_improvement_threshold,
                                                String[] p_ignore_net_classes_by_autorouter)
```

- This method creates a new board frame based on the input design file when it is called.
- This method is called within this class’s **InitializeGUI()** method.

```
private void open_board_design_action(ActionEvent evt)
```
- This method opens up a file explorer to allow the user to choose which design file to load.
- This method can only load either a SPECCTRA Design file (dsn) or a Freerouter Binary file (frb).

### II. DesignFile.java

This class serves a file handler to import and export specific design file formats. These are the following formats the application can accept or export through this class:

- **dsn** - SPECCTRA Design file
- **frb** - Freerouting Binary file
- **rules** - Rules file
- **ses** - SPECCTRA Session file
- **scr** - Eagle Script file

The DesignFile class restricts the application to only import and load dsn and frb files while allowing it to save and export ses and rules files. By default, the files are saved in ses format.

### III. BoardFrame.java

This class serves as the main view of the application. It consists of the dropdown menu, sidebar, canvas, and status bar. Whenever a new design file is loaded, the **MainApplication.java** class creates new BoardFrame objects through the **InitializeGUI()** method by calling the **create_board_frame()** method. This class is also responsible for writing and exporting the files in the formats specified in the **DesignFile.java** class.

**Key Methods**

```
private boolean saveAsBinary(File outputFile)
```
- This method writes the current board design file as a Freerouting Binary file (frb).
- The method will return **false** if the the writing process fails at any point.

```
public boolean saveAsSpecctraSessionSes(File outputFile, String designName)
```
- This method writes the current board design file as a SPECCTRA Session file (dsn)
- The method will return **false** if the the writing process fails at any point.
- By default, this method is called when the user exists out of the save window.

```
private boolean saveRulesAs(File rulesFile, String designName, BoardHandling p_board_handling)
```
- This method writes the current board design file as a Rules file.
- The method will return **false** if the the writing process fails at any point.

```
public void saveAsEagleScriptScr(File outputFile, String design_name)
```
- This method writes the current board design file as an Eagle Script file (scr).

```
public boolean saveAsSpecctraDesignDsn(File outputFile, String designName, boolean compatibilityMode)
```
- This method writes the current board design as a SPECCTRA Design file (dsn).

```
boolean load(InputStream p_input_stream, boolean isSpecctraDsn, JTextField p_message_field)
```
- This method reads an existing board design from a file. The method is configured to only read dsn files, so the method will return **false** if it attempts to read any other format.
- Upon successful dsn reading, the method will call BoardHandling’s loadFromSpecctraDsn with the input file, board observers, and a newly generated ID number.
- If unsuccessful, the method will return **false**.

### IV. BoardPanel.java

This class serves as the panel where the board design is rendered on the graphical user interface. The user can view and interact with the board design rendered on this panel. All of the user’s mouse actions (e.g. moving, clicking, dragging, holding, etc.) are handled in this class.

### V. BoardToolbar.java

<img src="/docs/gui/photos/AppUI-Sidebar.png" alt="Sidebar Preview" width="714" height="549">

This class serves as the application’s sidebar, containing all of the buttons used for auto-routing, undoing and redoing, zooming in and out, and much more. These are the component variables associated with the toolbar’s buttons:

- **settings_button**: opens up the auto-router settings on click
- **toolbar_autoroute_button**: initiates potential route generation and optimization on the board design on click
- **cancel_button**: cancels route generation on click
- **toolbar_undo_button**: renders the board design back to the previous action on click
- **toolbar_redo_button**: renders the board design forward to the most recent action on click
- **toolbar_incompletes_button**: toggles the display the rats nests (incomplete) routes on or off with every click
- **toolbar_violation_button**: toggles the display of clearance violations on or off with every click
- **toolbar_display_region_button**: enables the user to zoom into their desired region on click
- **toolbar_display_all_button**: zooms out to the whole board on click
- **delete_all_tracks**: deletes all existing tracks on the board on click
- **route_button**: enables manual routing mode on click
- **drag_button**: enables component dragging mode on click


### VI. WindowSelectParameter.java

<img src="/docs/gui/photos/AppUI-GeneralSettings.png" alt="General Settings Preview" width="302" height="478">

This class serves as the general settings window for the application, where the user can toggle the selectable layers, selectable components, and the current visible layer.

It is composed of several ActionListeners, all of which are assigned to the respective categories in the window.

**Key Methods**

```
 private class CurrentLayerListener implements ActionListener
 {
   public final int signal_layer_no;
   public final int layer_no;


   public CurrentLayerListener(int p_signal_layer_no, int p_layer_no)
   {
     signal_layer_no = p_signal_layer_no;
     layer_no = p_layer_no;
   }


   @Override
   public void actionPerformed(ActionEvent p_evt)
   {
     board_handling.set_current_layer(layer_no);
   }
 }
```
- The **CurrentLayerListener.java** class is configured to set the currently visible layer to the layer that the user clicked on.
- Once clicked on, the board components that do not belong on the selected layer will be grayed out.

```
 private class AllVisibleListener implements ActionListener
 {
   @Override
   public void actionPerformed(ActionEvent p_evt)
   {
     board_handling.settings.set_select_on_all_visible_layers(true);
   }
 }
```
- The **AllVisibleListener** class is configured to enable all components within the visible layers to be selectable.

```
 private class CurrentOnlyListener implements ActionListener
 {
   @Override
   public void actionPerformed(ActionEvent p_evt)
   {
     board_handling.settings.set_select_on_all_visible_layers(false);
   }
 }
```
- The **CurrentOnlyListener** is configured to enable only the components within the currently selected layer to be selectable.

```
 private class ItemSelectionListener implements ActionListener
```

- The ItemSelectionListener is configured to enable specific components to be selectable within the selected layers.
- Note that due to the length of the code block, it was only shortened to the class definition for this documentation.

### DSN/Application Flow

**DSN File Opening (Open)**
1. User starts up the application
2. The **MainApplication** object is created
3. **MainApplication** calls **InitializeGUI()**
4. User clicks on “File” dropdown menu
5. User clicks on “Open”
6. **MainApplication** calls **create_board_frame(...)** to open up the file explorer
7. User selects desired dsn file
8. User clicks “Open”
9. **MainApplication** calls **load()** from the **BoardFrame** class
10. **BoardFrame** checks if the file is in dsn format
11. If the file is in dsn format, **BoardFrame** calls **loadFromSpecctraDsn()** via **BoardHandling** from within **BoardPanel**. If the file was read successfully, **BoardFrame** calls **intialize_windows()** to initialize the windows. BoardFrame then notifies all its observers that a new board has been loaded via **listener.accept(boardpanel_.board_handling.get_routing_board())**.
12. If not, **load() returns false**

**File Writing (Save As)**
1. User clicks on “File” dropdown menu
2. User clicks on “Save as”
3. User selects dsn file format to save as
4. User clicks “Save”
5. **MainApplication calls ExportBoardToFile()**
6. If the no file exists or the file name extension does not end with dsn, the method returns null.
7. Otherwise, **MainApplication** calls **saveAsSpecctraDesignDsn()** through the **BoardHandling** class from within new_frame’s board_panel.
8. **BoardHandling** will check to see if the the board is read-only or if there is no output stream. If either one satisfies the condition, the **saveAsSpecctraDesignDsn()** method will immediately return **false**.
9. Otherwise, it will call **DsnFile.write()** to write the file and save it as true if the writing process was successful.
10. If the file writing was successful (**true**), the CRC32 (Cyclic Redundancy Check 32-bit) will be calculated via the **calculateCrc32()** method to check for any alterations in the writing process.
12. The boolean wasSaveSuccessful will be returned.
