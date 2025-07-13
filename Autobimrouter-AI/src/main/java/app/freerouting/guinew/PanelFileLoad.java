package app.freerouting.guinew;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel component that contains the components needed for the user
 * to load their desired design files.
 *
 * @author Austin Kwon
 * @since 2024-10-07
 */
public class PanelFileLoad extends JPanel implements ActionListener {

    static JButton buttonLoad;
    // static JTextField textField;
    static JLabel textName;
    private ButtonClickListener listener;

    /**
     * Main constructor for the file loading panel component.
     *
     * @param listener  Button click listener
     */
    public PanelFileLoad(ButtonClickListener listener) {

        this.listener = listener;

        this.setBounds(0, 25, 500, 100);	// Set panel bounds x, y, width, height
        this.setVisible(true);
    	this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        buttonLoad = new JButton();
        buttonLoad.setSize(60, 30);
		buttonLoad.addActionListener(this);
		buttonLoad.setText("Load");
		buttonLoad.setHorizontalTextPosition(JButton.CENTER);
		buttonLoad.setVerticalTextPosition(JButton.CENTER);
		buttonLoad.setFont(new Font("Ariel", Font.PLAIN, 12));
		buttonLoad.setForeground(Color.black);
		buttonLoad.setBackground(Color.white);

        textName = new JLabel();
        textName.setPreferredSize(new Dimension(200, 30));
        textName.setForeground(Color.BLACK);
        textName.setBackground(Color.WHITE);
        textName.setOpaque(true);

        this.add(buttonLoad);
        this.add(textName);
    }

    /**
     * Initializes the file loading panel.
     */
    public void initialize() {
        System.out.println("PanelFileLoad initialize() called!");
        revalidate();
        repaint();
    }

    /**
     * Update the textName label to display the name of the loaded
     * file.
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        textName.setText(fileName);
    }

    /**
     * Notifies listeners that a button has been clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

		String input = e.getActionCommand();
        listener.onButtonClicked(input);
	}

}
