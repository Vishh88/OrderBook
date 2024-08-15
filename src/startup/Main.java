package startup;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;
import logic.Operations;
import models.Menu;
import models.Order;

public class Main implements Runnable{

	private Operations operations = new Operations();
	public static void main(String[] args) {
		Main obj = new Main();
		Thread thread = new Thread(obj);
		thread.start();
	}
	
	@Override
	public void run() {
		try (Scanner in = new Scanner(System.in)) {
			TreeMap<Double, LinkedList<Order>> bid = new TreeMap<Double, LinkedList<Order>>();
			TreeMap<Double, LinkedList<Order>> ask = new TreeMap<Double, LinkedList<Order>>();
			Menu menu = new Menu();
			menu.setTitle("Simulation Menu");
			menu.setInstructions("Please choose from the menu below \nEnter the content number and hit enter: ");
			menu.setUnderlying("********************************************************************************************");
			String[] content = new String[7];
			content[0] = "End Simulation";
			content[1] = "Add Order";
			content[2] = "Modify Order";
			content[3] = "Delete Order";
			content[4] = "Modify Order";
			content[5] = "Search Price";
			content[6] = "";
			menu.setMenuItems(content);
			
			
			int choice = 0;
			while (choice != 4) {
				System.out.println();
				System.out.println(menu.getTitle());
				System.out.println(menu.getInstructions());
				System.out.println(menu.getUnderlying());
				for(int i = 0; i < menu.getMenuItems().length; i++) {
					System.out.println(i + " - " + menu.getMenuItems()[i]);
				}
				System.out.println(menu.getUnderlying());
				
				/*
				 * System.out.println(); System.out.println("Simulation Menu");
				 * System.out.println("Please choose from the menu below and hit enter: ");
				 * System.out.println(
				 * "********************************************************************************************"
				 * ); System.out.println("1 - Add order");
				 * System.out.println("2 - Load test data");
				 * System.out.println("3 - Modify order"); System.out.println("4 - Search");
				 * System.out.println("5 - View BID orders");
				 * System.out.println("6 - View ASK orders");
				 * System.out.println("0 - End Simulation"); System.out.println(
				 * "********************************************************************************************"
				 * );
				 */

				choice = in.nextInt();

				switch (choice) {
				case 1:
					Order newOrder = new Order();
					System.out.println("Enter order price: ");
					newOrder.setPrice(in.nextDouble());
					System.out.println("Enter order quantity: ");
					newOrder.setQuantity(in.nextInt());
					System.out.println("Is this a buy order?");
					System.out.println("Enter True or False");
					newOrder.setSide(in.nextBoolean());
					System.out.println("Storing " + newOrder);
					operations.AddOrder(bid, ask, newOrder);
					break;
				case 2:
					operations.LoadTestData(bid, ask);
					RunTest(bid, ask);
					break;
				case 3:
					operations.ModifyOrder(bid, ask, 23, 100);
					break;
				case 4:
					operations.PrintList(operations.Search(bid, ask, "buy", 3.0));
					break;
				case 5:
					operations.PrintPrice(bid);
					break;
				case 6:
					operations.PrintPrice(ask);
					break;
				case 0:
					System.out.println("Simulation is ending...");
					System.out.println("Simulation has ended.  GoodBye.");
					System.exit(0);
					break;
				default:
					break;
				}
			}
		}
		
	}
	
	public void RunTest(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap) {
		//LinkedList<Order> searchResult = new LinkedList<>();
		
		operations.LoadTestData(bidMap, askMap);

		System.out.println("\nbid list: \n" + bidMap);
		System.out.println("\nask list: \n" + askMap);
		operations.PrintPrice(bidMap);
		operations.PrintPrice(askMap);

		System.out.println("Deleting bid");
		// Operations.DeleteModifyOrder(bid,ask,orderTestbid5.getId(),true,0);
		// Operations.DeleteModifyOrder(bid,ask,orderTestbid4.getId(),true,0);

		operations.PrintPrice(bidMap);
		operations.PrintPrice(askMap);

		// Operations.DeleteModifyOrder(bid,ask,orderTestask3.getId(),false,99);

		operations.PrintPrice(askMap);

		//searchResult = Operations.Search(bidMap, askMap, "buy", 13.45);
		//Operations.PrintList(searchResult);
	}
}
