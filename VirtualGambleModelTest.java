import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import stockmarket.model.Portfolio;
import stockmarket.model.Stock;
import stockmarket.model.VirtualGamble;
import stockmarket.model.VirtualGambleImpl;

/**
 * A JUnit test class for VirtualGamble interface.
 */
public class VirtualGambleModelTest {
  VirtualGamble virtualGamble;

  /**
   * This method runs before any test to create a VirtualGamble object.
   */
  @Before
  public void setUp() {
    virtualGamble = new VirtualGambleImpl();
  }

  /*---------------------Test createPortfolio----------------------*/

  /**
   * Test to verify that IllegalArgumentException is thrown when trying to add a new Portfolio with
   * ID that already exists.
   */
  @Test(expected = IllegalArgumentException.class)
  public void duplicatePortfolioCreationTest() {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.createPortfolio("retirement");
  }

  /* Test to verify that an empty portfolio with  a valid portfolio ID can be created.
   */
  @Test
  public void validPortfolioCreationTest() {
    virtualGamble.createPortfolio("retirement");
    Map<String, Portfolio> stocks = virtualGamble.getStockDetails();
    assertEquals(true, stocks.containsKey("retirement"));
  }



  /*-------------------------Test buyShare----------------------------*/

  /**
   * Test to verify a single stock into can be bought into a portfolio.
   */
  @Test
  public void buyShareValidSingleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    Map<String, Portfolio> stocks = virtualGamble.getStockDetails();
    Portfolio abcd = stocks.get("retirement");
    List<Stock> abc = abcd.getStockList();
    int count = 0;
    for (Stock obj : abc) {
      if (obj.getPurchaseDate().equals("2016-02-29") && obj.getCompanyTicker().equals("GOOG")) {
        count++;

      }
      assertEquals(1, count);

    }


  }

  /**
   * Test to verify a multiple stocks of different companies into can be bought into a portfolio.
   */
  @Test
  public void buyShareValidMultipleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-05-06",
            10);
    virtualGamble.buyShare("retirement", "GOOO", 20, "2009-02-12",
            10);
    assertEquals(5550.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify that correct total cost basis can be obtained after adding multiple stocks of
   * same company a portfolio.
   */
  @Test
  public void getBuyShareValidMultipleStockSameCompanyTest() throws
          IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "GOOG", 1500, "2014-05-06",
            10);
    assertEquals(5520.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify NoSuchElementException is thrown if trying to add a stock to portfolio that does
   * not exist.
   */
  @Test(expected = NoSuchElementException.class)
  public void buyShareWithPortfolioNotExist() throws IOException {
    virtualGamble.buyShare("invalidPortfolio", "GOOG", 1500,
            "2016-02-29", 10);
  }

  /**
   * Test to verify that IllegalArgumentException is thrown if trying to buyShare with invalid date
   * format.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buyShareInvalidDateFormat() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014/05/06",
            10);
  }

  /**
   * Test to verify that IllegalArgumentException is thrown if trying to buyShare with invalid date
   * like Feb 29 of 2014 which is non leap year.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buyShareInvalidDate() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-02-29",
            10);
  }

  /**
   * Test to verify that IllegalArgumentException is thrown on trying to buy share on a Holiday.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buyShareOnHoliday() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2017-06-17",
            10);
  }

  /**
   * Test to verify that IllegalArgumentException is thrown when trying to buy share with negative
   * amount.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buyShareNegativeAmount() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", -3000, "2016-02-29",
            10);
  }

  /*---------------------Test getTotalCostBasis-------------------------*/

  /**
   * Test to verify that correct total cost basis can be obtained after adding a single stock into a
   * portfolio and includes commission.
   */
  @Test
  public void getTotalCostBasisValidSingleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    assertEquals(4010, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify that correct total cost basis can be obtained after adding multiple stocks of
   * different company into a portfolio and this cost  basis includes commission amount for each
   * transaction.
   */
  @Test
  public void getTotalCostBasisValidMultipleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-05-06",
            10);
    virtualGamble.buyShare("retirement", "GOOO", 20, "2009-02-12",
            10);
    assertEquals(5550.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify that correct total cost basis can be obtained after adding multiple stocks of
   * same company a portfolio and this amount includes the cost basis.
   */
  @Test
  public void getTotalCostBasisValidMultipleStockSameCompanyTest() throws
          IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "GOOG", 1500, "2014-05-06",
            10);
    assertEquals(5520.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify that NoSuchElementException is thrown on trying to get Total Cost basis of a
   * Portfolio which does not exist.
   */
  @Test(expected = NoSuchElementException.class)
  public void getTotalCostBasisInvalidPortfolioID() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.getTotalCostBasis("invalidID");
  }

  /**
   * Test to verify total cost basis is zero if the user has not bought any shares yet in a given
   * portfolio.
   */
  @Test
  public void getTotalCostBasisEmptyPortfolioTest() {
    virtualGamble.createPortfolio("retirement");
    assertEquals(0.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }


  /**
   * Test to verify IllegalArgumentException is thrown if available stocks amounts less than stocks
   * being purchased.
   */
  @Test(expected = IllegalArgumentException.class)
  public void sharesLessThanRequiredTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", Math.pow(1000, 100),
            "2016-02-29", 10);
  }

  /**
   * Test to verify that total cost basis can be obtained for specified date and it includes the
   * commission amount.
   */
  @Test
  public void getTotalCostBasisSpecificDate() throws ParseException,
          IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-05-06",
            10);
    virtualGamble.buyShare("retirement", "GOOO", 20, "2009-02-12",
            10);
    assertEquals(5550.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
    assertEquals(1540.0, virtualGamble.getTotalCostBasis("retirement",
            "2014-08-07"), 0.01);
    assertEquals(30.0, virtualGamble.getTotalCostBasis("retirement",
            "2010-08-07"), 0.01);
    assertEquals(0.0, virtualGamble.getTotalCostBasis("retirement",
            "2008-08-07"), 0.01);
  }


  /*---------------------Test getTotalValue----------------------------*/

  /**
   * Test to verify that correct total value of stocks can be obtained after adding a single stock
   * into a portfolio for a given date.
   */
  @Test
  public void getTotalValueValidSingleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    assertEquals(6091.39, virtualGamble.getTotalValue("retirement",
            "2018-11-01"), 0.01);
  }

  /**
   * Test to verify that correct total value of stocks for a given date can be obtained after adding
   * multiple stocks into a portfolio.
   */
  @Test
  public void getTotalValueValidMultipleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-05-06",
            10);
    virtualGamble.buyShare("retirement", "GOOO", 56, "2009-02-12",
            10);
    assertEquals(10323.24, virtualGamble.getTotalValue("retirement",
            "2018-10-29"), 0.01);
  }

  /**
   * Test to verify that correct total cost basis can be obtained after adding multiple stocks of
   * same company a portfolio.
   */
  @Test
  public void getTotalValueValidMultipleStockSameCompanyTest() throws
          IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "MSFT", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1500, "2014-05-06",
            10);
    assertEquals(12885.15, virtualGamble.getTotalValue("retirement"), 0.01);
  }

  /**
   * Test to verify that correct total value of stocks for a today's date can be obtained after
   * adding multiple stocks into a portfolio when method is called without date parameter.
   */
  @Test
  public void getTotalValueTodayValidMultipleStockTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "MSFT", 1200, "2009-02-12",
            10);
    virtualGamble.buyShare("retirement", "MSFT", 1200, "2009-02-12",
            10);
    assertEquals(14199.37, virtualGamble.getTotalValue("retirement"), 0.01);
  }

  /**
   * Test to verify that NoSuchElementException is thrown on trying to buy share into a Portfolio
   * which does not exist.
   */
  @Test(expected = NoSuchElementException.class)
  public void getTotalValueInvalidPortfolioID() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.getTotalValue("invalidID", "2011-04-20");
  }

  /**
   * Test to verify total value is zero if the user has not bought any shares yet in a given
   * portfolio.
   */
  @Test
  public void getTotalValueEmptyPortfolioTest() throws InterruptedException {
    virtualGamble.createPortfolio("retirement");
    assertEquals(0.0, virtualGamble.getTotalValue("retirement", "2011-04-20"),
            0.01);
  }

  /**
   * Test to verify that a company can be added to a portfolio without buying a share.
   */
  @Test
  public void addCompanyTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    assertEquals(true, virtualGamble.getStockDetails().get("retirement").getCompanyList().
            contains("msft"));
  }

  /**
   * Test to verify that Illegal argument exception is thrown when adding invalid company to a
   * portfolio without buying a share.
   */
  @Test(expected = IllegalArgumentException.class)
  public void addInvalidCompanyTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "maaa");
  }

  /*--------------------------New functionality test------------------------*/

  /**
   * Test to invest amount equally to each company.
   */
  @Test
  public void investAmountEquallyTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "gooo");
    virtualGamble.investFixedAmountEqually("retirement", 60, "2014-08-28",
            10);
    assertEquals("[Company Ticker:goog\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 0.03526714865103156\n" +
            "Cost Basis:30.0, Company Ticker:msft\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 0.4483299708585519\n" +
            "Cost Basis:30.0, Company Ticker:gooo\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 190.47619047619048\n" +
            "Cost Basis:30.0]", virtualGamble.getStockDetails().get("retirement")
            .getStockList().toString());
  }

  /**
   * Test to invest with weights to each company.
   */
  @Test
  public void investAmountWeightedTest() throws IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "gooo");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("gooo", 10.0);
    virtualGamble.investFixedAmountWeighted("retirement", 600,
            "2014-08-28", map, 5);
    assertEquals("[Company Ticker:goog\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 0.7406101216716628\n" +
            "Cost Basis:425.0, Company Ticker:msft\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 2.6899798251513114\n" +
            "Cost Basis:125.0, Company Ticker:gooo\n" +
            "Purchase Date:2014-08-28\n" +
            "Number of Shares: 571.4285714285714\n" +
            "Cost Basis:65.0]", virtualGamble.getStockDetails().get("retirement")
            .getStockList().toString());
  }

  /**
   * Test to get costbasis for dollar average investment.
   */
  @Test
  public void getCostBasisForDollarAverage() throws ParseException,
          IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2014-07-12", 400, 30, map, 5);
    assertEquals(1245.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }


  /**
   * Test to get total value for dollar average investment.
   */
  @Test
  public void getValueForDollarAverage() throws ParseException, IOException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2014-07-12", 400, 30, map, 5);
    assertEquals(2006.25, virtualGamble.getTotalValue("retirement", "2017-07-26"), 0.01);
  }

  /**
   * Test to verify that portfolio data can be persisted into a file.
   */
  @Test
  public void testPortfolioDataSave() throws IOException, ParseException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.buyShare("retirement", "GOOG", 4000, "2016-02-29",
            10);
    virtualGamble.buyShare("retirement", "AAPL", 2000, "2016-02-29",
            10);
    virtualGamble.save("retirement");
    File f = new File("portfolio/retirement.csv");
    if (!f.exists()) {
      fail();
    }
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get("portfolio/retirement.csv")));
    } catch (IOException e) {
      fail();
    }
    assertEquals("PurchaseDate,CompanyTicker,CostBasis,NumberOfShares,Commission\n" +
            "2016-02-29,GOOG,4010.0,5.733287467033597,10.0\n" +
            "2016-02-29,AAPL,2010.0,20.693222969477496,10.0\n", data);
  }


  /**
   * Test to verify that portfolio data can be retrieved from a file.
   */
  @Test
  public void testPortfolioDataRetrieve() throws IOException, ParseException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2014-05-12", 400, 30, map, 5);
    virtualGamble.save("retirement");
    virtualGamble = new VirtualGambleImpl();
    virtualGamble.retrieve("retirement");
    assertEquals(415.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);

  }

  /**
   * Test to verify that IllegalArgument Exception is thrown when trying to retrieve a portfolio
   * data that does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void portfolioDataRetrieveInvalid() throws IOException,
          ParseException {
    virtualGamble = new VirtualGambleImpl();
    virtualGamble.retrieve("retiremen");

  }

  /**
   * Test to verify that Strategy data can be persisted into a file.
   */
  @Test
  public void testStrategyDataSave() throws IOException, ParseException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2014-05-12", 400, 30, map, 5);
    virtualGamble.saveStrategy("retirement", "str1");
    File f = new File("strategy/str1.csv");
    if (!f.exists()) {
      fail();
    }
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get("strategy/str1.csv")));
    } catch (IOException e) {
      fail();
    }
    assertEquals("StartDate,EndDate,PeriodInDays,amount,weights,commission\n" +
            "2014-04-21,2014-05-12,30,400.0,{goog=70.0; aapl=10.0; msft=20.0},5.0", data);
  }

  /**
   * Test to verify that strategy data can be retrieved from a file.
   */
  @Test
  public void testStrategyDataRetrieve() throws IOException, ParseException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2014-05-12", 400, 30, map, 5);
    virtualGamble.saveStrategy("retirement", "str1");
    virtualGamble = new VirtualGambleImpl();
    virtualGamble.createPortfolio("retirement");
    virtualGamble.retrieveStrategy("str1", "retirement");
    assertEquals(415.0, virtualGamble.getTotalCostBasis("retirement"), 0.01);
  }

  /**
   * Test to verify that we can obtain values to plot the graph.
   */
  /*@Test
  public void testGetValuesForMap() throws IOException, ParseException {
    virtualGamble.createPortfolio("retirement");
    virtualGamble.addStockPortfolio("retirement", "msft");
    virtualGamble.addStockPortfolio("retirement", "goog");
    virtualGamble.addStockPortfolio("retirement", "aapl");
    Map map = new HashMap();
    map.put("msft", 20.0);
    map.put("goog", 70.0);
    map.put("aapl", 10.0);
    virtualGamble.applyDollarCostAveraging("retirement", "2014-04-21",
            "2015-05-12", 400, 30, map, 5);
    Map<String, Double> valueMap = virtualGamble.getValuesForGraph("retirement");
    for (Map.Entry<String, Double> entry : valueMap.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
  }*/


}
