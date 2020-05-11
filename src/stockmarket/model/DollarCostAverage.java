package stockmarket.model;

import java.util.Map;

/**
 * This class represents the parameters of Dollar Cost Average Investment Strategy.
 */
public class DollarCostAverage {
  private final double commission;
  private String startDate;
  private String endDate;
  private double amount;
  private Map<String, Double> weights;
  int periodInDays;

  public double getCommission() {
    return commission;
  }

  /**
   * Constructor to initialize DollarCostAverage.
   */
  public DollarCostAverage(String startDate, String endDate, double amount, int periodInDays,
                           Map<String, Double> weights, double commission) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.amount = amount;
    this.weights = weights;
    this.periodInDays = periodInDays;
    this.commission = commission;
  }

  /**
   * Method to get start date.
   *
   * @return start date
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Method to get end date.
   *
   * @return end date of investment
   */
  public String getEndDate() {
    return endDate;
  }


  /**
   * Method to get amount of investment.
   *
   * @return amount of investment
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Method to set amount of investment.
   *
   * @param amount amount of investment
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Method to get weights of investment.
   *
   * @return weights of investment
   */
  public Map<String, Double> getWeights() {
    return weights;
  }

  /**
   * Method to set weights of investment.
   *
   * @param weights weights of investment
   */
  public void setWeights(Map<String, Double> weights) {
    this.weights = weights;
  }

  /**
   * Method to get Period in Days.
   *
   * @return period in days
   */
  public int getPeriodInDays() {
    return periodInDays;
  }
}
