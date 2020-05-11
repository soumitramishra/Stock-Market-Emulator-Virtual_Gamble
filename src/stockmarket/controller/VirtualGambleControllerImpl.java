package stockmarket.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import stockmarket.model.VirtualGamble;
import stockmarket.view.VirtualGambleView;

/**
 * This is a Controller Class which implements the VirtualGambleController Interface. It implements
 * all the operations mandated by the VirtualGambleController Interface.
 */
public class VirtualGambleControllerImpl implements VirtualGambleController {

  private Appendable ap;
  private String portfolioID;
  private String companyName;
  private double amount;
  private String date;
  private Scanner scan;
  private VirtualGambleView view;
  private int count;
  private Map<String, Double> weights;
  private double commission;
  private double weight;
  private String strategyName;

  /**
   * This creates an object of VirtualGambleControllerImpl.
   *
   * @param rd The readable object which contains the user input.
   * @param ap The appendable object to process application output.
   */
  public VirtualGambleControllerImpl(Readable rd, Appendable ap) {
    if (rd == null) {
      throw new IllegalArgumentException("Readable object cannot be null");
    }

    if (ap == null) {
      throw new IllegalArgumentException("Appendable object cannot be null");
    }
    this.ap = ap;
    scan = new Scanner(rd);
    weights = new HashMap<>();
  }

  @Override
  public void startUp(VirtualGamble model, VirtualGambleView view) throws IOException,
          ParseException {

    Objects.requireNonNull(model);
    Objects.requireNonNull(view);
    this.view = view;


    while (true) {
      view.print(ap, "Enter your choice from the menu:\n"
              + "1) Create a new Portfolio\n2) Buy share\n3) Get Total Cost Basis of a portfolio\n"
              + "4) Get the total value of a portfolio at a certain date\n5) Get a list of all "
              + "portfolios\n6) Add companies to an portfolio without buying share\n"
              + "7) Invest one time with amount distributed among companies\n"
              + "8)Invest using dollar cost averaging on a portfolio\n"
              + "9) Save a portfolio\n"
              + "10) Retrieve a portfolio\n"
              + "11)Save a strategy\n"
              + "12) Retrieve a strategy\n"
              + "Enter q/Q to quit this application at any point of time.\n");
      switch (scan.next()) {
        //Create a portfolio.
        case "1":
          if (!createPortfolio(model)) {
            return;
          }
          continue;
          // Buy a share and store in the given portfolio.
        case "2":

          if (!buyshare(model)) {
            return;
          }
          continue;

          //Obtain the cost basis of a given portfolio.
        case "3":
          if (!getCostBasis(model)) {
            return;
          }
          continue;


          //Obtain the total value of a portfolio at a certain date.
        case "4":
          if (!totalValue(model)) {
            return;
          }
          continue;

          //Get total details of portfolio
        case "5":
          Map portfolioList = model.getStockDetails();
          view.print(ap, portfolioList.keySet().toString() + "\n");
          continue;
          //Add company without buying share
        case "6":
          if (!addCompany(model)) {
            return;
          }
          continue;
          //Invest specific amount in portfolio
        case "7":
          if (!invest(model)) {
            return;
          }
          continue;
          //Apply dollar cost averaging on portfolio
        case "8":
          if (!applyDollarCost(model)) {
            return;
          }
          continue;
          //Quit
        case "9":
          if (!savePortfolio(model)) {
            return;
          }
          continue;
        case "10":
          if (!retrievePortfolio(model)) {
            return;
          }
          continue;
        case "11":
          if (!saveStrategy(model)) {
            return;
          }
          continue;
        case "12":
          if (!retrieveStrategy(model)) {
            return;
          }
          continue;
        case "q":
        case "Q":
          return;

        default:
          view.print(ap, "Wrong option. Enter the correct option number from the menu\n\n");

      }

    }
  }

  private boolean retrieveStrategy(VirtualGamble model) {
    view.print(ap, "Enter the strategy name to be retreived");
    if (!setStrategyName()) {
      return false;
    }
    view.print(ap, "Enter the portfolio name to apply this strategy");
    if (!setPortfolioID()) {
      return false;
    }
    try {
      model.retrieveStrategy(strategyName, portfolioID);
    } catch (IllegalArgumentException | NoSuchElementException e) {
      view.print(ap, e.getMessage());
    }
    return true;
  }

  private boolean saveStrategy(VirtualGamble model) {
    view.print(ap, "Enter the portfolio name of which Dollar cost strategy is to be saved\n");
    if (!setPortfolioID()) {
      return false;
    }
    view.print(ap, "Enter a strategy name\n");
    if (!setStrategyName()) {
      return false;
    }
    try {
      model.saveStrategy(portfolioID, strategyName);
    } catch (IllegalArgumentException | IOException | NoSuchElementException e) {
      view.print(ap, e.getMessage());
    }
    return true;
  }

  private boolean retrievePortfolio(VirtualGamble model) {
    view.print(ap, "Enter the portfolio name to be retrieved");
    if (!setPortfolioID()) {
      return false;
    }
    try {
      model.retrieve(portfolioID);
    } catch (IllegalArgumentException e) {
      view.print(ap, e.getMessage());
    }
    return true;
  }

  private boolean savePortfolio(VirtualGamble model) {
    view.print(ap, "Enter the portfolio name to be saved");
    if (!setPortfolioID()) {
      return false;
    }
    try {
      model.save(portfolioID);
    } catch (IOException | IllegalArgumentException | NoSuchElementException e) {
      view.print(ap, e.getMessage());
    }
    return true;
  }

  /**
   * Helper methopd to apply dollar cost averaging on a portfolio.
   */
  private boolean applyDollarCost(VirtualGamble model) throws ParseException, IOException {
    view.print(ap, "Enter portfolio name to apply dollar cost averaging on\n");
    if (!setPortfolioID()) {
      return false;
    }

    view.print(ap, "Enter Amount\n");
    if (!setAmount()) {
      return false;
    }

    view.print(ap, "Enter commission");

    if (!setCommissionFee()) {
      return false;
    }

    view.print(ap, "Press 1 for equal weights\n Press 2 to specify weights");
    Set<String> companies = model.getStockDetails().get(portfolioID).getCompanyList();
    if (scan.next().equals("1")) {
      weight = (1.0 / companies.size()) * 100.0;
      for (String tickr : companies) {
        weights.put(tickr, weight);
      }
    } else {

      view.print(ap, "Enter weights corresponding to companies\n");
      while (true) {
        double sumOfWeights = 0;
        for (String tickr : companies) {
          view.print(ap, "Enter weight for" + tickr);
          if (!setWeight()) {
            return false;
          }
          weights.put(tickr, weight);
        }
        for (double weight : weights.values()) {
          sumOfWeights += weight;
        }
        if (sumOfWeights == 100) {
          break;
        } else {
          view.print(ap, "Sum of weights of all companies must be 100 \n");
        }
      }
    }

    view.print(ap, "Enter time period to repeat transaction");
    if (!setCount()) {
      return false;
    }
    int period = count;


    view.print(ap, "Specify a start date\n");
    String startDate = "";
    if (!setDate()) {
      return false;
    }
    startDate = date;

    view.print(ap, "Press 1 to specify an end date or 2 for ongoing policy");

    if (scan.next().equals("2")) {
      model.applyDollarCostAveraging(portfolioID, startDate,
              new SimpleDateFormat("yyyy-MM-dd").format(new Date()), amount, period,
              weights, commission);
    } else {
      if (!setDate()) {
        return false;
      }
      String endDate = date;


      try {
        model.investFixedAmountWeighted(portfolioID, amount, date, weights, commission);
      } catch (IOException | IllegalArgumentException e) {
        view.print(ap, e.getMessage());
      }
      try {
        model.applyDollarCostAveraging(portfolioID, startDate, endDate, amount, period, weights,
                commission);
      } catch (IllegalArgumentException | NoSuchElementException e) {
        view.print(ap, e.getMessage() + "\n");
      }

    }
    return true;
  }


  /**
   * Helper method to find out the total value of the portfolio.
   */
  private boolean totalValue(VirtualGamble model) {
    view.print(ap, "Enter portfolio name\n");
    if (!setPortfolioID()) {
      return false;
    }
    view.print(ap, "Enter date in the format YYYY-MM-DD\n");
    if (!setDate()) {
      return false;
    }
    try {
      double totalValue = model.getTotalValue(portfolioID, date);
      view.print(ap, "Total Value of " + portfolioID + " is" + totalValue + "\n\n");
    } catch (IllegalArgumentException | NoSuchElementException e) {
      view.print(ap, e.getMessage() + "\n");
    }
    return true;
  }

  /**
   * Helper method to find out the cost basis of the portfolio.
   */
  private boolean getCostBasis(VirtualGamble model) {
    view.print(ap, "Enter portfolio name\n");
    if (!setPortfolioID()) {
      return false;
    }
    view.print(ap, "Enter the date up to which cost basis is required.");
    if (!setDate()) {
      return false;
    }
    try {
      double costBasis = model.getTotalCostBasis(portfolioID, date);
      view.print(ap, "Cost basis of " + portfolioID + " is" + costBasis + "\n\n");
    } catch (IllegalArgumentException | NoSuchElementException | ParseException e) {
      view.print(ap, e.getMessage() + "\n");
    }
    return true;
  }

  /**
   * Helper method to buy a share in the portfolio.
   */
  private boolean buyshare(VirtualGamble model) {
    view.print(ap, "Enter portfolio name\n");
    if (!setPortfolioID()) {
      return false;
    }
    view.print(ap, "Enter Company Ticker Name id\n");
    if (!setCompany()) {
      return false;
    }
    view.print(ap, "Enter Amount\n");
    if (!setAmount()) {
      return false;
    }

    view.print(ap, "Enter commission");

    if (!setCommissionFee()) {
      return false;
    }
    view.print(ap, "Enter date in the format YYYY-MM-DD\n");

    if (!setDate()) {
      return false;
    }
    try {
      model.buyShare(portfolioID, companyName, amount, date, commission);
      view.print(ap, "Successfully brought share\n\n");
    } catch (IllegalArgumentException | NoSuchElementException | IOException e) {
      view.print(ap, e.getMessage() + "\n");
    }
    return true;
  }

  /**
   * This is a private helper method to validate and store the commission fee.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setCommissionFee() {
    String temp = scan.next();
    if (quit(temp)) {
      return false;
    }


    String regex = "\\d+\\.?\\d*";
    if (!temp.matches(regex)) {
      view.print(ap, "Enter a valid amount\n");
      setCommissionFee();
    } else {
      commission = Double.parseDouble(temp);
    }
    return true;
  }


  /**
   * This is a private helper method to validate and set the company ticker name.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setCompany() {

    companyName = scan.next().toUpperCase();
    if (quit(companyName)) {
      return false;
    }

    Pattern p = Pattern.compile("[^a-zA-Z]");
    boolean hasSpecialChar = p.matcher(companyName).find();
    if (hasSpecialChar) {
      view.print(ap, "Company ticker name do not contain special characters or numbers."
              + " ReEnter the name\n");
      setCompany();
    }
    if (companyName.length() != 4) {
      view.print(ap, "Company ticker name can only be of 4 letters. "
              + "ReEnter the company name\n");
      setCompany();
    }
    return true;

  }

  /**
   * This is a private helper method to validate and set the amount of which shares has to be
   * bought.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setAmount() {

    String temp = scan.next();
    if (quit(temp)) {
      return false;
    }


    String regex = "\\d+\\.?\\d*";
    if (!temp.matches(regex)) {
      view.print(ap, "Enter a valid amount\n");
      setAmount();
    } else {
      amount = Double.parseDouble(temp);
    }
    return true;
  }

  /**
   * This is a private helper method to validate the number of companies to invest in.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setCount() {

    String temp = scan.next();
    if (quit(temp)) {
      return false;
    }


    String regex = "\\d+";
    if (!temp.matches(regex)) {
      view.print(ap, "Enter a valid number\n");
      setCount();
    } else {
      count = Integer.parseInt(temp);
    }
    return true;
  }


  /**
   * This is a private helper method to validate the weights in an investment.
   */
  private boolean setWeight() {

    String temp = scan.next();
    if (quit(temp)) {
      return false;
    }


    String regex = "\\d+\\.?\\d*";
    if (!temp.matches(regex)) {
      view.print(ap, "Enter a valid weight\n");
      setWeight();
    } else {
      weight = Double.parseDouble(temp);
    }
    return true;
  }


  /**
   * This is a private helper method to validate and set the Date.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setDate() {
    date = scan.next();
    if (quit(date)) {
      return false;
    }
    String dateFormat = "\\d{4}-\\d{2}-\\d{2}";
    if (!date.matches(dateFormat)) {
      view.print(ap, "Enter a valid date in the form of YYYY-MM-DD\n");
      setDate();
    }
    return true;
  }

  /**
   * This is a private helper method to validate and set the Portfolio name.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setPortfolioID() {
    portfolioID = scan.next();
    if (quit(portfolioID)) {
      return false;
    }

    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
    boolean hasSpecialChar = p.matcher(portfolioID).find();
    if (hasSpecialChar) {
      view.print(ap, "Portfolio name cannot contain special characters. ReEnter the name\n");
      setPortfolioID();
    }
    return true;
  }

  /**
   * This is a private helper method to set the strategy name to be saved.
   *
   * @return false if user wants to quit / true otherwise.
   */
  private boolean setStrategyName() {
    strategyName = scan.next();
    if (quit(strategyName)) {
      return false;
    }

    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
    boolean hasSpecialChar = p.matcher(strategyName).find();
    if (hasSpecialChar) {
      view.print(ap, "Strategy name cannot contain special characters. ReEnter the name\n");
      setStrategyName();
    }
    return true;
  }


  /**
   * This is a private helper method to check if the user wants to quit the application at any point
   * of time.
   *
   * @return true if user wants to quit/false otherwise.
   */
  private boolean quit(String param) {
    if (param.equalsIgnoreCase("Q")) {
      view.print(ap, "Thanks for using the Virtual Gamble Application\n");
      return true;
    } else {
      return false;
    }
  }

  /**
   * Helper method to add company to portfolio without buying share.
   */
  private boolean addCompany(VirtualGamble model) {
    view.print(ap, "Enter portfolio name\n");
    if (!setPortfolioID()) {
      return false;
    }
    view.print(ap, "Enter the number pf companies to be added");
    if (!setCount()) {
      return false;
    }
    view.print(ap, "Enter company names\n");
    for (int i = 0; i < count; i++) {
      if (!setCompany()) {
        return false;
      }
      try {
        model.addStockPortfolio(portfolioID, companyName);
      } catch (IllegalArgumentException | NoSuchElementException | IOException e) {
        view.print(ap, e.getMessage() + "\n ");
        view.print(ap, "Re-enter company name");
        count++;
      }
    }
    return true;
  }

  /**
   * Helper method to invest a ceratin amount using weights in a portfolio.
   */
  private boolean invest(VirtualGamble model) {

    view.print(ap, "Enter portfolio name\n");
    if (!setPortfolioID()) {
      return false;
    }
    return (investAmount(model));
  }

  /**
   * This private helper method allows us to invest a certain amount in the portfolio.
   */
  private boolean investAmount(VirtualGamble model) {
    view.print(ap, "Enter the amount you want to invest in this portfolio");
    if (!setAmount()) {
      return false;
    }

    view.print(ap, "Enter commission");

    if (!setCommissionFee()) {
      return false;
    }

    view.print(ap, "Enter date in the format YYYY-MM-DD\n");
    if (!setDate()) {
      return false;
    }
    return (setWeights(model));
  }

  private boolean setWeights(VirtualGamble model) {
    view.print(ap, "Press 1 for equal weights\n Press 2 to specify weights");

    if (scan.next().equals("1")) {
      try {
        model.investFixedAmountEqually(portfolioID, amount, date, commission);
      } catch (IOException e) {
        view.print(ap, e.getMessage());
      }
    } else {
      Set<String> companies = model.getStockDetails().get(portfolioID).getCompanyList();
      view.print(ap, "Enter weights corresponding to companies\n");
      while (true) {
        double sumOfWeights = 0;
        for (String tickr : companies) {
          view.print(ap, "Enter weight for" + tickr);
          if (!setWeight()) {
            return false;
          }
          weights.put(tickr, weight);
        }
        for (double weight : weights.values()) {
          sumOfWeights += weight;
        }
        if (sumOfWeights == 100) {
          break;
        } else {
          view.print(ap, "Sum of weights of all companies must be 100 \n");
        }
      }

      try {
        model.investFixedAmountWeighted(portfolioID, amount, date, weights, commission);
      } catch (IOException | IllegalArgumentException e) {
        view.print(ap, e.getMessage());
      }
    }


    return true;
  }


  /**
   * Helper method to create a portfolio.
   */
  private boolean createPortfolio(VirtualGamble model) {
    view.print(ap, "Enter the name of the portfolio to be created\n"
            + "Portfolio name can can be a combination of numbers and letters. Special"
            + "characters are not permitted\nEnter q/Q to quit.\n\n");

    if (!setPortfolioID()) {
      return false;
    }
    try {
      model.createPortfolio(portfolioID);
      view.print(ap, "Portfolio " + portfolioID + " has been successfully created\n\n");
    } catch (IllegalArgumentException | NoSuchElementException e) {
      view.print(ap, e.getMessage() + "\n ");
    }
    return true;
  }
}
