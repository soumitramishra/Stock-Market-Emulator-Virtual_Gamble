package stockmarket.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This interface represents a virtual gamble which that helps users who are new to investing to
 * learn about how money could grow. It will allow the user to create investment portfolios, try out
 * buying and selling of stock, and various investment strategies to see how they can grow (or
 * shrink) their money with time.
 */
public interface VirtualGamble {

  /**
   * Method to create new Portfolio which is a combination of one or more shares of different
   * companies.
   *
   * @param portfolioID unique identity of a portfolio like retirement, collegeSavings etc
   * @throws IllegalArgumentException if portfolio with given ID already exists
   */
  void createPortfolio(String portfolioID) throws IllegalArgumentException;


  /**
   * Method to buy shares of some stock in a portfolio worth a certain amount at a certain date.It
   * is assumed that stock is purchased at lowest price of share on a particular day.
   *
   * @param portfolioID unique ID of portfolio in which share is to be added
   * @param company     ticker symbol of the company for which share is to be added
   * @param amount      amount of the share to be bought
   * @param date        date on which share is to be bought
   * @param commission  commission for this transaction
   * @throws NoSuchElementException   thrown when the given Portfolio ID does not exist
   * @throws IllegalArgumentException thrown when stock is not available at given date or amount is
   *                                  negative or format of date is invalid.
   */
  void buyShare(String portfolioID, String company, double amount, String date, double commission)
          throws NoSuchElementException, IllegalArgumentException,
          IOException;


  /**
   * Method to get the total cost basis of all the shares for a particular portfolio on a certain
   * date.
   *
   * @param portfolioID unique ID of portfolio for which total cost basis is to be calculated
   * @return the total cost basis of portfolio on the given date rounded to two decimal places
   * @throws IllegalArgumentException if the portfolioID does not exist or the number of available
   *                                  shares is less than required
   */
  double getTotalCostBasis(String portfolioID) throws IllegalArgumentException;

  double getTotalCostBasis(String portfolioID, String date) throws ParseException;

  /**
   * Method to get the total value of all the shares for a particular portfolio on a certain date.
   *
   * @param portfolioID unique ID of portfolio for which total value is to be calculated
   * @param date        date on which total value is to be calculated
   * @return the total value of portfolio on the given date rounded to two decimal places
   * @throws NoSuchElementException   thrown when the given Portfolio ID does not exist
   * @throws IllegalArgumentException thrown when the date is invalid or date is in invalid format
   *                                  or stock is not available at given date
   */
  double getTotalValue(String portfolioID, String date) throws NoSuchElementException,
          IllegalArgumentException;

  /**
   * Method to get the total value of all the shares for a particular portfolio on a today's date.
   *
   * @param portfolioID unique ID of portfolio for which total value is to be calculated
   * @return the total value of portfolio on the given date rounded to two decimal places
   * @throws NoSuchElementException   thrown when the given Portfolio ID does not exist
   * @throws IllegalArgumentException thrown when the date is invalid or date is in invalid format
   *                                  or stock is not available at given date
   */
  double getTotalValue(String portfolioID) throws NoSuchElementException, IllegalArgumentException;

  /**
   * Get list of all stocks of a particular Portfolio ID.
   *
   * @return te list of all stocks of a particular Portfolio ID
   */
  Map<String, Portfolio> getStockDetails();

  /**
   * Method to add company to portfolio without buying share.
   *
   * @param portfolioID unique ID of portfolio
   * @param company     ticker symbol of the company
   * @throws NoSuchElementException when portfolio ID does not exist
   */
  void addStockPortfolio(String portfolioID, String company)
          throws NoSuchElementException, IOException;

  /**
   * Method to invest fixed amount equally between  all companies.
   *
   * @param portfolioID unique id of portfolio
   * @param amount      amount to be invested
   * @param date        date of investment
   * @param commission  commission of investment
   * @throws IOException when file read/write fails
   */
  void investFixedAmountEqually(String portfolioID, double amount, String date, double commission)
          throws IOException;

  /**
   * Method to invest fixed amount divided into companies based on given weights.
   *
   * @param portfolioID unique ID of portfolio
   * @param amount      amount to be invested
   * @param date        date of investment
   * @param weights     weight for investments
   * @param commission  commission for this investment
   * @throws IOException when file read/write fails
   */
  void investFixedAmountWeighted(String portfolioID, double amount, String date, Map<String,
          Double> weights, double commission) throws IOException;

  /**
   * Method to apply DollarCostAveraging Technique to portfolio.
   *
   * @param portfolioID unique ID of portfolio
   * @param startDate   date to start investment
   * @param endDate     date to end investment
   * @param amount      amount to be invested for each cycle
   * @param period      period to repeat investment
   * @param weights     weights for investment
   * @param commission  commission for this investment
   */
  void applyDollarCostAveraging(String portfolioID, String startDate, String endDate,
                                double amount, int period,
                                Map<String, Double> weights, double commission) throws
          ParseException, IOException;


  /**
   * Method to save the state of a portfolio.
   *
   * @param portfolioID unique ID of portfolio whose state is to be saved
   * @throws IllegalArgumentException if the portfolioID does not exist
   * @throws IOException              if the File read or write operation fails
   */
  void save(String portfolioID) throws IllegalArgumentException, IOException;

  /**
   * Method to retrieve a previously saved portfolio state.
   *
   * @param portfolioID unique ID of portfolio to be retrieved
   * @throws IllegalArgumentException if the portfolioID already exists
   */
  void retrieve(String portfolioID) throws IllegalArgumentException;

  /**
   * Method to save the parameters of a Dollar cost strategy.
   *
   * @param portfolioID  unique ID on which the dollar cost strategy to be saved is applied
   * @param strategyName name with which the strategy is to be saved
   * @throws IOException              if file read or write operation fails
   * @throws IllegalArgumentException if the portfolio does not exist
   */
  void saveStrategy(String portfolioID, String strategyName) throws IOException,
          IllegalArgumentException;


  /**
   * Method to retrieve a saved Dollar cost strategy and apply it to a given portfolio.
   *
   * @param strategyName name of the strategy to be retrieved
   * @param portfolioID  unique ID of portfolio on which this strategy is to be applied
   * @throws IllegalArgumentException thrown if portfolio does not exist
   */
  void retrieveStrategy(String strategyName, String portfolioID) throws IllegalArgumentException;

  /**
   * Method to update the values to be used for plotting the graph with the latest values of
   * portfolio.
   *
   * @param portfolioiID unique ID of the portfolio to update the file data
   * @throws ParseException if the date parsing fails
   * @throws IOException    if file read write operation
   */
  void getValuesForGraph(String portfolioiID) throws ParseException, IOException;
}
