package stockmarket.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.AbstractButton;
import javax.swing.Box;

import java.awt.GridLayout;
import java.awt.Dimension;

import java.util.Enumeration;

import stockmarket.controller.Features;

/**
 * This class implements the VirtualGambleGUI interface and implements all the operations mandated
 * by the interface.
 */
public class VirtualGambleGUIImpl extends JFrame implements VirtualGambleGUI {

  private final ButtonGroup choices;
  private VirtualGambleForm form;
  private final JRadioButton createPortfolioButton;
  private JRadioButton buyShareButton;
  private JRadioButton getCostBasisButton;
  private JRadioButton getTotalValueButton;
  private JRadioButton getAllPortfoliosButton;
  private JRadioButton addCompanyButton;
  private JRadioButton investEquallyButton;
  private JRadioButton investWeightedButton;
  private JRadioButton dollarCostAverageButton;
  private JRadioButton dollarCostWeightedButton;
  private JRadioButton savePortfolioButton;
  private JRadioButton retrievePortfolioButton;
  private JRadioButton saveStrategyButton;
  private JRadioButton retrieveStrategyButton;
  private JRadioButton getGraphicalPerformance;
  private final JPanel panel;
  private Features features;

  /**
   * Constructor to create the home screen of the application by adding various options and submit
   * button.
   *
   * @param caption the title of the application
   */
  public VirtualGambleGUIImpl(String caption) {
    super(caption);
    setSize(500, 300);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    panel = new JPanel();
    panel.setLayout(new GridLayout(16, 1));
    createPortfolioButton = new JRadioButton("Create Portfolio");
    panel.add(createPortfolioButton);
    addButtons();
    choices = new ButtonGroup();
    groupButtons();
    this.add(panel);
    this.setResizable(false);
    pack();
  }

  /**
   * Method to group the radio buttons.
   */
  private void groupButtons() {

    choices.add(createPortfolioButton);
    choices.add(buyShareButton);
    choices.add(getCostBasisButton);
    choices.add(getTotalValueButton);
    choices.add(getAllPortfoliosButton);
    choices.add(addCompanyButton);
    choices.add(investEquallyButton);
    choices.add(investWeightedButton);
    choices.add(dollarCostAverageButton);
    choices.add(dollarCostWeightedButton);
    choices.add(savePortfolioButton);
    choices.add(retrievePortfolioButton);
    choices.add(saveStrategyButton);
    choices.add(retrieveStrategyButton);
    choices.add(getGraphicalPerformance);
  }

  /**
   * Method to add buttons to the panel.
   */
  private void addButtons() {
    JButton submitButton;
    buyShareButton = new JRadioButton("Buy Share");
    panel.add(buyShareButton);
    getCostBasisButton = new JRadioButton("Get Cost Basis of a portfolio");
    panel.add(getCostBasisButton);
    getTotalValueButton = new JRadioButton("Get Total Value of a portfolio");
    panel.add(getTotalValueButton);
    getAllPortfoliosButton = new JRadioButton("List all portfolios");
    panel.add(getAllPortfoliosButton);
    addCompanyButton = new JRadioButton("Add company to portfolio");
    panel.add(addCompanyButton);
    investEquallyButton = new JRadioButton("Invest amount equally among companies");
    panel.add(investEquallyButton);
    investWeightedButton = new JRadioButton("Invest amount weighted among companies");
    panel.add(investWeightedButton);
    dollarCostAverageButton = new JRadioButton("Apply dollar cost average equally among "
            + "companies");
    panel.add(dollarCostAverageButton);
    dollarCostWeightedButton = new JRadioButton("Apply dollar cost average weighted among "
            + "companies");
    panel.add(dollarCostWeightedButton);

    savePortfolioButton = new JRadioButton("Save portfolio");
    panel.add(savePortfolioButton);
    retrievePortfolioButton = new JRadioButton("Retrieve a portfolio");
    panel.add(retrievePortfolioButton);
    saveStrategyButton = new JRadioButton("Save Strategy");
    panel.add(saveStrategyButton);
    retrieveStrategyButton = new JRadioButton("Retrieve a Strategy");
    panel.add(retrieveStrategyButton);
    getGraphicalPerformance = new JRadioButton("Generate the graph for performance of "
            + "a portfolio over time");
    panel.add(getGraphicalPerformance);
    panel.add(Box.createRigidArea(new Dimension(0, 0)));
    submitButton = new JButton("Submit");
    submitButton.addActionListener(l -> openForm());
    panel.add(submitButton);
  }

  /**
   * Method to get the selected choice in order to take further actions.
   */
  private String getSelectedChoice() {
    for (Enumeration<AbstractButton> buttons = choices.getElements(); buttons.hasMoreElements(); ) {
      AbstractButton button = buttons.nextElement();
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return "";
  }

  @Override
  public void openForm(String[] options, String choice) {
    form = new VirtualGambleForm(choice, options);
    form.setFeature(features);
    form.setVisible(true);
  }

  /**
   * This function is used to open a new form based on the user inputs.
   */
  private void openForm() {
    String choice = getSelectedChoice();
    String[] options = new String[]{};
    String message = "";
    boolean isMessage = false;
    switch (choice) {
      case "Create Portfolio":
        options = new String[]{"PortfolioID"};
        break;
      case "Buy Share":
        options = new String[]{"PortfolioID", "Company Ticker", "Amount", "Date", "Commission"};
        break;
      case "Get Cost Basis of a portfolio":
        options = new String[]{"PortfolioID", "Date"};
        break;
      case "Get Total Value of a portfolio":
        options = new String[]{"PortfolioID", "Date"};
        break;
      case "List all portfolios":
        message = features.getData(choice).toString();
        isMessage = true;
        break;
      case "Add company to portfolio":
        options = new String[]{"PortfolioID", "Company Ticker"};
        break;
      case "Invest amount equally among companies":
        options = new String[]{"PortfolioID", "Amount", "Date", "Commission"};
        break;
      case "Invest amount weighted among companies":
        options = new String[]{"PortfolioID"};
        break;
      case "Apply dollar cost average equally among companies":
        options = new String[]{"PortfolioID", "Start Date", "End Date", "Amount", "Period",
          "Commission"};
        break;
      case "Apply dollar cost average weighted among companies":
        options = new String[]{"PortfolioID"};
        break;
      case "Save portfolio":
        options = new String[]{"PortfolioID"};
        break;
      case "Retrieve a portfolio":
        options = new String[]{"PortfolioID"};
        break;
      case "Save Strategy":
        options = new String[]{"PortfolioID", "Strategy Name"};
        break;
      case "Retrieve a Strategy":
        options = new String[]{"PortfolioID", "Strategy Name"};
        break;
      case "Generate the graph for performance of a portfolio over time":
        options = new String[]{"PortfolioID"};
        break;
      default:
        options = new String[]{};
    }
    if (!isMessage) {
      form = new VirtualGambleForm(choice, options);
      form.setFeature(features);
      form.setVisible(true);
    } else {
      form = new VirtualGambleForm(message);
      form.setVisible(true);
    }

  }

  @Override
  public void showMessage(String message) {
    form = new VirtualGambleForm(message);
    form.setVisible(true);
  }

  @Override
  public void setFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void showGraph() {
    Graph chart = new Graph(
            "Value Trend",
            "Portfolio Performance");
    chart.display();
  }

}
