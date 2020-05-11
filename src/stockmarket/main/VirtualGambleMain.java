package stockmarket.main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import stockmarket.controller.VirtualGambleControllerImpl;
import stockmarket.view.VirtualGambleViewImpl;
import stockmarket.model.VirtualGamble;
import stockmarket.model.VirtualGambleImpl;
import stockmarket.view.VirtualGambleView;

/**
 * This class contains the main method which is used to start up the application.
 */
public class VirtualGambleMain {

  /**
   * The main method initializes the MVC objects and passes the model and view to controller , and
   * then calls the method of controller which starts up the application.
   */
  public static void main(String[] args) throws IOException, ParseException, InterruptedException {
    VirtualGamble model = new VirtualGambleImpl();
    VirtualGambleView view = new VirtualGambleViewImpl();
    VirtualGambleControllerImpl controller = new VirtualGambleControllerImpl(
            new InputStreamReader(System.in), System.out);
    controller.startUp(model, view);
  }
}
