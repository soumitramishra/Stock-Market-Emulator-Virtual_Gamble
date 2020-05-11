package stockmarket.view;

import java.io.IOException;

/**
 * This class implements the VirtualGambleView interface and implements all the operations mandated
 * by the interface.
 */

public class VirtualGambleViewImpl implements VirtualGambleView {
  @Override
  /**
   * This is a method to display the system response to the user.
   *
   * @param buffer The message to be appended to the appendable object and displayed to the user.
   */
  public void print(Appendable ap, String buffer) {

    try {
      ap.append(buffer);
    } catch (IOException e) {
      throw new IllegalStateException("Controller is unable to successfully receive "
              + "input or transmit output\n");
    }
  }

}
