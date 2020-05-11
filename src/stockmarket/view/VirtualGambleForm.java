package stockmarket.view;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.GridLayout;


import stockmarket.controller.Features;

/**
 * This class represents a form which is used to take user inputs or to show messages to the user.
 */
public class VirtualGambleForm extends JFrame {
  private final JPanel panel;
  private SortedMap<String, JTextField> textFieldMap;
  private JButton submitButton;
  private JLabel messageLabel;
  private Features features;

  /**
   * This constructor creates a form to take user inputs.
   *
   * @param title   title of the form
   * @param entries user inputs to be taken
   */
  public VirtualGambleForm(String title, String[] entries) {
    super(title);
    panel = new JPanel();
    panel.setLayout(new GridLayout(20, 2));
    setSize(500, 300);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    messageLabel = new JLabel("");
    textFieldMap = new TreeMap<>(fieldComparator);
    for (String entry : entries) {
      textFieldMap.put(entry, new JTextField(10));
    }
    for (Map.Entry<String, JTextField> entry : textFieldMap.entrySet()) {
      JLabel label = new JLabel();
      label.setText(entry.getKey());
      panel.add(label);
      panel.add(entry.getValue());
    }
    submitButton = new JButton("submit");
    panel.add(submitButton);

    this.add(panel);
    pack();
  }

  /**
   * This creates a window to display a message to the user.
   */
  public VirtualGambleForm(String message) {
    super("Message");
    panel = new JPanel();
    panel.setLayout(new GridLayout(7, 2));
    setSize(500, 300);
    setLocation(200, 200);
    messageLabel = new JLabel();
    messageLabel.setText(message);
    panel.add(messageLabel);
    this.add(panel);
  }

  /**
   * Method to create label text to input string map from text to text field map.
   *
   * @return map from label to user input
   */
  private Map<String, String> getInputMap() {
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, JTextField> entry : this.textFieldMap.entrySet()) {
      map.put(entry.getKey(), entry.getValue().getText());
    }
    return map;
  }

  /**
   * Method to set the features provided by the form.
   *
   * @param f features provided by the form
   */
  public void setFeature(Features f) {
    this.features = f;
    submitButton.addActionListener(l -> features.processInput(this.getTitle(), getInputMap()));
    submitButton.addActionListener(l -> this.dispose());
  }

  private Comparator<String> fieldComparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
      String[] priorityIndex = new String[]{"Commission", "Amount", "End Date", "Start Date",
        "Date", "Company Ticker", "Strategy Name", "PortfolioID"};
      List<String> priorityList = Arrays.asList(priorityIndex);

      int ret = priorityList.indexOf(s2) - priorityList.indexOf(s1);
      if (ret == 0) {
        ret = s1.compareTo(s2);
      }
      return ret;
    }
  };
}
