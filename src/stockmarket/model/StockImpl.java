package stockmarket.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is an implementation of Stock interface and defines all the methods mandated by this
 * interface.
 */
public class StockImpl implements Stock {
  private final String purchaseDate;
  private final String companyTicker;
  private final double costBasis;
  private final double numberOfShares;
  private final double commission;


  @Override
  public double getCommission() {
    return commission;
  }

  /**
   * Construct a stock with given CompanyTicker, amount and purchase date.
   *
   * @param companyTicker ticker symbol of the company of which stock is to be constructed
   * @param amount        amount of the stock in dollars
   * @param purchaseDate  purchase date of the stock
   */
  public StockImpl(String companyTicker, double amount, String purchaseDate, double commission) {

    String fullDataForCompany = getStockData(companyTicker);
    if (!fullDataForCompany.contains(purchaseDate)) {
      throw new IllegalArgumentException("Stock is not available for date " + purchaseDate
              + " for company " + companyTicker);
    }
    String[] dataRequired = fullDataForCompany.split(purchaseDate)[1].split(
            "\\n")[0].split(",");
    int i = 0;
    double lowestPrice = Double.parseDouble(dataRequired[3]);
    Long volume = Long.parseLong(dataRequired[5].trim());
    this.costBasis = amount + commission;
    this.numberOfShares = amount / lowestPrice;
    if (numberOfShares >= volume) {
      throw new IllegalArgumentException("The number of shares is less than required");
    }
    this.companyTicker = companyTicker;
    this.purchaseDate = purchaseDate;
    this.commission = commission;
  }

  @Override
  public double getCostBasis() {
    return costBasis;
  }

  @Override
  public double getCostBasis(String date) throws ParseException {
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date requiredDate = sdf.parse(date);
    Date purchaseDate = sdf.parse(this.purchaseDate);
    if (purchaseDate.equals(requiredDate) || purchaseDate.before(requiredDate)) {
      return this.costBasis;
    } else {
      return 0;
    }
  }

  /**
   * private helper method to get the stock data for given company.
   *
   * @param stockSymbol ticker symbol of the company for which stock data is to be obtained
   * @return the complete stock data available till date
   */
  private static String getStockData(String stockSymbol) {
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get("data/" + stockSymbol.toLowerCase()
              + ".csv")));
    } catch (IOException e) {
      throw new IllegalArgumentException("Stock data not available for company");
    }
    return data;
  }

  @Override
  public double getNumberOfShares() {
    return numberOfShares;
  }

  @Override
  public double getValueOnDate(String date) {
    String fullDataForCompany = getStockData(companyTicker);
    if (!fullDataForCompany.contains(date)) {
      throw new IllegalArgumentException("Data for given date" + date
              + " is not available for company " + companyTicker);
    }
    String[] dataRequired = fullDataForCompany.split(date)[1].split(
            "\\n")[0].split(",");
    return numberOfShares * Double.parseDouble(dataRequired[3]);
  }

  @Override
  public String toString() {
    String stockState = "Company Ticker:" + this.companyTicker
            + "\nPurchase Date:" + this.purchaseDate
            + "\nNumber of Shares: " + this.numberOfShares + "\nCost Basis:" + this.costBasis;
    return stockState;
  }

  @Override
  public String getPurchaseDate() {
    return purchaseDate;
  }

  @Override
  public String getCompanyTicker() {
    return companyTicker;
  }


}
