package stockmarket.controller;

import java.io.IOException;
import java.text.ParseException;

import stockmarket.model.VirtualGamble;
import stockmarket.view.VirtualGambleView;

/**
 * This interface represents a controller for the Virtual Gamble Application.The Controller is
 * responsible for controlling the application logic and acts as the coordinator between the View
 * and the Model.
 */
public interface VirtualGambleController {
  /**
   * This function is used to start up the Virtual Gamble Application.
   *
   * @param model The model class object of the Virtual Gamble Application.
   * @param view  The View class object of the Virtual Gamble Application.
   * @throws IOException An exception is thrown if objects of Model or View is equivalent to null.
   */
  public void startUp(VirtualGamble model, VirtualGambleView view) throws IOException,
          ParseException;
}
