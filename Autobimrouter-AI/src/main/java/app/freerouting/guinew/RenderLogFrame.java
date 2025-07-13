package app.freerouting.guinew;

import app.freerouting.logger.LogListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Satvik Chemudupati
 * @since 2024-10-05
 *        A dialog to display routing progress logs dynamically.
 *        Provides options to stop the routing process or save the output
 *        results.
 */
public class RenderLogFrame extends JDialog implements LogListener {

    // Components for the dialog
    private JTextArea logArea; // For displaying log messages
    private JButton saveButton; // Save button for saving the output file
    private Runnable saveAction; // Custom save action defined by the user
    private JButton stopButton; // Stop button for stopping the routing process
    private Runnable stopAction; // Custom stop action
    private Runnable resetAction; // Custom reset action for state cleanup

    /**
     * Constructs the RenderLogFrame dialog with a log area, save button, and stop
     * button.
     * Initializes the layout, adds components, and sets up user interaction
     * handlers.
     */
    public RenderLogFrame() {
        // Set dialog properties
        this.setTitle("Routing Progress");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); // Center the dialog on the screen
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        // Initialize the log area for displaying routing progress logs
        logArea = new JTextArea();
        logArea.setEditable(false); // Make the log area read-only
        logArea.setLineWrap(true); // Enable line wrapping for long messages
        logArea.setWrapStyleWord(true); // Wrap at word boundaries for better readability

        // Wrap the log area in a scroll pane for easier navigation
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(580, 480)); // Set preferred size for the scroll pane
        this.add(scrollPane, BorderLayout.CENTER);

        // Initialize the Save button
        saveButton = new JButton("Save");
        saveButton.setEnabled(false); // Initially disabled until routing is completed
        saveButton.addActionListener(e -> {
            if (saveAction != null) {
                saveAction.run(); // Trigger the custom save action
            }
        });

        // Initialize the Stop button
        stopButton = new JButton("Stop");
        stopButton.setEnabled(true); // Always enabled for user interaction
        stopButton.addActionListener(e -> {
            if (stopAction != null) {
                stopAction.run(); // Trigger the custom stop action
            }
        });

        // Create a panel for buttons and add them
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);
        buttonPanel.add(saveButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Add a window listener to handle the close event
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (resetAction != null) {
                    resetAction.run(); // Trigger the reset action when the window is closed
                }
                dispose(); // Dispose of the dialog
            }
        });
        // Set the dialog visible to the user
        this.setVisible(true);
    }

    /**
     * Appends a new log message to the log area.
     * This method ensures thread-safe updates to the UI using SwingUtilities.
     *
     * @param message The log message to add.
     */
    @Override
    public void onLogMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n"); // Append the message to the log area
            logArea.setCaretPosition(logArea.getDocument().getLength()); // Scroll to the bottom
        });
    }


    public void setStopAction(Runnable stopAction) {
        this.stopAction = stopAction;
    }


    public void enableSaveButton() {
        SwingUtilities.invokeLater(() -> {
            saveButton.setEnabled(true); // Enable the Save button
        });
    }

    /**
     * Sets a custom action to be executed when the Save button is clicked.
     *
     * @param saveAction A {@link Runnable} representing the save action.
     */
    public void setSaveAction(Runnable saveAction) {
        this.saveAction = saveAction;
    }

    /**
     * Sets the action to reset routing-related states when the window is closed.
     *
     * @param resetAction A {@link Runnable} representing the reset action.
     */
    public void setResetAction(Runnable resetAction) {
        this.resetAction = resetAction;
    }
}
