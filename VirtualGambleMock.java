import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import stockmarket.model.Portfolio;
import stockmarket.model.PortfolioImpl;
import stockmarket.model.VirtualGamble;


/**
 * This is a mock model that implements VirtualGamble interface and maintains a log in order to help
 * testing the VirtualGamble Controller in isolation.
 */
public class VirtualGambleMock implements VirtualGamble {

  private StringBuilder log;

  /**
   * Constructor to initialize the log.
   *
   * @param log log to be appended according to the called operations
   */
  public VirtualGambleMock(StringBuilder log) {
    this.log = log;

  }

  @Override
  public void createPortfolio(String portfolioID) throws IllegalArgumentException {
    //Throwing exception to verify Exception handling capability of controller.
    if (portfolioID.equals("invalid")) {
      throw new IllegalArgumentException("MockIllegalArgumentException");
    }
    log.append("Created Portfolio\n");
  }

  @Override
  public void buyShare(String portfolioID, String company, double amount, String date,
                       double commission)
          throws NoSuchElementException, IllegalArgumentException {
    //Throwing exception to verify Exception handling capability of controller.
    if (portfolioID.equals("illegal")) {
      throw new IllegalArgumentException("MockIllegalArgumentException");
    }
    if (portfolioID.equals("invalid")) {
      throw new NoSuchElementException("MockNoSuchElementException");
    }
    log.append("Bought a share\n");
  }

  @Override
  public double getTotalCostBasis(String portfolioID) throws IllegalArgumentException {
    //Throwing exception to verify Exception handling capability of controller.
    if (portfolioID.equals("invalid")) {
      throw new IllegalArgumentException("MockIllegalArgumentException");
    }
    return 100;
  }

  @Override
  public double getTotalCostBasis(String portfolioID, String date) throws ParseException {
    return 0;
  }

  @Override
  public double getTotalValue(String portfolioID, String date) throws
          NoSuchElementException, IllegalArgumentException {
    //Throwing exception to verify Exception handling capability of controller.
    if (portfolioID.equals("illegal")) {
      throw new IllegalArgumentException("MockIllegalArgumentException");
    }
    if (portfolioID.equals("invalid")) {
      throw new NoSuchElementException("MockNoSuchElementException");
    }
    return 200;
  }

  @Override
  public double getTotalValue(String portfolioID) throws NoSuchElementException,
          IllegalArgumentException {
    //Throwing exception to verify Exception handling capability of controller.
    if (portfolioID.equals("invalid")) {
      throw new IllegalArgumentException("MockIllegalArgumentException");
    }
    return 300;
  }

  @Override
  public Map<String, Portfolio> getStockDetails() {
    Map mock = new HashMap<String, PortfolioImpl>();
    PortfolioImpl mockPortfolio = new PortfolioImpl();
    mock.put("PortfolioMock", mockPortfolio);
    return mock;
  }

  @Override
  public void addStockPortfolio(String portfolioID, String company) throws
          NoSuchElementException {
    log.append("Added company " + company + " Portfolio ID" + company);
  }

  @Override
  public void investFixedAmountEqually(String portfolioID, double amount, String date,
                                       double commission) {
    log.append("Invested amount " + amount + " on " + date + " commission " + commission + "Pid"
            + portfolioID);
  }

  @Override
  public void investFixedAmountWeighted(String portfolioID, double amount, String date,
                                        Map<String, Double> weights, double commission) {
    log.append("Invested amount " + amount + " on " + date + " commission " + commission + "Pid"
            + portfolioID);
  }

  @Override
  public void applyDollarCostAveraging(String portfolioID, String startDate, String endDate,
                                       double amount, int period, Map<String, Double> weights,
                                       double commission) throws ParseException, IOException {
    log.append("Dollar cost amount " + amount + " on " + startDate + "End " + endDate
            + " commission " + commission + "Pid" + portfolioID);
  }


  @Override
  public void save(String portfolioID) {
    log.append("Created Portfolio " + portfolioID);
  }

  @Override
  public void retrieve(String portfolioID) {
    log.append("Created Portfolio " + portfolioID);
  }

  @Override
  public void saveStrategy(String portfolioID, String strategyName) {
    log.append("Saved Strategy " + strategyName + " Portfolio " + portfolioID);
  }

  @Override
  public void retrieveStrategy(String strategyName, String portfolioID) {
    log.append("Retrieved Strategy " + strategyName + " Portfolio " + portfolioID);
  }

  @Override
  public void getValuesForGraph(String portfolioID) throws ParseException {
    log.append("updated values for graph for Portfolio " + portfolioID);
  }
}
