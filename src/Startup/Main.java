package Startup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Models.Order;

public class Main {

	public static void main(String[] args) {
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
			
		AddOrder(BID, ASK, orderTestAsk1);
		AddOrder(BID, ASK, orderTestAsk2);
		AddOrder(BID, ASK, orderTestAsk3);
		AddOrder(BID, ASK, orderTestAsk4);
		AddOrder(BID, ASK, orderTestAsk5);
		AddOrder(BID, ASK, orderTestAsk6);
		
		AddOrder(BID, ASK, orderTestBid1);
		AddOrder(BID, ASK, orderTestBid2);
		AddOrder(BID, ASK, orderTestBid3);
		AddOrder(BID, ASK, orderTestBid4);
		AddOrder(BID, ASK, orderTestBid5);
		AddOrder(BID, ASK, orderTestBid6);
		
		System.out.println("BID LIST: " + BID);
		System.out.println("ASK LIST: " + ASK);
		PrintTable(BID);
		
		DeleteModifyOrder(BID,ASK,orderTestBid6.getId(),true,0);
		
		PrintTable(BID);
		PrintTable(ASK);
		
		DeleteModifyOrder(BID,ASK,orderTestAsk3.getId(),false,99);
		
		PrintTable(ASK);
		//ArrayList<LinkedList<Order>> BID = new ArrayList<LinkedList<Order>>();
		//ArrayList<LinkedList<Order>> ASK = new ArrayList<LinkedList<Order>>();

	}
	
	private static void AddOrder(HashMap<Double, LinkedList<Order>> BIDMap, HashMap<Double, LinkedList<Order>> ASKMap, Order order) {
		Double doubleObj = new Double(order.getPrice());
		LinkedList<Order> orderLinkedList = new LinkedList<>();
		
		if (order.isSide()) {
			
			if (!BIDMap.containsKey(doubleObj)) {
				orderLinkedList.add(order);
				BIDMap.put(doubleObj, orderLinkedList);
			}
			else {
				System.out.println("TestingBID" + BIDMap.get(doubleObj));
				BIDMap.get(doubleObj).add(order);
				System.out.println("TestingBID2" + BIDMap.get(doubleObj));
			}
		}
		else {
			if (!ASKMap.containsKey(doubleObj)) {
				orderLinkedList.add(order);
				ASKMap.put(doubleObj, orderLinkedList);
			}
			else {
				System.out.println("TestingASK" + ASKMap.get(doubleObj));
				ASKMap.get(doubleObj).add(order);
				System.out.println("TestingASK2" + ASKMap.get(doubleObj));
			}
		}
		
	}
	
	private static void DeleteModifyOrder(HashMap<Double, LinkedList<Order>> BIDMap, HashMap<Double, LinkedList<Order>> ASKMap, String orderId, boolean delete, int quantity) {
		int changed = 0;
		double modifiedKey = 0;
		Order tempOrder = new Order();
		
		for (Map.Entry<Double, LinkedList<Order>> entry : BIDMap.entrySet()) {
			for (Order order: entry.getValue()) {
				if(order.getId().equalsIgnoreCase(orderId)) {
					if (!delete) {
						tempOrder = order;
						tempOrder.setQuantity(quantity);
						modifiedKey = entry.getKey();
					}
					entry.getValue().remove(order);
					changed =1;
				}
			}
		}
		if (modifiedKey != 0)
			BIDMap.get(modifiedKey).add(tempOrder);
				
		if(changed ==0){
			for (Map.Entry<Double, LinkedList<Order>> entry : ASKMap.entrySet()) {
				for (Order order: entry.getValue()) {
					if(order.getId().equalsIgnoreCase(orderId)) {
						if (!delete) {
							tempOrder = order;
							tempOrder.setQuantity(quantity);
							modifiedKey = entry.getKey();
						}
						entry.getValue().remove(order);
						changed =1;
					}
				}
			}
			if (modifiedKey != 0)
				ASKMap.get(modifiedKey).add(tempOrder);
		}

		
	}
	
	private static void PrintTable(HashMap<Double, LinkedList<Order>> map) {
		System.out.println("Table Format: ");
		for (Map.Entry<Double, LinkedList<Order>> entry : map.entrySet()) {
			System.out.print(entry.getKey() + ": ");
			for (Order order: entry.getValue()) {
				System.out.print(order.getQuantity() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
