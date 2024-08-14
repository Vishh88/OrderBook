package logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import models.Order;

public class Operations {

	public static void AddOrder(HashMap<Double, LinkedList<Order>> BIDMap, HashMap<Double, LinkedList<Order>> ASKMap, Order order) {
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
	
	
	public static void DeleteModifyOrder(HashMap<Double, LinkedList<Order>> BIDMap, HashMap<Double, LinkedList<Order>> ASKMap, String orderId, boolean delete, int quantity) {
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
	
	
	public static void PrintTable(HashMap<Double, LinkedList<Order>> map) {
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
	
	public static void Search() {
		
	}
}
