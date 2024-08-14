package startup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import logic.Operations;
import models.Order;

public class Main {

	public static void main(String[] args) {
		Operations operation = new Operations(); 
		HashMap<Double,LinkedList<Order>> BID = new HashMap<Double,LinkedList<Order>>();
		HashMap<Double,LinkedList<Order>> ASK = new HashMap<Double,LinkedList<Order>>();
		
		Order orderTestBid1 = new Order(13.45, 16, true);
		Order orderTestBid2 = new Order(13.41, 30, true);
		Order orderTestBid3 = new Order(13.35, 435000, true);
		Order orderTestBid4 = new Order(13.34, 30, true);
		Order orderTestBid5 = new Order(13.20, 1080, true);
		Order orderTestBid6 = new Order(13.45, 10, true);

		
		Order orderTestAsk1 = new Order(13.80, 20, false);
		Order orderTestAsk2 = new Order(13.83, 60, false);
		Order orderTestAsk3 = new Order(13.87, 27, false);
		Order orderTestAsk4 = new Order(13.90, 300, false);
		Order orderTestAsk5 = new Order(13.91, 30, false);
		Order orderTestAsk6 = new Order(13.87, 10, false);		
		
		
		//Iterator<Double> itrBid = BID.keySet().iterator();
		//Iterator<Double> itrAsk = ASK.keySet().iterator();
			
		operation.AddOrder(BID, ASK, orderTestAsk1);
		operation.AddOrder(BID, ASK, orderTestAsk2);
		operation.AddOrder(BID, ASK, orderTestAsk3);
		operation.AddOrder(BID, ASK, orderTestAsk4);
		operation.AddOrder(BID, ASK, orderTestAsk5);
		operation.AddOrder(BID, ASK, orderTestAsk6);
		
		operation.AddOrder(BID, ASK, orderTestBid1);
		operation.AddOrder(BID, ASK, orderTestBid2);
		operation.AddOrder(BID, ASK, orderTestBid3);
		operation.AddOrder(BID, ASK, orderTestBid4);
		operation.AddOrder(BID, ASK, orderTestBid5);
		operation.AddOrder(BID, ASK, orderTestBid6);
		
		System.out.println("BID LIST: " + BID);
		System.out.println("ASK LIST: " + ASK);
		operation.PrintTable(BID);
		
		operation.DeleteModifyOrder(BID,ASK,orderTestBid6.getId(),true,0);
		
		operation.PrintTable(BID);
		operation.PrintTable(ASK);
		
		operation.DeleteModifyOrder(BID,ASK,orderTestAsk3.getId(),false,99);
		
		operation.PrintTable(ASK);
		//ArrayList<LinkedList<Order>> BID = new ArrayList<LinkedList<Order>>();
		//ArrayList<LinkedList<Order>> ASK = new ArrayList<LinkedList<Order>>();

	}
	
}
