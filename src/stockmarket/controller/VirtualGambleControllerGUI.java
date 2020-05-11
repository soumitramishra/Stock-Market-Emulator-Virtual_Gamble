package stockmarket.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

import stockmarket.model.VirtualGamble;
import stockmarket.view.VirtualGambleGUI;

/**
 * This is a GUI Controller Class which implements the Features Interface. It implements all the
 * operations mandated by the Features Interface.
 */
public class VirtualGambleControllerGUI implements Features {
  private final VirtualGamble model;
  private final VirtualGambleGUI view;

  private String portfolioID;
  private String companyName;
  private double amount;
  private String date;
  private Map<String, Double> weights;
  private double commission;
  private String strategyName;
  private String startDate;
  private String endDate;
  private int period;

  /**
   * Constructor to initialize the model and view of the application.
   *
   * @param m model of the VirtualGamble application
   * @param v view of the VirtualGamble application
   */
  public VirtualGambleControllerGUI(VirtualGamble m, VirtualGambleGUI v) {
    this.model = m;
    this.view = v;
  }

  @Override
  public void startUp() {
    view.setVisible(true);
    weights = new HashMap<>();
  }

  @Override
  public void processInput(String choice, Map<String, String> userInputs) {
    System.out.println("Choice:" + choice);
    try {
      setInputVariables(userInputs);
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
    switch (choice) {
      case "Create Portfolio":
        createPortfolio();
        break;
      case "Buy Share":
        buyShare();
        break;
      case "Get Cost Basis of a portfolio":
        getTotalCostBasis();
        break;
      case "Get Total Value of a portfolio":
        getTotalValue();
        break;
      case "List all portfolios":
        listAllPortfolios();
        break;
      case "Add company to portfolio":
        addCompanyToPortfolioID();
        break;
      case "Invest amount equally among companies":
        investFixedAmountEqually();
        break;
      case "Invest amount weighted among companies":
        investFixedAmountWeightedForm();
        break;
      case "Apply dollar cost average equally among companies":
        applyDollarCostEqually();
        break;
      case "Apply dollar cost average weighted among companies":
        applyDollarCostWeightedForm();
        break;
      case "Invest Weighted":

        investFixedAmountWeighted(userInputs);
        break;
      case "Dollar cost average weighted":

        applyDollarCostWeighted(userInputs);
        break;
      case "Save portfolio":
        savePortfolio();
        break;
      case "Retrieve a portfolio":
        retrievePortfolio();
        break;
      case "Save Strategy":
        saveStrategy();
        break;
      case "Retrieve a Strategy":
        retrieveStrategy();
        break;
      case "Generate the graph for performance of a portfolio over time":
        generateGraph();
        break;
      default:
        view.showMessage("invalid option");
    }
  }

  /**
   * Helper method to plot performance graph of a portfolio overtime.
   */
  private void generateGraph() {
    try {
      model.getValuesForGraph(portfolioID);
      view.showGraph();
    } catch (ParseException | IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to retrieve a previously saved strategy.
   */
  private void retrieveStrategy() {
    try {
      model.retrieveStrategy(strategyName, portfolioID);
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to save a dollar cost strategy.
   */
  private void saveStrategy() {
    try {
      model.saveStrategy(portfolioID, strategyName);
    } catch (NoSuchElementException | IOException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to retrieve a previously saved Portfolio.
   */
  private void retrievePortfolio() {
    try {
      model.retrieve(portfolioID);
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to save a portfolio.
   */
  private void savePortfolio() {
    try {
      model.save(portfolioID);
    } catch (IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to apply dollar cost average strategy to a portfolio with custom weights.
   */
  private void applyDollarCostWeighted(Map<String, String> userInputs) {
    try {
      getWeights(userInputs);
      model.applyDollarCostAveraging(portfolioID, startDate, endDate, amount, period, weights,
              commission);
    } catch (ParseException | IOException | NoSuchElementException
            | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to invest amount to each company in a portfolio with custom weights.
   */
  private void investFixedAmountWeighted(Map<String, String> userInputs) {
    try {
      getWeights(userInputs);
      model.investFixedAmountWeighted(portfolioID, amount, date, weights, commission);
    } catch (IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to generate form to take user inputs to apply dollar cost average strategy.
   */
  private void applyDollarCostWeightedForm() {
    Set<String> companies = model.getStockDetails().get(portfolioID).getCompanyList();
    String[] options = new String[companies.size() + 5];
    options[0] = "Start Date";
    options[1] = "End Date";
    options[2] = "Amount";
    options[3] = "Period";
    options[4] = "Commission";
    int i = 5;
    for (String company : companies) {
      options[i++] = company;
    }
    view.openForm(options, "Dollar cost average weighted");
  }

  /**
   * Helper method to apply dollar cost average strategy to a portfolio with equal weights.
   */
  private void applyDollarCostEqually() {
    setEqualWeights();
    try {
      model.applyDollarCostAveraging(portfolioID, startDate, endDate, amount, period, weights,
              commission);
    } catch (ParseException | IOException | NoSuchElementException
            | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to generate a form to take user inputs in order to invest into a portfolio with
   * custome weights.
   */
  private void investFixedAmountWeightedForm() {
    Set<String> companies = model.getStockDetails().get(portfolioID).getCompanyList();
    String[] options = new String[companies.size() + 3];
    options[0] = "Amount";
    options[1] = "Date";
    options[2] = "Commission";
    int i = 3;
    for (String company : companies) {
      options[i++] = company;
    }
    view.openForm(options, "Invest Weighted");
  }

  private void investFixedAmountEqually() {
    try {
      model.investFixedAmountEqually(portfolioID, amount, date, commission);
    } catch (IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to add company to a portfolio without actually buying share.
   */
  private void addCompanyToPortfolioID() {
    try {
      model.addStockPortfolio(portfolioID, companyName);
    } catch (IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to list all the portfolios.
   */
  private void listAllPortfolios() {
    Set<String> portolios = model.getStockDetails().keySet();
    String portfoliosString = "";
    for (String portfolio : portolios) {
      portfoliosString += portfolio + "\n";
    }
    view.showMessage(portfoliosString);
  }

  /**
   * Helper method to get total value  of a portfolio.
   */
  private void getTotalValue() {
    try {
      view.showMessage("Total value for Portfolio " + portfolioID
              + model.getTotalValue(portfolioID, date));
    } catch (NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to get total cost basis for this portfolio.
   */
  private void getTotalCostBasis() {
    try {
      view.showMessage("Total cost basis for Portfolio " + portfolioID
              + model.getTotalCostBasis(portfolioID, date));
    } catch (ParseException | NoSuchElementException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to buyShare.
   */
  private void buyShare() {
    try {
      System.out.println("commission = " + commission);
      model.buyShare(portfolioID, companyName, amount, date, commission);
    } catch (IOException | NoSuchElementException | IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to create a new portfolio.
   */
  private void createPortfolio() {
    try {
      model.createPortfolio(portfolioID);
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
  }

  /**
   * Helper method to set equal weights to be used for investment.
   */
  private void setEqualWeights() {
    weights.clear();
    Set<String> companies = model.getStockDetails().get(portfolioID).getCompanyList();
    for (String company : companies) {
      weights.put(company, (1.0 / companies.size()) * 100.0);
    }
  }

  @Override
  public Collection<String> getData(String requiredData) {
    if (requiredData.equals("List all portfolios")) {
      return model.getStockDetails().keySet();
    } else if (requiredData.equals("Get company List")) {
      return model.getStockDetails().get(portfolioID).getCompanyList();
    }
    return null;
  }

  /**
   * Helper method to set input variables from the user inputs.
   *
   * @param userInputs a map representing all the user inputs
   * @throws if one or more user inputs are not valid
   */
  private void setInputVariables(Map<String, String> userInputs) throws IllegalArgumentException {
    if (userInputs.containsKey("PortfolioID")) {
      portfolioID = userInputs.get("PortfolioID");
      checkSpecialCharacters(portfolioID);
    }
    if (userInputs.containsKey("Company Ticker")) {
      companyName = userInputs.get("Company Ticker");
      checkSpecialCharacters(companyName);
      if (companyName.length() != 4) {
        throw new IllegalArgumentException("Company name must be of length 4");
      }
    }
    if (userInputs.containsKey("Amount")) {
      isDecimal(userInputs.get("Amount"));
      amount = Double.parseDouble(userInputs.get("Amount"));
    }
    if (userInputs.containsKey("Commission")) {
      isDecimal(userInputs.get("Commission"));
      commission = Integer.parseInt(userInputs.get("Commission"));
    }
    if (userInputs.containsKey("Date")) {
      date = userInputs.get("Date");
      validateDate(date);
    }
    if (userInputs.containsKey("Strategy Name")) {
      strategyName = userInputs.get("Strategy Name");
      checkSpecialCharacters(strategyName);
    }
    if (userInputs.containsKey("Start Date")) {
      startDate = userInputs.get("Start Date");
      validateDate(startDate);
    }
    if (userInputs.containsKey("End Date")) {
      endDate = userInputs.get("End Date");
      validateDate(endDate);
    }
    if (userInputs.containsKey("Period")) {
      isInteger(userInputs.get("Period"));
      period = Integer.parseInt(userInputs.get("Period"));
    }

  }

  /**
   * Helper method to check that a period is a positive integer or not.
   *
   * @param period to be validated for being a positive integer
   * @throws IllegalArgumentException if the period is not positive integer
   */
  private void isInteger(String period) throws IllegalArgumentException {
    String regex = "\\d+";
    if (!period.matches(regex)) {
      throw new IllegalArgumentException("Period must be a positive integer");
    }
  }

  /**
   * Helper method to validate the date format.
   *
   * @param date date to be validated
   * @throws IllegalArgumentException if the date is not in proper format
   */
  private void validateDate(String date) throws IllegalArgumentException {
    String dateFormat = "\\d{4}-\\d{2}-\\d{2}";
    if (!date.matches(dateFormat)) {
      throw new IllegalArgumentException("Date must be of the format yyyy-MM-dd");
    }
  }

  /**
   * Helper method to verify whether that a given string has special characters.
   *
   * @param string string to be validated
   * @throws IllegalArgumentException if the string contains special characters.
   */
  private void checkSpecialCharacters(String string) throws IllegalArgumentException {
    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
    if (p.matcher(string).find()) {
      throw new IllegalArgumentException("Portfolio name, Company Ticker or strategy name "
              + "cannot contain special characters");
    }
  }

  /**
   * Method to check a given string is in decimal format or not.
   *
   * @param string to be validated
   * @throws IllegalArgumentException if the string is not in proper decimal format
   */
  private void isDecimal(String string) throws IllegalArgumentException {
    String regex = "\\d+\\.?\\d*";
    if (!string.matches(regex)) {
      throw new IllegalArgumentException("Amount, weights and commission fee must be decimals");
    }
  }

  /**
   * Method to get the weights from the user input.
   *
   * @param userInputs map representing all user inputs
   * @throws IllegalArgumentException if the sum of weights is not equal to 100
   */
  private void getWeights(Map<String, String> userInputs) throws IllegalArgumentException {
    weights.clear();
    double sum = 0;
    for (Map.Entry<String, String> entry : userInputs.entrySet()) {
      if (entry.getKey().equals("PortfolioID") || entry.getKey().equals("Company Ticker")
              || entry.getKey().equals("Amount") || entry.getKey().equals("Commission")
              || entry.getKey().equals("Date") || entry.getKey().equals("Strategy Name")
              || entry.getKey().equals("Start Date") || entry.getKey().equals("End Date")
              || entry.getKey().equals("Period")) {
        //Do nothing
      } else {
        isDecimal(entry.getValue());
        weights.put(entry.getKey(), Double.parseDouble(entry.getValue()));
        sum += Double.parseDouble(entry.getValue());
      }
    }
    if (sum != 100) {
      throw new IllegalArgumentException("Sum of weights must be 100");
    }
  }

}
