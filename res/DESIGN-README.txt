The Virtual Gambling application helps users who are new to investing to learn about how money could grow, in the style of “virtual gambling”. Similar to virtual gambling, our application will not use real money and investment. The previous version of the application allows the user to perform following operations in Command line interface:

•	Create one or more portfolios and examine its composition.
•	Buy shares of some stock in a portfolio worth a certain amount at a certain date.
•	Determine the total cost basis and total value of a portfolio at a certain date.
•	Get a list of all portfolios created by the user.
•	Invest an amount equally between companies or based on weights
•	Invest using dollar cost averaging

The following features has been added to the application:-
•	Persisting a portfolio into a file
•	Retrieving a porfolio from the file
•	Persisting a dollar cost strategy into a file
•	Retrieving a dollar cost strategy from the file
•	Support for GUI alongside CLI to perform above functionalities
•	Generate a graph which shows performance of the portfolio over time




The design of this application is based on the Model View Controller (MVC) style. The design consists of three important modules based on MVC.

Model: The model Interface used here is named as VirtualGamble. The class implementing this interface implements all the functionalities of this application mentioned above. The model is the central component of the pattern. It is the application's dynamic data structure, independent of the user interface. It directly manages the data, logic and rules of the application.


Controller: 
a) CLI 
The controller interface used here is named as VirtualGambleController. The class VirtualGambleControllerImpl implements this interface and defines the controller functionalities. The controller accepts input and converts it to commands for the model or view.
The controller responds to the user input and performs interactions on the VirtualGamble model objects. The controller receives the input, optionally validates it and then passes the input to the model. It then sends the response received from the model to the view to display it to the user.

The VirtualGamble takes the portfolio name, company ticker name, date and amount(to buy shares) as input to pass to the model.The controller does an input validation of these parameters before passing them to the model based on the following measures:

- The portfolio name must only contain numbers and alphabets. Any special characters are not permitted.
- The company ticker name must contain only letters and must be of four characters.
- The date has to be provided in the format YYYY-MM-DD.
- The amount must be a valid positive number.

b) GUI- 
The controller of the GUI is responsible for taking the processing the inputs obtained from the GUI. It performs all the functions which is provided by the CLI controller and is defined as VirtualGambleControllerGUI.

View: 
a)CLI 
The view is responsible for displaying the response of the system to the user. Our view interface VirtualGambleView contains a print method which displays the system response passed to it via the controller.

b) GUI-
The GUI starts with a home screen consisting of following radio buttons corresponding to each functionality
1. Create Portfolio
2. Get Cost Basis of a portfolio
3. Get Total Value of a portfolio
4. List all portfolios
5. Add company to portfolio
7. Invest amount weighted among companies
8. Apply dollar cost average equally among companies
9. Apply dollar cost average weighted among companies

On selecting one of the options and clicking submit button, either a form appears to get user inputs, e.g. in case of Create Portfolio, a form asking for portfolioID appears, or a window with the asked information appears, e.g. on selecting "List all portfolio", a list of all portfolios is displayed on a new window.

The form page contains labels and text boxes. On entering the inputs and pressing submit button, control is transferred to Controller where the inputs are processed.

Main Class : The class VirtualGambleMain is responsible for the execution of this application. The main method initializes the MVC objects and passes the model and view to controller, and
then calls the startUp() method of controller which starts up the application.


Our design makes use of some classes which act as Composite data types being used by the MVC classes:

1)	Portfolio:  The portfolio class represents a Portfolio which is created by the user. The portfolio is a combination of several company stocks. The portfolio contains a list of stocks.

2)	Stock: The stock class represents a stock which has been bought by the user. The stock has a purchase date, the no. of shares it consists of, the cost basis (the price at which it has been bought) and the company ticker which represents the name of the company to which the share belongs to.


This application uses data from the Alpha Vantage API. The application also caches a data to minimize the number of API calls and increase the efficiency by decreasing latency of the transactions. This is done by persisting the data corresponding to a company when a share is bought for that company for the first time. The subsequent purchase or value calculations makes use of this cached data.











