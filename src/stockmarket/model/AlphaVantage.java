package stockmarket.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class provides access to Alphavantage api to fetch the stock details.
 */
public class AlphaVantage {
  static final String[] keys = {"V5BH0KQTOT4L73C8", "D3DLDO843OQ797XN", "W1PZAPGJY2CGZQSQ",
      "D7TN3XS42MKN7S7U", "W0M1JOKC82EZEQA8"
  };
  static int keyIndex = 0;

  static int count = 0;

  /**
   * Method to get the stock data for given company.
   *
   * @param stockSymbol ticker symbol of the company for which stock data is to be obtained
   * @return the complete stock data available till date
   */
  public static String getStockData(String stockSymbol) {
    {
      String data = "";

      //the API key needed to use this web service.
      //Please get your own free API key here: https://www.alphavantage.co/
      //Please look at documentation here: https://www.alphavantage.co/documentation/
      String apiKey = keys[keyIndex];
      System.out.println("Using key " + apiKey + " at index " + keyIndex);
      URL url = null;

      try {
        url = new URL("https://www.alphavantage"
                + ".co/query?function=TIME_SERIES_DAILY"
                + "&outputsize=full"
                + "&symbol"
                + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("the alphavantage API has either changed or "
                + "no longer works");
      }

      InputStream in = null;
      StringBuilder output = new StringBuilder();

      try {
        in = url.openStream();
        int b;

        while ((b = in.read()) != -1) {
          output.append((char) b);
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("No price data found for " + stockSymbol);
      }
      data = output.toString();
      count++;
      if (count == 5) {
        keyIndex = (keyIndex + 1) % keys.length;
        count = 0;
      }
      if (data.contains("Our standard API call frequency is 5 \" +\n" +
              "              \"calls per minute and 500 calls per day")) {
        System.out.println("--start--");
        System.out.println("calls exceeded...");
        keyIndex = (keyIndex + 1) % keys.length;
        count = 0;
        data = getStockData(stockSymbol);
      }
      if (data.contains("Invalid API call")) {
        System.out.println(url);
        throw new IllegalArgumentException("Invalid company name");
      }
      return data;
    }
  }
}
