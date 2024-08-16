package logic;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import generators.OrderGenerator;
import models.Order;

public class Operations {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public void AddOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, Order order) {
		Double doubleObj = new Double(order.getPrice());
		LinkedList<Order> orderLinkedList = new LinkedList<>();
		lock.writeLock().lock();
		try {
			if (order.isSide()) {
				if (!bidMap.containsKey(doubleObj)) {
					orderLinkedList.add(order);
					bidMap.put(doubleObj, orderLinkedList);
				} else {
					bidMap.get(doubleObj).add(order);
				}
			} else {
				if (!askMap.containsKey(doubleObj)) {
					orderLinkedList.add(order);
					askMap.put(doubleObj, orderLinkedList);
				} else {
					askMap.get(doubleObj).add(order);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	
	public boolean DeleteOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, String orderId) {
		boolean deleted = false;
		lock.writeLock().lock();
		try {
			for (Map.Entry<Double, LinkedList<Order>> entry : bidMap.entrySet()) {
				for (Order order : entry.getValue()) {
					if (order.getId().equalsIgnoreCase(orderId)) {
						entry.getValue().remove(order);
						deleted = true;
					}
				}
			}// removes all objects from the map with empty values
			bidMap.values().remove(new LinkedList<>());

			if (!deleted) {
				for (Map.Entry<Double, LinkedList<Order>> entry : askMap.entrySet()) {
					for (Order order : entry.getValue()) {
						if (order.getId().equalsIgnoreCase(orderId)) {
							entry.getValue().remove(order);
							deleted = true;
						}
					}
				}// removes all objects from the map with empty values
				askMap.values().remove(new LinkedList<>());
			}
		} finally {
			lock.writeLock().unlock();
		}
		return deleted;
		
	}

	public boolean ModifyOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, String orderId, int newQuantity) {
		double modifiedKey = 0;
		Order tempOrder = new Order();
		lock.writeLock().lock();
		try {
			for (Map.Entry<Double, LinkedList<Order>> entry : bidMap.entrySet()) {
				for (Order order : entry.getValue()) {
					if (order.getId().equalsIgnoreCase(orderId)) {
						tempOrder = order;
						tempOrder.setQuantity(newQuantity);
						modifiedKey = entry.getKey();
						entry.getValue().remove(order);
					}
				}
			}
			if (modifiedKey != 0) {
				bidMap.get(modifiedKey).addLast(tempOrder);
			}// removes all objects from the map with empty values
			bidMap.values().remove(new LinkedList<>());

			if (modifiedKey != 0) {
				for (Map.Entry<Double, LinkedList<Order>> entry : askMap.entrySet()) {
					for (Order order : entry.getValue()) {
						if (order.getId() == orderId) {
							tempOrder = order;
							tempOrder.setQuantity(newQuantity);
							modifiedKey = entry.getKey();
							entry.getValue().remove(order);
						}
					}
				}
				if (modifiedKey != 0) {
					askMap.get(modifiedKey).add(tempOrder);
				}// removes all objects from the map with empty values
				askMap.values().remove(new LinkedList<>());
			}
		} finally {
			lock.writeLock().unlock();
		}// checking if any order was modified, then send a successful response back, else failure response back
		if (modifiedKey == 0) {
			return false;
		}
		else {
			return true; 
		}
	}

	public void PrintPrice(TreeMap<Double, LinkedList<Order>> map) {
		System.out.format("+---------+----------------------------------------------------+%n");
		System.out.format("| Price   | Quantity  HighestPriority -----> LowestPriority    |%n");
		System.out.format("+---------+----------------------------------------------------+%n");
		String leftAlignment = "| %-7.2f |";
		for (Map.Entry<Double, LinkedList<Order>> entry : map.entrySet()) {
			System.out.format(leftAlignment, entry.getKey());
			for (Order order : entry.getValue()) {
				System.out.print(order.getQuantity() + "  ");
				
			}
			System.out.println();
		}
		System.out.println();
	}

	public LinkedList<Order> Search(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, boolean side, double price) {
		if (side) {
			return bidMap.get(price);
		} else { 
			return askMap.get(price);
		} 
	}

	public void PrintData(LinkedList<Order> list) {
		if (list.isEmpty()) {
			System.out.println("List is empty, nothing to print.");
		} else {
			System.out.println("Printing searched orders for price: " + list.get(0).getPrice());
			System.out.println("in descending priority order. ");
			for (Order order : list) {
				System.out.println();
				System.out.println(order);
			}
		}
	}
	
	public void PrintData(TreeMap<Double, LinkedList<Order>> map) {
		if (map.firstEntry().getValue().getFirst().isSide()) {
			System.out.println("BID");
		}
		else {
			System.out.println("\nASK");
		}
		System.out.format("+---------+----------------------------------------------------+%n");
		System.out.format("| Price   | Orders                                             |%n");
		System.out.format("+---------+----------------------------------------------------+%n");
		String leftAlignment = "| %-7s | %-7.100s |%n";
		for (Map.Entry<Double, LinkedList<Order>> entry : map.entrySet()) {
			for (Order order : entry.getValue()) {
				System.out.format(leftAlignment, entry.getKey().toString(), order.toString());
			}
		}
	}

	public void LoadTestData(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap) {
		OrderGenerator orderGenerator = new OrderGenerator();
		System.out.println("Generating Orders...");
		for (int i = 0; i < 10; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateRandomOrder());
		}

		for (int i = 0; i < 5; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
		}
		askMap.values().remove(new LinkedList<>());
		bidMap.values().remove(new LinkedList<>());
	}


	public void GenerateOrders(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, int bidCount, int askCount) {
		OrderGenerator orderGenerator = new OrderGenerator();
		for (int i=0; i<bidCount; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateSameSideOrder(true));
		}
		
		for (int i=0; i<askCount; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateSameSideOrder(false));
		}
		
	}
}
