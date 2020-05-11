package stockmarket.main;

import stockmarket.controller.VirtualGambleControllerGUI;
import stockmarket.model.VirtualGamble;
import stockmarket.model.VirtualGambleImpl;
import stockmarket.view.VirtualGambleGUIImpl;

/**
 * This class contains the main method which is used to start up the application in GUI mode.
 */
public class VirtualGambleGUIMain {
  /**
   * The main method initializes the MVC objects and passes the model and view to controller , and
   * then calls the method of controller which starts up the application.
   */
  public static void main(String[] ar) {
    VirtualGamble model = new VirtualGambleImpl();
    VirtualGambleGUIImpl view = new VirtualGambleGUIImpl("Virtual Gamble");
    VirtualGambleControllerGUI controller = new VirtualGambleControllerGUI(model, view);
    view.setFeatures(controller);
    controller.startUp();
  }
}
