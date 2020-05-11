package stockmarket.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * This is an implementation of VirtualGamble Interface that provides functionality of adding new
 * Portfolios, buying share and getting the total value and cost basis of particular portfolio. It
 * maintains a map-String Portfolio ID to Portfolio object to achieve this.
 */
public class VirtualGambleImpl implements VirtualGamble {

  private Map<String, Portfolio> portfolios;

  /**
   * Constructor to initialize the portfolios map to an empty Hashmap.
   */
  public VirtualGambleImpl() {
    portfolios = new HashMap<>();
  }

  @Override
  public void createPortfolio(String portfolioID) throws IllegalArgumentException {
    checkForDuplicatePortfolioID(portfolioID);
    portfolios.put(portfolioID, new PortfolioImpl());

  }

  /**
   * Method to check that the entered portfolio ID does not already exist.
   *
   * @param portfolioID portfolioID to be verified for duplicate
   * @throws IllegalArgumentException if the given portfolio  ID already exists
   */
  private void checkForDuplicatePortfolioID(String portfolioID) throws IllegalArgumentException {
    if (portfolios.containsKey(portfolioID)) {
      throw new IllegalArgumentException("The given portfolio already exist");
    }
  }


  @Override
  public void buyShare(String portfolioID, String company, double amount, String date,
                       double commission)
          throws NoSuchElementException, IOException {
    validatePortfolioID(portfolioID);
    if (!this.portfolios.get(portfolioID).getCompanyList().contains(company)) {
      addStockPortfolio(portfolioID, company);
    }
    update(company);
    validateDate(date);
    if (amount <= 0) {
      throw new IllegalArgumentException("amount cannot be negative");
    }
    portfolios.get(portfolioID).addStock(company, amount, date, commission);
  }

  /**
   * Helper method to validate that given date is valid and is in valid format or not.
   *
   * @param date date in String format to be validated
   * @throws IllegalArgumentException if date is invalid
   */
  private void validateDate(String date) throws IllegalArgumentException {
    String dateFormat = "\\d{4}-\\d{2}-\\d{2}";
    if (!date.matches(dateFormat)) {
      throw new IllegalArgumentException("Date should be in the format yyyy-MM-dd");
    }
    String[] dateArray = date.split("-");
    int year = Integer.parseInt(dateArray[0]);
    int month = Integer.parseInt(dateArray[1]);
    int day = Integer.parseInt(dateArray[2]);
    if (month == 2) { //February
      if (year % 4 == 0) { //Leap Year
        if (day > 29) {
          throw new IllegalArgumentException("Invalid date");
        }
      } else {
        if (day > 28) { //Non leap year
          throw new IllegalArgumentException("Invalid date");
        }
      }
    } else if (month == 4 || month == 6 || month == 9 || month == 11) { //Apr, Jun, Sep, Nov
      if (day > 30) {
        throw new IllegalArgumentException("Invalid date");
      }
    } else {
      if (day > 31) {
        throw new IllegalArgumentException("Invalid date");
      }
    }
  }


  @Override
  public double getTotalCostBasis(String portfolioID) {
    validatePortfolioID(portfolioID);
    Double number = this.portfolios.get(portfolioID).getTotalCostBasis();
    String numberAsString = String.format("%.2f", number);
    return Double.parseDouble(numberAsString);
  }

  @Override
  public double getTotalCostBasis(String portfolioID, String date) throws ParseException {
    validatePortfolioID(portfolioID);
    validateDate(date);
    Double number = this.portfolios.get(portfolioID).getTotalCostBasis(date);
    String numberAsString = String.format("%.2f", number);
    return Double.parseDouble(numberAsString);
  }

  @Override
  public double getTotalValue(String portfolioID, String date) {
    validatePortfolioID(portfolioID);
    validateDate(date);
    Double number = this.portfolios.get(portfolioID).getTotalValue(date.trim());
    String numberAsString = String.format("%.2f", number);

    return Double.parseDouble(numberAsString);
  }

  @Override
  public double getTotalValue(String portfolioID) throws NoSuchElementException,
          IllegalArgumentException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String todaysDate = dateFormat.format(date);
    return this.getTotalValue(portfolioID, todaysDate);
  }

  @Override
  public Map<String, Portfolio> getStockDetails() {

    Map temp = new HashMap<>(portfolios);
    return temp;
  }

  /**
   * Helper method to check whether portfolioID exists or not.
   *
   * @param portfolioID portfolioID to be validated
   * @throws NoSuchElementException if portfolioID does not exist
   */
  private void validatePortfolioID(String portfolioID) throws NoSuchElementException {
    if (!portfolios.containsKey(portfolioID)) {
      throw new NoSuchElementException("The given portfolio does not exist");
    }
  }


  @Override
  public void addStockPortfolio(String portfolioID, String company)
          throws NoSuchElementException, IOException,
          IllegalArgumentException {
    validatePortfolioID(portfolioID);
    update(company);
    portfolios.get(portfolioID).addStockData(company);

  }


  @Override
  public void investFixedAmountEqually(String portfolioID, double amount, String date,
                                       double commission) throws IOException {
    validatePortfolioID(portfolioID);
    int companyCount = portfolios.get(portfolioID).getCompanyList().size();
    double investment = amount / companyCount;
    for (String company : portfolios.get(portfolioID).getCompanyList()) {
      buyShare(portfolioID, company, investment, date, commission);
    }
  }

  @Override
  public void investFixedAmountWeighted(String portfolioID, double amount, String date, Map<String,
          Double> weights, double commission) throws IOException {
    validatePortfolioID(portfolioID);
    double investment;
    for (String company : portfolios.get(portfolioID).getCompanyList()) {
      investment = (weights.get(company) / 100) * amount;
      buyShare(portfolioID, company, investment, date, commission);
    }
  }

  @Override
  public void applyDollarCostAveraging(String portfolioID, String startDate, String endDate,
                                       double amount, int period, Map<String, Double> weights,
                                       double commission) throws ParseException, IOException {
    validatePortfolioID(portfolioID);

    portfolios.get(portfolioID).setDollarCostAveraged(true);
    DollarCostAverage data = new DollarCostAverage(startDate, endDate, amount, period,
            weights, commission);
    portfolios.get(portfolioID).setDollarCostAverage(data);
    Calendar currentCal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    currentCal.setTime(sdf.parse(startDate));
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(sdf.parse(endDate));
    if (currentCal.after(endCal)) {
      throw new IllegalArgumentException("start date cannot be after end date");
    }
    for (Map.Entry<String, Double> entry : weights.entrySet()) {
      currentCal.setTime(sdf.parse(startDate));
      String company = entry.getKey();
      double weight = entry.getValue();
      String fullDataForCompany = new String(Files.readAllBytes(Paths.get("data/"
              + company.toLowerCase() + ".csv")));
      while (currentCal.before(endCal)) {
        String currDateString = sdf.format(currentCal.getTime());
        if (fullDataForCompany.contains(currDateString)) {
          buyShare(portfolioID, company, amount * (weight / 100), currDateString, commission);
          currentCal.add(Calendar.DAY_OF_YEAR, period);
        } else {
          currentCal.add(Calendar.DAY_OF_YEAR, 1);
        }
      }
    }
  }


  /**
   * Method to find the next available date to invest.
   *
   * @param portfolioID unique ID of portfolio
   * @param current     date to start investment
   * @param weights     weight of investement for each company
   * @return invested cost basis
   */
  private double investOnNextAvailableDate(String portfolioID, Date current,
                                           Map<String, Double> weights) {
    double costBasis = 0;
    for (String company : portfolios.get(portfolioID).getCompanyList()) {
      String data = "";
      try {
        data = new String(Files.readAllBytes(Paths.get("data/" + company.toLowerCase()
                + ".csv")));
      } catch (IOException e) {
        throw new IllegalArgumentException("Stock data not available for company");
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String currDateString = sdf.format(current);
      while (!data.contains(currDateString)) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        current = calendar.getTime();
        currDateString = sdf.format(current);
      }
    }
    costBasis = costBasis + portfolios.get(portfolioID).getDollarCostAverage().getAmount();
    return costBasis;
  }


  /**
   * Method to update the cached data for companies in a particular portfolio.
   *
   * @param company company data for which cache is to be updated
   * @throws IOException when a file read or write fails
   */
  private void update(String company) throws IOException {
    String fullDataForCompany;
    File tmpDir = new File("data/"
            + company.toLowerCase() + ".csv");
    if (!tmpDir.exists()) {
      fullDataForCompany = AlphaVantage.getStockData(company);
      BufferedWriter writer = new BufferedWriter(new FileWriter("data/"
              + company.toLowerCase() + ".csv", false));
      writer.write(fullDataForCompany);
      writer.close();
    }
  }


  @Override
  public void save(String portfolioID) throws IllegalArgumentException, IOException {
    validatePortfolioID(portfolioID);
    String dataToPersist = "PurchaseDate,CompanyTicker,CostBasis,NumberOfShares,Commission\n";
    List<Stock> stocks = this.getStockDetails().get(portfolioID).getStockList();
    if (stocks.size() <= 0) {
      throw new IllegalArgumentException("The given portfolio has not stocks");
    }
    for (Stock stock : stocks) {
      String stockData = stocks.toString();
      dataToPersist = dataToPersist + stock.getPurchaseDate() + "," + stock.getCompanyTicker()
              + "," + stock.getCostBasis() + "," + stock.getNumberOfShares()
              + "," + stock.getCommission() + "\n";
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter("portfolio/"
            + portfolioID.toLowerCase() + ".csv", false));
    writer.write(dataToPersist);
    writer.close();
  }

  @Override
  public void retrieve(String portfolioID) throws IllegalArgumentException {
    checkForDuplicatePortfolioID(portfolioID);
    String data;
    try {
      data = new String(Files.readAllBytes(Paths.get("portfolio/" + portfolioID.toLowerCase()
              + ".csv")));
      String[] stockData = data.split("\\n");
      createPortfolio(portfolioID);
      for (int i = 1; i < stockData.length; i++) {
        String[] splittedData = stockData[i].split(",");
        String purchaseDate = splittedData[0];
        String company = splittedData[1];
        double commission = Double.parseDouble(splittedData[4]);
        double amount = Double.parseDouble(splittedData[2]) - commission;
        buyShare(portfolioID, company, amount, purchaseDate, commission);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("The given portfolio is not present in saved data");
    }
  }

  @Override
  public void saveStrategy(String portfolioID, String strategyName) throws IOException,
          IllegalArgumentException {
    validatePortfolioID(portfolioID);
    if (!this.portfolios.get(portfolioID).getDollarCostAveraged()) {
      throw new IllegalArgumentException("This portfolio does not posses a dollar cost strategy");
    }
    String dataToPersist = "StartDate,EndDate,PeriodInDays,amount,weights,commission\n";

    DollarCostAverage dollarCostAverage = this.portfolios.get(portfolioID).getDollarCostAverage();
    dataToPersist += dollarCostAverage.getStartDate() + "," + dollarCostAverage.getEndDate() + ","
            + dollarCostAverage.getPeriodInDays() + "," + dollarCostAverage.getAmount() + ","
            + dollarCostAverage.getWeights().toString().replaceAll(",", ";")
            + "," + dollarCostAverage.getCommission();
    BufferedWriter writer = new BufferedWriter(new FileWriter("strategy/"
            + strategyName.toLowerCase() + ".csv", false));
    writer.write(dataToPersist);
    writer.close();
  }

  @Override
  public void retrieveStrategy(String strategyName, String portfolioID)
          throws IllegalArgumentException {
    validatePortfolioID(portfolioID);
    String data;
    try {
      data = new String(Files.readAllBytes(Paths.get("strategy/" + strategyName.toLowerCase()
              + ".csv")));
      String[] splittedData = data.split("\n")[1].split(",");
      String companyData = splittedData[4].replaceAll("[{}]", "");
      String[] companiesDetails = companyData.split("; ");
      Map<String, Double> map = new HashMap();
      for (String company : companiesDetails) {
        String name = company.split("=")[0];
        double weight = Double.parseDouble(company.split("=")[1]);
        map.put(name, weight);
      }
      int periodInDays = Integer.parseInt(splittedData[2]);
      double amount = Double.parseDouble(splittedData[3]);
      String startDate = splittedData[0];
      String endDate = splittedData[1];
      double commission = Double.parseDouble(splittedData[5]);
      applyDollarCostAveraging(portfolioID, startDate, endDate, amount, periodInDays,
              map, commission);

    } catch (IOException | ParseException e) {
      throw new IllegalArgumentException("The given strategy is not present in saved data");
    }
  }

  @Override
  public void getValuesForGraph(String portfolioID) throws ParseException,
          IOException {

    validatePortfolioID(portfolioID);
    Map<String, Double> valueMap = new TreeMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal_end = new GregorianCalendar();
    cal_end.setTime(new Date());
    Calendar cal_start = getFirstPurchaseDate(portfolioID);
    double value = 0;
    int daysBetween = daysBetween(cal_start.getTime(), cal_end.getTime());
    int counter;
    if (daysBetween < 10) {
      counter = 1;
    } else {
      counter = daysBetween / 10;
    }
    while (cal_start.before(cal_end)) {
      String date = sdf.format(cal_start.getTime());
      try {
        value = getTotalValue(portfolioID, date);
      } catch (IllegalArgumentException e) {
        // value remains same for next date even if the data is not available
      } finally {
        valueMap.put(date, value);
        cal_start.add(Calendar.DAY_OF_YEAR, counter);
      }
    }
    String fullDataForGraph = "";
    for (Map.Entry<String, Double> entry : valueMap.entrySet()) {
      fullDataForGraph = fullDataForGraph + entry.getKey() + "," + entry.getValue() + "\n";
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter("temp/graphdata.csv",
            false));
    writer.write(fullDataForGraph);
    writer.close();
    if (fullDataForGraph.isEmpty()) {
      throw new IllegalArgumentException("The given portfolio is empty");
    }
  }

  private Calendar getFirstPurchaseDate(String portfolioID) throws ParseException {
    List<Stock> stocks = getStockDetails().get(portfolioID).getStockList();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    for (Stock stock : stocks) {
      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(sdf.parse(stock.getPurchaseDate()));
      if (cal2.before(cal)) {
        cal = cal2;
      }

    }
    return cal;
  }

  private int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }


}