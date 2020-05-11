import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Map;

import stockmarket.controller.VirtualGambleController;
import stockmarket.controller.VirtualGambleControllerImpl;
import stockmarket.model.VirtualGamble;
import stockmarket.view.VirtualGambleView;
import stockmarket.view.VirtualGambleViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class VirtualGambleControllerTest {
  private StringBuilder logs;
  private VirtualGamble virtualGambleMock;
  private VirtualGambleController controller;
  private VirtualGambleView view = new VirtualGambleViewImpl();
  Readable rd;
  Appendable ap;

  @Before
  public void setUp() {
    logs = new StringBuilder();
    virtualGambleMock = new VirtualGambleMock(logs);
    rd = new StringReader("q");
    ap = new StringBuffer();
    controller = new VirtualGambleControllerImpl(rd, ap);
  }


  /**
   * Test to verify that IllegalArgumentException is thrown  if readable parameter is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void readableNullTest() {
    controller = new VirtualGambleControllerImpl(null, ap);
  }

  /**
   * Test to verify that IllegalArgumentException is thrown if appendable parameter is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void appendableNullTest() {
    controller = new VirtualGambleControllerImpl(rd, null);
  }

  /**
   * Test to verify that controller can handle IllegalArgumentException while creating a portfolio.
   */
  @Test
  public void createPortfolioIllegalArgumentExceptionTest() throws IOException, ParseException {
    rd = new StringReader("1 invalid q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    if (!ap.toString().contains("MockIllegalArgumentException")) {
      fail();
    }
  }

  /**
   * Test to verify that Portfolio can be created successfully.
   */
  @Test
  public void createPortfolioValidTest() throws IOException, ParseException {
    rd = new StringReader("1 hello q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    assertEquals("Created Portfolio\n", logs.toString());
  }

  /**
   * Test to verify that controller can handle NoSuchElementException while buying shares.
   */
  @Test
  public void buyShareNoSuchElementExceptionTest() throws IOException, ParseException {
    rd = new StringReader("2 invalid msft 2000 2018-09-09 q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    if (!ap.toString().contains("MockNoSuchElementException")) {
      fail();
    }
  }

  /**
   * Test to verify that controller can handle IllegalArgumentException while buying shares.
   */
  @Test
  public void buyShareIllegalArgumentExceptionTest() throws IOException, ParseException {
    rd = new StringReader("2 illegal msft 2000 2018-09-09 q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    if (!ap.toString().contains("MockIllegalArgumentException")) {
      fail();
    }
  }

  /**
   * Test to verify that shares can be bought successfully into a portfolio.
   */
  @Test
  public void buyShareTest() throws IOException, ParseException {
    rd = new StringReader("1 hello 2 hello msft 2000 2018-11-13 q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    assertEquals("Created Portfolio\n" +
            "Bought a share\n", logs.toString());
  }

  /**
   * Test to verify that controller can handle IllegalArgumentException while
   * gettingTotalCostBasis.
   */
  @Test
  public void getTotalCostBasisIllegalArgumentExceptionTest() throws IOException, ParseException {
    rd = new StringReader("3 invalid 2018-09-09 q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    if (!ap.toString().contains("MockIllegalArgumentException")) {
      fail();
    }
  }

  @Test
  public void getTotalCostBasisTest() throws IOException, ParseException {
    rd = new StringReader("3 hello q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    assertEquals("Enter your choice from the menu:\n" +
            "1) Create a new Portfolio\n" +
            "2) Buy share\n" +
            "3) Get Total Cost Basis of a portfolio\n" +
            "4) Get the total value of a portfolio at a certain date\n" +
            "5) Get a list of all portfolios\n" +
            "Enter q/Q to quit this application at any point of time.\n" +
            "\n" +
            "Enter portfolio name\n" +
            "Cost basis of hello is100.0\n" +
            "\n" +
            "Enter your choice from the menu:\n" +
            "1) Create a new Portfolio\n" +
            "2) Buy share\n" +
            "3) Get Total Cost Basis of a portfolio\n" +
            "4) Get the total value of a portfolio at a certain date\n" +
            "5) Get a list of all portfolios\n" +
            "Enter q/Q to quit this application at any point of time.\n" +
            "\n", ap.toString());
  }

  /**
   * Test to verify that controller can handle NoSuchElementException while getting total value.
   */
  @Test
  public void getTotalValueIllegalArgumentExceptionTest() throws IOException, ParseException {
    rd = new StringReader("4 invalid 2018-09-09 q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    if (!ap.toString().contains("MockNoSuchElementException")) {
      fail();
    }
  }

  /**
   * Test to verify the  total value obtained by controller is same as that returned by model.
   */
  @Test
  public void getTotalValueTest() throws IOException, ParseException {
    rd = new StringReader("3 hello q");
    controller = new VirtualGambleControllerImpl(rd, ap);
    controller.startUp(virtualGambleMock, view);
    assertEquals("Enter your choice from the menu:\n" +
            "1) Create a new Portfolio\n" +
            "2) Buy share\n" +
            "3) Get Total Cost Basis of a portfolio\n" +
            "4) Get the total value of a portfolio at a certain date\n" +
            "5) Get a list of all portfolios\n" +
            "Enter q/Q to quit this application at any point of time.\n" +
            "\n" +
            "Enter portfolio name\n" +
            "Cost basis of hello is100.0\n" +
            "\n" +
            "Enter your choice from the menu:\n" +
            "1) Create a new Portfolio\n" +
            "2) Buy share\n" +
            "3) Get Total Cost Basis of a portfolio\n" +
            "4) Get the total value of a portfolio at a certain date\n" +
            "5) Get a list of all portfolios\n" +
            "Enter q/Q to quit this application at any point of time.\n" +
            "\n", ap.toString());
  }

  /**
   * Test to verify that details of all the stocks can be obtained by controller.
   */
  @Test
  public void getStocksTest() {
    Map map = virtualGambleMock.getStockDetails();
    if (!map.containsKey("PortfolioMock")) {
      fail();
    }
  }

}
