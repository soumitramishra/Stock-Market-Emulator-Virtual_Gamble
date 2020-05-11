package stockmarket.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;


/**
 * This interface represents a Portfolio which is a combination of a number of stocks from various
 * companies.
 */
public interface Portfolio {
  DollarCostAverage getDollarCostAverage();

  boolean getDollarCostAveraged();

  /**
   * Method to add a stock to this Portfolio.
   *
   * @param company    company ticker of the stock to be added
   * @param amount     amount of stock to be added in dollars
   * @param date       date of purchase of stock in yyyy-MM-dd format
   * @param commission commission for this investment to be added to cost basis
   */
  public void addStock(String company, double amount, String date, double commission)
          throws IOException;

  /**
   * Method to get the sum total cost basis of all the stocks in this Portfolio.
   *
   * @return sum total cost basis of all the stocks in this Portfolio
   */
  public double getTotalCostBasis();

  double getTotalCostBasis(String date) throws ParseException;

  /**
   * Method to get the total value of all the stocks in this Portfolio.
   *
   * @param date date in the format yyyy-MM-dd
   * @return sum total value of all the stocks in this Portfolio
   */
  public double getTotalValue(String date);

  /**
   * Method to get list of all the stocks in this Portfolio.
   *
   * @return list of all the stocks in this Portfolio
   */
  List<Stock> getStockList();

  /**
   * Method to get the list of all companies in this portfolio.
   *
   * @return all companies in this portfolio
   */
  Set<String> getCompanyList();

  /**
   * Method to add a company to this portfolio without any investment.
   *
   * @param company company to added to this portfolio
   */
  void addStockData(String company);

  /**
   * Method to set DollarCostAveraged to true when Dollar cost strategy is applied to this
   * portfolio.
   *
   * @param b false initially and true when dollar cost average strategy is applied to this
   *          portfolio
   */
  void setDollarCostAveraged(boolean b);

  /**
   * Method to set the dollar cost average parameters.
   *
   * @param data object that represents dollar cost average strategy paremeters
   */
  void setDollarCostAverage(DollarCostAverage data);
}
