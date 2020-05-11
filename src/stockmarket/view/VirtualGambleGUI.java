package stockmarket.view;

import stockmarket.controller.Features;

/**
 * This interface represents the Graphical User Interface for the Virtual Gamble Application. The
 * GUI is responsible for showing the system response to the user and take inputs from the user.
 */
public interface VirtualGambleGUI {

  /**
   * Method to show message to the user.
   *
   * @param message message to be shown to user
   */
  void showMessage(String message);

  /**
   * Method to set features provided by this GUI.
   *
   * @param features object which represent supporting features
   */
  void setFeatures(Features features);

  /**
   * Method to make the view visible.
   *
   * @param b true to show and false to hide view
   */
  void setVisible(boolean b);

  /**
   * Method to open a new form to take user inputs.
   *
   * @param options the inputs to be taken by user
   * @param choice  this is the action choice corresponding to which user needs to enter inputs
   */
  void openForm(String[] options, String choice);

  /**
   * Method to show the graphical performance of a portfolio.
   */
  void showGraph();
}
