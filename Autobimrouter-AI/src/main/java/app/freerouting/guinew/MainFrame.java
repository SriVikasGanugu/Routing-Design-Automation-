package app.freerouting.guinew;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import app.freerouting.autoroute.BoardUpdateStrategy;
import app.freerouting.autoroute.ItemSelectionStrategy;
import app.freerouting.board.BoardObserverAdaptor;
import app.freerouting.board.BoardObservers;
import app.freerouting.filehandling.FileController;
import app.freerouting.gui.DesignFile;
import app.freerouting.interactive.InteractiveActionThread;
import app.freerouting.logger.FRLogger;
import app.freerouting.settings.GlobalSettings;

/**
 * The main view that makes up the user interface. Serves as the View component
 * in the MVC design pattern.
 *
 * @author Austin Kwon
 * @author Satvik Chemudupati
 * @since 2024-10-06
 * @see app.freerouting.guinew.WindowBase
 * @see app.freerouting.guinew.ButtonClickListener
 */

public class MainFrame extends WindowBase implements ButtonClickListener {

    static PanelFileLoad panelFileLoad;
    static PanelRender panelRender;

    private static WindowAutorouteParameter autoroute_parameter_window;

    private static RenderLogFrame render_log_frame;
    private static OutputLogFrame output_log_frame;

    private FileController fileController;
    private BoardObservers board_observers;

    public boolean is_test_version;
    public Locale locale;
    public boolean save_intermediate_stages;
    public float optimization_improvement_threshold;
    public String[] ignore_net_classes_by_autorouter;
    public int max_passes;
    public BoardUpdateStrategy board_update_strategy;
    // Issue: adding a new field into AutorouteSettings caused exception when
    // loading
    // an existing design: "Couldn't read design file", "InvalidClassException",
    // incompatible with
    // serialized data
    // so choose to pass this parameter through BoardHandling
    public String hybrid_ratio;
    public ItemSelectionStrategy item_selection_strategy;
    public int num_threads;
    public String design_dir_name;

    /**
     * Class constructor to create and render the main frame.
     *
     * @param globalSettings global app settings
     */
    public MainFrame(GlobalSettings globalSettings) {
        super(500, 140);
        System.out.println("MainFrame(GlobalSettings) constructor called!");

        this.design_dir_name = globalSettings.getDesignDir();
        this.max_passes = globalSettings.getMaxPasses();
        this.num_threads = globalSettings.getNumThreads();
        this.board_update_strategy = globalSettings.getBoardUpdateStrategy();
        this.hybrid_ratio = globalSettings.getHybridRatio();
        this.item_selection_strategy = globalSettings.getItemSelectionStrategy();
        this.is_test_version = globalSettings.isTestVersion();
        this.locale = globalSettings.getCurrentLocale();
        this.save_intermediate_stages = !globalSettings.disabledFeatures.snapshots;
        this.optimization_improvement_threshold = globalSettings.autoRouterSettings.optimization_improvement_threshold;
        this.ignore_net_classes_by_autorouter = globalSettings.autoRouterSettings.ignore_net_classes_by_autorouter;

        this.setTitle("Auto BIM Router");
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        panelFileLoad = new PanelFileLoad(this);
        panelRender = new PanelRender(this);

        this.add(panelRender, BorderLayout.CENTER);
        this.add(panelFileLoad, BorderLayout.CENTER);

        board_observers = new BoardObserverAdaptor();

        this.fileController = new FileController(this, board_observers);

        this.rerenderComponents();
    }

    /**
     * Refresh and update the components each time the loaded file
     * has been updated.
     */
    public void rerenderComponents() {
        System.out.println("MainFrame rerenderComponents() called!");
        revalidate();
        repaint();
    }

    /**
     * Displays the error message dialog when an error occurs.
     *
     * @param e
     */
    public void displayError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a message dialog stating that no file has been loaded.
     */
    public void displayFileNotLoaded() {
        FRLogger.warn("no file loaded!");
        JOptionPane.showMessageDialog(this, "No file has been loaded! Please load a design file first.",
                "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Update the selected file directory
     *
     * @param design_dir_name the directory name of the selected file
     */
    public void setFileDirectory(String design_dir_name) {
        this.design_dir_name = design_dir_name;
    }

    /**
     * Get the name of the directory of the selected file
     *
     * @return
     */
    public String getFileDirectory() {
        return this.design_dir_name;
    }

    /**
     * Get the current file controller
     *
     * @return
     */
    public FileController getFileController() {
        return this.fileController;
    }

    @Override
    public void onButtonClicked(String buttonName) {
        System.out.println(buttonName + " was clicked!");

        switch (buttonName) {

            /**
             * Handles the "Load" button click.
             * Opens a file dialog for the user to load a design file. If a file is
             * successfully
             * loaded, it updates the file controller, the UI, and board handler settings.
             * Logs warnings if no file is loaded or errors occur during the loading
             * process.
             */
            case "Load":
                System.out.println("Loading file...");
                try {
                    // Show file open dialog for the user to select a design file
                    File inputFile = DesignFile.showOpenDialog(design_dir_name, this);

                    // Set the input file in the file controller
                    fileController.setInputFile(inputFile);
                    if (fileController.getInputFile() != null) {
                        // Update the file controller with the loaded file
                        fileController.setDesignFile(fileController.getInputFile());
                        fileController.load();
                        System.out.println("Loaded file: " + fileController.getDesignFileName());

                        // Update the UI to display the loaded file name
                        panelFileLoad.setFileName(fileController.getDesignFileName());

                        // Update board handler settings for the loaded file
                        fileController.updateBoardHandlerSettings();
                    } else {
                        // Log a warning if no file is selected
                        FRLogger.warn("no file loaded!");
                    }
                } catch (Exception e) {
                    // Log an error and show a message if file loading fails
                    FRLogger.error("unable to load file", e);
                    displayFileNotLoaded();
                }
                break;

            /**
             * Handles the "Settings" button click.
             * Opens the autoroute parameter settings window if a file is loaded.
             * Displays a warning if no file is loaded.
             */
            case "Settings":
                System.out.println("Opening settings...");
                if (fileController.getInputFile() != null) {
                    // Create the settings window
                    this.autoroute_parameter_window = new WindowAutorouteParameter(this);
                } else {
                    // Log a warning and display a message if no file is loaded
                    FRLogger.warn("no file loaded!");
                    displayFileNotLoaded();
                }

                // Make the settings window visible if it is initialized
                if (autoroute_parameter_window != null) {
                    autoroute_parameter_window.setVisible(true);
                }
                break;

            /**
             * Handles the "Route" button click.
             * Initiates the routing process if a design file is loaded. Displays a
             * routing log frame to track progress. Allows the user to stop routing
             * or save the results. The process runs in a background thread using
             * SwingWorker.
             */
            case "Route":
                System.out.println("Route button clicked!");

                // Check if a file is loaded before starting the routing process
                if ((fileController.getInputFile() != null) && (fileController.getDesignFile() != null)) {
                    // Initialize the log frame to display routing progress
                    render_log_frame = new RenderLogFrame();
                    FRLogger.addLogListener(render_log_frame);
                    render_log_frame.setVisible(true);

                    final boolean[] stopRouting = { false }; // Flag for stopping the routing process

                    // Define the stop action for the log frame
                    render_log_frame.setStopAction(() -> {
                        System.out.println("Stopping routing process...");
                        stopRouting[0] = true; // Set the flag to stop routing
                        resetRoutingState(); // Reset the routing state
                    });

                    // Define a reset action for the window close
                    render_log_frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            stopRouting[0] = true; // Ensure the routing process is stopped
                            resetRoutingState(); // Reset the routing state when the window is closed
                        }

                    });

                    // Define the save action for the log frame
                    render_log_frame.setSaveAction(() -> {
                        try {
                            // Open a Save As dialog to let the user save the output file
                            File outputFile = fileController.getDesignFile().showSaveAsDialog(null, null);
                            if (outputFile != null && fileController.writeOutputFile(outputFile)) {
                                // Disable the main frame after successful saving
                                MainFrame.this.setEnabled(false);

                                // Show the output log frame with the saved file's path
                                output_log_frame = new OutputLogFrame(MainFrame.this);
                                output_log_frame.setText(fileController.getOutputFile().getPath());
                                output_log_frame.setVisible(true);
                            } else {
                                // Display an error message if saving fails
                                JOptionPane.showMessageDialog(
                                        render_log_frame,
                                        "Failed to save the file. Please try again.",
                                        "Save Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            // Handle unexpected errors during the save action
                            JOptionPane.showMessageDialog(
                                    render_log_frame,
                                    "An unexpected error occurred: " + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    // Perform the routing process in the background
                    SwingWorker<Void, String> worker = new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            // Loop to handle the routing process until stopped
                            while (!App.globalSettings.flag) {
                                if (stopRouting[0]) {
                                    // Log and exit if the user stops routing
                                    FRLogger.info("Routing stopped by user.");
                                    break;
                                }

                                // Start autorouter and route optimizer
                                InteractiveActionThread thread = fileController.boardHandling
                                        .start_autorouter_and_route_optimizer();

                                // Attach listeners to the autorouter if available
                                if (fileController.boardHandling.autorouter_listener != null) {
                                    thread.addListener(fileController.boardHandling.autorouter_listener);
                                }

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            // Log completion or stopping of the routing process
                            if (stopRouting[0]) {
                                FRLogger.info("Routing stopped by user.");
                            } else {
                                FRLogger.info("Routing completed!");
                            }
                            // Enable the Save button after routing is done
                            render_log_frame.enableSaveButton();
                            System.out.println("Save button has been enabled.");
                        }
                    };

                    // Start the SwingWorker thread for routing
                    worker.execute();

                } else {
                    // Log a warning and display a message if no file is loaded
                    FRLogger.warn("no file loaded!");
                    JOptionPane.showMessageDialog(this, "No file has been loaded! Please load a design file first.",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                break;

            /**
             * Handles the "Close" button click.
             * Resets the application to its initial state and re-enables the main frame.
             */
            case "Close":
                this.setEnabled(true); // Re-enable the main frame
                resetApplication(); // Reset the application state
                break;

            /**
             * Default case for unhandled buttons.
             * Performs no action.
             */
            default:
                break;
        }
    }

    private void resetRoutingState() {
        System.out.println("Resetting routing state...");

        // Clear routing-related states
        App.globalSettings.flag = false; // Reset global routing flag
        if (render_log_frame != null) {
            render_log_frame.dispose(); // Dispose of the log frame
            render_log_frame = null;
        }
        if (output_log_frame != null) {
            output_log_frame.dispose(); // Dispose of the output log frame
            output_log_frame = null;
        }

        // Reset UI components
        SwingUtilities.invokeLater(() -> {
            panelFileLoad.setFileName(""); // Clear the file name label
            panelRender.clear(); // Reset the render panel (implement the clear method in PanelRender)
            this.setEnabled(true); // Re-enable the main frame
        });

        System.out.println("Routing state reset complete.");
    }

    public void resetApplication() {
        System.out.println("Resetting application to initial state...");

        // Reset routing-related states
        resetRoutingState();

        // Clear file references
        fileController.setInputFile(null);
        fileController.setDesignFile(null);

        // Reset global settings
        App.globalSettings.flag = false;

        // Reset UI components
        SwingUtilities.invokeLater(() -> {
            panelFileLoad = new PanelFileLoad(this);
            panelRender = new PanelRender(this);

            this.getContentPane().removeAll(); // Remove existing UI components
            this.add(panelRender, BorderLayout.CENTER);
            this.add(panelFileLoad, BorderLayout.CENTER);

            rerenderComponents();
        });

        System.out.println("Application reset completed.");
    }

}
