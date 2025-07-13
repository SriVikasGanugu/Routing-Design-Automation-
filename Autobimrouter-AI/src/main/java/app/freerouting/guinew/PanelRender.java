package app.freerouting.guinew;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * JPanel component that contains the components needed for the user
 * to either begin auto-routing or open up the auto-routing settings.
 *
 * @author Austin Kwon
 * @since 2024-10-08
 */
public class PanelRender extends JPanel implements ActionListener {

    static JButton buttonRoute;
    static JButton buttonSettings;
    private ButtonClickListener listener;

    /**
     * Main constuctor for the render panel component.
     *
     * @param listener
     */
    public PanelRender(ButtonClickListener listener) {
        this.listener = listener;

        this.setBounds(0, 50, 500, 100); // Set panel bounds x, y, width, height
        this.setVisible(true);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 40, 20));

        buttonRoute = new JButton();
        buttonRoute.setSize(100, 60);
        buttonRoute.addActionListener(this);
        buttonRoute.setText("Route");
        buttonRoute.setHorizontalTextPosition(JButton.CENTER);
        buttonRoute.setVerticalTextPosition(JButton.CENTER);
        buttonRoute.setFont(new Font("Ariel", Font.PLAIN, 12));
        buttonRoute.setForeground(Color.black);
        buttonRoute.setBackground(Color.white);

        buttonSettings = new JButton();
        buttonSettings.setSize(100, 60);
        buttonSettings.addActionListener(this);
        buttonSettings.setText("Settings");
        buttonSettings.setHorizontalTextPosition(JButton.CENTER);
        buttonSettings.setVerticalTextPosition(JButton.CENTER);
        buttonSettings.setFont(new Font("Ariel", Font.PLAIN, 12));
        buttonSettings.setForeground(Color.black);
        buttonSettings.setBackground(Color.white);

        // buttonRoute.setOpaque(true);

        this.add(buttonSettings);
        this.add(buttonRoute);
    }

    /**
     * Notifies listeners that a button has been clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String input = e.getActionCommand();
        listener.onButtonClicked(input);
    }

    public void clear() {
        System.out.println("Clearing PanelRender...");

        // Reset Route button properties
        buttonRoute.setText("Route");
        buttonRoute.setFont(new Font("Ariel", Font.PLAIN, 12));
        buttonRoute.setForeground(Color.black);
        buttonRoute.setBackground(Color.white);

        // Reset Settings button properties
        buttonSettings.setText("Settings");
        buttonSettings.setFont(new Font("Ariel", Font.PLAIN, 12));
        buttonSettings.setForeground(Color.black);
        buttonSettings.setBackground(Color.white);

        // If you add dynamic components, reset them here as well.

        System.out.println("PanelRender cleared.");
    }

}
