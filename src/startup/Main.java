package startup;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

import generators.TestData;
import logic.Displays;
import logic.MatchingEngine;
import logic.Operations;
import models.Menu;
import models.Order;

public class Main implements Runnable {

	private Operations operations = new Operations();
	private Displays display = new Displays();
	private TestData testData = new TestData();
	private MatchingEngine matchingEngine = new MatchingEngine();
	private TreeMap<Double, LinkedList<Order>> bid = new TreeMap<Double, LinkedList<Order>>();
	private TreeMap<Double, LinkedList<Order>> ask = new TreeMap<Double, LinkedList<Order>>();

	public static void main(String[] args) {
		Main obj = new Main();
		Thread thread = new Thread(obj); 
		thread.start(); // Creating a new instance of the order book on a different thread
	}

	@Override
	public void run() {
		try (Scanner in = new Scanner(System.in)) {
			String orderId = "";
			int quantity, bidCount, askCount = 0;
			boolean side = false;
			double price = 0;
			boolean success = false;
			Menu menu = new Menu();
			menu.setTitle("Simulation Menu");
			menu.setInstructions("Please choose from the menu below \nEnter the content number and hit enter: ");
			menu.setUnderlying(
					"********************************************************************************************");
			String[] content = new String[11];
			content[0] = "Exit Application!";
			content[1] = "Load Test Data";
			content[2] = "Display All Orders";
			content[3] = "Add Order";
			content[4] = "Generate Orders (bulk)";
			content[5] = "Modify Order";
			content[6] = "Delete Order";
			content[7] = "Search Price Level";
			content[8] = "BID Price - Quantity";
			content[9] = "ASK Price - Quantity";
			content[10] = "Execute An Order";
			menu.setMenuItems(content);

			int choice = 0;
			do {
				try {
					System.out.println();
					System.out.println(menu.getTitle());
					System.out.println(menu.getInstructions());
					System.out.println(menu.getUnderlying());
					for (int i = 0; i < menu.getMenuItems().length; i++) {
						System.out.println(i + " - " + menu.getMenuItems()[i]);
					}
					System.out.println(menu.getUnderlying());

					choice = in.nextInt();

					switch (choice) {
					case 1:
						testData.LoadTestData(bid, ask);
						Thread.sleep(1000);
						System.out.println("Test data loaded successfully.");
						break;
					case 2:
						if (!bid.isEmpty()) {
							display.PrintData(bid);
						}			
						
						if (!ask.isEmpty()) {
							display.PrintData(ask);
						}
						
						if(bid.isEmpty() && ask.isEmpty()) {
							System.out.println("No orders have been created.  \nPlease load test data or create new orders and try again.");
						}
						Thread.sleep(1000);
						break;
					case 3: //Add order
						Order newOrder = new Order();
						System.out.println("Enter order price: ");
						newOrder.setPrice(in.nextDouble());
						System.out.println("Enter order quantity: ");
						newOrder.setQuantity(in.nextInt());
						System.out.println("Is this a buy order?");
						System.out.println("Enter True or False");
						newOrder.setSide(in.nextBoolean());
						operations.AddOrder(bid, ask, newOrder);
						System.out.println("Order " + newOrder + " added successfully.");
						Thread.sleep(1000);
						break;
					case 4:
						System.out.println("Please enter how many buy orders you would like to generate:");
						bidCount = in.nextInt();
						System.out.println("Please enter how many sell orders you would like to generate:");
						askCount = in.nextInt();
						testData.GenerateOrders(bid, ask, bidCount, askCount);
						System.out.println("Bulk orders generated successfully");
						Thread.sleep(1000);
						break;
					case 5:
						System.out.println("Enter the order Id: ");
						orderId = in.next();
						System.out.println("Enter the new quantity for this order: ");
						quantity = in.nextInt();
						if (operations.ModifyOrder(bid, ask, orderId, quantity)) {
							System.out.println("Order " + orderId + " was successfully updated.");
						}
						else {
							System.out.println("Order " + orderId + " was not found.  \nPlease check the orderId and try again.");
						}
						Thread.sleep(1000);
						break;
					case 6:
						System.out.println("Enter the order Id: ");
						orderId = in.next();
						if (operations.DeleteOrder(bid, ask, orderId)) {
							System.out.println("Order " + orderId + " was successfully deleted.");
						}
						else {
							System.out.println("Order " + orderId + " was not found.  \nPlease check the orderId and try again.");
						}
						Thread.sleep(1000);
						break;
					case 7:
						System.out.println("Is this a buy order?");
						System.out.println("Enter True or False");
						side = in.nextBoolean();
						System.out.println("Enter the price to search: ");
						price = in.nextDouble();
						display.PrintData(operations.SearchPrice(bid, ask, side, price));
						Thread.sleep(1000);
						break;
					case 8:
						display.PrintPrice(bid);
						Thread.sleep(1000);
						break;
					case 9:
						display.PrintPrice(ask);
						Thread.sleep(1000);
						break;
					case 10:
						Order executeOrder = new Order();
						System.out.println("Enter order price: ");
						executeOrder.setPrice(in.nextDouble());
						System.out.println("Enter order quantity: ");
						executeOrder.setQuantity(in.nextInt());
						System.out.println("Is this a buy order?");
						System.out.println("Enter True or False");
						executeOrder.setSide(in.nextBoolean());
						if(matchingEngine.ExecuteOrder(bid, ask, executeOrder)) {
							System.out.println("Execution of order " +executeOrder + " \nwas successfully.");
						}
						else {
							System.out.println("Error Executing order, please try again.");
						}							
						Thread.sleep(1000);
						break;
					case 0:
						System.out.println("Application is closing...");
						System.out.println("GoodBye.");
						System.exit(0);
						break;
					default:
						System.out.println("The choice you have made is not part of the list. ");
						System.out.println("Please select a content number from the list provided. ");
						Thread.sleep(1000);
						break;
					}
				} catch (InputMismatchException e) {
					System.out.println("The input you have entered is invalid.  \nPlease restart the application and try again.");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (choice != 0);
		}
	}
}
