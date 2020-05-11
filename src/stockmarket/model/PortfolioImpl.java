package stockmarket.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is an implementation of Portfolio interface and defines all the methods mandated by
 * this interface.
 */
public class PortfolioImpl implements Portfolio {
  private final List<Stock> stocks;
  private Set<String> companies;
  private boolean dollarCostAveraged;

  @Override
  public DollarCostAverage getDollarCostAverage() {
    return dollarCostAverage;
  }

  @Override
  public boolean getDollarCostAveraged() {
    return dollarCostAveraged;
  }

  private DollarCostAverage dollarCostAverage;

  /**
   * Constructor to initialize stocks to an empty ArrayList.
   */
  public PortfolioImpl() {
    this.stocks = new ArrayList<>();
    this.companies = new HashSet<>();
    this.dollarCostAveraged = false;
  }

  @Override
  public void addStock(String company, double amount, String date, double commission)
          throws IOException {
    this.stocks.add(new StockImpl(company, amount, date, commission));
  }

  @Override
  public double getTotalCostBasis() {
    double totalCost = 0;
    for (Stock stock : stocks) {
      totalCost += stock.getCostBasis();
    }
    return totalCost;
  }

  @Override
  public double getTotalCostBasis(String date) throws ParseException {
    double totalCost = 0;
    for (Stock stock : stocks) {
      totalCost += stock.getCostBasis(date);
    }
    return totalCost;
  }

  @Override
  public double getTotalValue(String date) {
    double totalValue = 0;
    for (Stock stock : stocks) {
      totalValue += stock.getValueOnDate(date);
    }
    return totalValue;
  }

  @Override
  public List<Stock> getStockList() {
    List<Stock> newStockImpl = new ArrayList<>(stocks.size());
    newStockImpl.addAll(stocks);
    return newStockImpl;
  }

  @Override
  public Set<String> getCompanyList() {

    Set<String> returnCompanyList = new HashSet<>();
    returnCompanyList.addAll(this.companies);
    return returnCompanyList;
  }

  @Override
  public void addStockData(String company) {
    if (!companies.add(company)) {
      throw new IllegalArgumentException("Company already exists in portfolio\n");
    }
  }

  @Override
  public void setDollarCostAveraged(boolean b) {
    this.dollarCostAveraged = b;
  }

  @Override
  public void setDollarCostAverage(DollarCostAverage data) {
    this.dollarCostAverage = data;
  }


  @Override
  public String toString() {
    String stockString = "";
    for (Stock stock : stocks) {
      stockString = stockString + stock;
    }
    return stocks.toString();
  }
}