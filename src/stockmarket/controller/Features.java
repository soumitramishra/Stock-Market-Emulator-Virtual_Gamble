package stockmarket.controller;

import java.util.Collection;
import java.util.Map;

/**
 * This class represents the features for the controller supporting GUI for the VirtualGamble
 * application.
 */
public interface Features {
  /**
   * Method to startUp the application.
   */
  void startUp();

  /**
   * Method to process the input from the user and call the corresponding method in the model.
   *
   * @param choice     Option selected by user
   * @param userInputs A map that represents user input
   */
  void processInput(String choice, Map<String, String> userInputs);

  /**
   * Method to get the data from the model to be displayed on the GUI.
   *
   * @param requiredData this is the name of required data to be fetched from model
   * @return a collection of data fetched from model based on the requiredData parameter
   */
  Collection getData(String requiredData);
}
