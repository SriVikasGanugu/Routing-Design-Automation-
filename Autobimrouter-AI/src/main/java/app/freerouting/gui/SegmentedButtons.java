package app.freerouting.gui;

import app.freerouting.management.TextManager;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SegmentedButtons extends JPanel
{
  private final ButtonGroup buttonGroup;
  private final Map<JToggleButton, String> buttonValues;
  private String selectedValue;
  private final Color textColor = new Color(249, 155, 28); // Text color
  private final Color selectedTextColor = Color.white; // Text color for selected button
  private final Color selectedColor = new Color(30, 30, 30); // Background color for selected button
  private final Color hoverColor = new Color(220, 220, 220); // Hover color
  private final Color selectedAndHoverColor = new Color(50, 50, 50); // Selected and hover color
  private final Color borderColor = new Color(121, 116, 126); // Border color around the buttons
  private final int borderWidth = 1; // Border width around the buttons
  private TextManager tm;
  private final List<Consumer<String>> valueChangedEventListeners = new ArrayList<>();

  public SegmentedButtons(TextManager tm, String heading, String... values) {
    setLayout(new BorderLayout());
    this.tm = tm;
    // Set an empty border as a margin around the component
    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    // Optionally put the heading above the buttons, centered horizontally
    if (heading != null && !heading.isEmpty()) {
      JLabel headingLabel = new JLabel(heading, SwingConstants.CENTER); // Center the text in the label
      headingLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
      headingLabel.setForeground(textColor);
      headingLabel.setBackground(Color.BLACK);
      add(headingLabel, BorderLayout.NORTH); // Add the label to the NORTH
    }

    // Create a new panel for the buttons with vertical layout
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
    buttonPanel.setBackground(Color.BLACK);
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Set layout of buttonPanel to BoxLayout with Y_AXIS for vertical orientation
    add(buttonPanel, BorderLayout.CENTER); // Add the buttonPanel to the CENTER

    buttonGroup = new ButtonGroup();
    buttonValues = new LinkedHashMap<>();

    Map<String, String> valueTextMap = new LinkedHashMap<>();
    for (String value : values) {
      valueTextMap.put(value, tm.getText(value));
    }

    for (Map.Entry<String, String> entry : valueTextMap.entrySet()) {
      JToggleButton button = createSegmentButton(entry.getValue(), entry.getKey());
      button.setBackground(Color.BLACK);
      button.setForeground(Color.white);
      String tooltip = tm.getText(entry.getKey() + "_tooltip");
      button.setToolTipText(tooltip != null && !tooltip.isEmpty() ? tooltip : entry.getValue());

      buttonPanel.add(button);
      buttonGroup.add(button);
      buttonValues.put(button, entry.getKey());
    }

    // Set the first button as selected by default
    if (!buttonValues.isEmpty()) {
      JToggleButton firstButton = buttonValues.keySet().iterator().next();
      firstButton.setSelected(true);
      setSelectedValue(buttonValues.get(firstButton));
    }
  }

  private JToggleButton createSegmentButton(String text, String value)
  {
    JToggleButton button = new JToggleButton(text)
    {
      @Override
      public void setSelected(boolean selected)
      {
        super.setSelected(selected);
        if (selected)
        {
          this.setFont(new Font("Dialog", Font.BOLD, 12));
          this.setForeground(selectedTextColor);
          this.setBackground(selectedColor);
          this.setOpaque(true);
          tm.setText(this, text);
        }
        else
        {
          this.setFont(new Font("Dialog", Font.PLAIN, 12));
          this.setForeground(textColor);
          this.setOpaque(false);
          tm.setText(this, text);
        }
      }
    };

    button.setFocusPainted(false);
    button.setContentAreaFilled(true);
    button.setOpaque(false);
    button.setFont(new Font("Dialog", Font.PLAIN, 12)); // Set font here if necessary
    button.setMargin(new Insets(0, 10, 0, 10)); // 10 pixels of padding on all sides

    // Disable the default borders
    button.setBorderPainted(false);

    // Hover effect
    button.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (button.isSelected())
        {
          button.setForeground(selectedTextColor);
          button.setBackground(selectedAndHoverColor);
          button.setOpaque(true);
        }
        else
        {
          button.setForeground(textColor);
          button.setBackground(hoverColor);
          button.setOpaque(true);
        }
//        tm.setText(button, text);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (button.isSelected())
        {
          button.setForeground(selectedTextColor);
          button.setBackground(selectedColor);
          button.setOpaque(true);
        }
        else
        {
          button.setForeground(textColor);
          button.setOpaque(false);
        }
      }
    });

    // Action listener for selection changes
    button.addActionListener(e ->
    {
      selectedValue = buttonValues.get(button);
      for (Map.Entry<JToggleButton, String> entry : buttonValues.entrySet())
      {
        JToggleButton btn = entry.getKey();
        btn.setFont(new Font("Dialog", Font.PLAIN, 12)); // reset font for all
        btn.setForeground(textColor);
        btn.setOpaque(false);
        tm.setText(btn, entry.getValue());
      }

      button.setFont(new Font("Dialog", Font.BOLD, 12)); // Set bold font for selected
      button.setForeground(selectedTextColor);
      button.setBackground(selectedColor);
      button.setOpaque(true);
      tm.setText(button, text);

      this.valueChangedEventListeners.forEach(listener -> listener.accept(selectedValue));
    });

    return button;
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    for (JToggleButton button : buttonValues.keySet())
    {
      button.setEnabled(enabled);
    }
  }

  public void addValueChangedEventListener(Consumer<String> listener)
  {
    valueChangedEventListeners.add(listener);
  }

  public String getSelectedValue()
  {
    return selectedValue;
  }

  public void setSelectedValue(String value)
  {
    for (Map.Entry<JToggleButton, String> entry : buttonValues.entrySet())
    {
      JToggleButton button = entry.getKey();
      button.setSelected(entry.getValue().equals(value));
    }
  }
}