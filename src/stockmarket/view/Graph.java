package stockmarket.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;

/**
 * This class represents a Date vs value graph.
 */
public class Graph extends JFrame {

  /**
   * Constructor to construct the graph.
   *
   * @param applicationTitle title of the application
   * @param chartTitle       title of the chart
   */
  public Graph(String applicationTitle, String chartTitle) {
    super(applicationTitle);
    JFreeChart lineChart = ChartFactory.createLineChart(
            chartTitle,
            "Date", "Total Value",
            createDataset(),
            PlotOrientation.VERTICAL,
            true, true, false);

    ChartPanel chartPanel = new ChartPanel(lineChart);
    chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
    setContentPane(chartPanel);

  }

  /**
   * Helper method to get the  data set to create graph.
   *
   * @return data set to create  graph
   */
  private DefaultCategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get("temp/graphdata.csv")));
    } catch (IOException e) {
      throw new IllegalArgumentException("Stock data not available for company");
    }

    String[] tmpdata = data.split("\n");
    for (String str : tmpdata) {
      System.out.println("---");
      System.out.println(str);
      String date = str.split(",")[0];
      double value = Double.parseDouble(str.split(",")[1]);
      dataset.addValue(value, "Total Value", date);
    }
    return dataset;
  }


  /**
   * Method to display the graph after it has been constructed.
   */
  public void display() {
    this.pack();
    RefineryUtilities.centerFrameOnScreen(this);
    this.setVisible(true);
  }
}