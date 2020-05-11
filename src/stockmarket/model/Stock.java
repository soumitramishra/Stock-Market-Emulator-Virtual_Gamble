package stockmarket.model;

import java.text.ParseException;

/**
 * This interface represents a stock which is simply a part of ownership in that company.
 */
public interface Stock {

  /**
   * Method to get the commission amount of this stock.
   *
   * @return the commission amount of this stock
   */
  double getCommission();

  /**
   * Method to get the cost basis of this stock.
   *
   * @return the cost basis of this stock
   */
  double getCostBasis();

  /**
   * Method to get cost basis for given date.
   *
   * @param date date to obtain cost basis for
   * @return cost basis for given date
   */
  double getCostBasis(String date) throws ParseException;

  /**
   * Method to get the number of shares of this stock.
   *
   * @return the number of shares of this stock
   */
  double getNumberOfShares();

  /**
   * Method to get the value of this stock on particular date.
   *
   * @param date date in yyyy-MM-dd for which value is required
   * @return the value of this stock on given date
   */
  double getValueOnDate(String date);

  /**
   * Method to get the date on which this stock was purchased.
   *
   * @return the date on whic this stock was purchased
   */
  String getPurchaseDate();

  /**
   * Method to get the ticker symbol of the company of which the stock belongs.
   *
   * @return the ticker symbol of the company of which the stock belongs
   */
  String getCompanyTicker();


}
