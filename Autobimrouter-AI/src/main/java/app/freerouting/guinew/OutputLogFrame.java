package app.freerouting.guinew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class OutputLogFrame extends JDialog implements ActionListener {
    private final JTextArea outputArea;
    static JButton closeButton;
    static JPanel panel;
    private MainFrame mainFrame;

    private ButtonClickListener listener;

    public OutputLogFrame(ButtonClickListener listener) {
        this.setTitle("Output");
        this.setSize(500, 460);

        this.listener = listener;

        // this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        this.setLayout(new BorderLayout());

        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.outputArea = new JTextArea("Output goes here");
        outputArea.setSize(200, 200);
        outputArea.setBackground(Color.white);
        outputArea.setForeground(Color.black);
        outputArea.setEditable(false);
        outputArea.setVisible(true);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        this.closeButton = new JButton("Close");
        closeButton.setSize(60, 30);
        closeButton.addActionListener(this);

        this.add(outputArea, BorderLayout.CENTER);
        this.add(closeButton, BorderLayout.SOUTH);
    }

    public void setText(String text) {
        String outputText = String.format("File saved at %s", text);
        outputArea.setText(outputText);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String input = e.getActionCommand();

        if (input.equals("Close")) {
            System.out.println(input + " clicked!");
            // mainFrame.resetApplication();

        }

        listener.onButtonClicked(input);

    }
}
