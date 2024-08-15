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
	
	
	public void DeleteOrder(TreeMap<Double, LinkedList<Order>> bidMap,
			TreeMap<Double, LinkedList<Order>> askMap, long orderId, boolean delete, int quantity) {
		
	}

	public void ModifyOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, long orderId, int newQuantity) {
		double modifiedKey = 0;
		Order tempOrder = new Order();
		lock.writeLock().lock();
		try {
			for (Map.Entry<Double, LinkedList<Order>> entry : bidMap.entrySet()) {
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
				bidMap.get(modifiedKey).addLast(tempOrder);
			}
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
				}
				askMap.values().remove(new LinkedList<>());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void PrintPrice(TreeMap<Double, LinkedList<Order>> map) {
		
		for (Map.Entry<Double, LinkedList<Order>> entry : map.entrySet()) {
			System.out.print(entry.getKey() + ": ");
			for (Order order : entry.getValue()) {
				System.out.print(order.getQuantity() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public LinkedList<Order> Search(TreeMap<Double, LinkedList<Order>> bidMap,
			TreeMap<Double, LinkedList<Order>> askMap, String side, double price) {

		if (side.equalsIgnoreCase("Buy")) {
			return bidMap.get(price);
		} else if (side.equalsIgnoreCase("Sell")) {
			return askMap.get(price);
		} else {
			return new LinkedList<>();
		}

	}

	public void PrintList(LinkedList<Order> list) {
		if (list.isEmpty()) {
			System.out.println("List is empty, nothing to print.");
		} else {
			System.out.println("Printing Searched orders for price: " + list.get(0).getPrice());
			System.out.println("in priority order. ");
			for (Order order : list) {
				System.out.println();
				System.out.println(order);
			}
		}
	}
	
	public void PrintMap() {
		
	}

	public void LoadTestData(TreeMap<Double, LinkedList<Order>> bidMap,
			TreeMap<Double, LinkedList<Order>> askMap) {
		OrderGenerator orderGenerator = new OrderGenerator();
		System.out.println("Generating Orders...");
		for (int i = 0; i < 10; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateRandomOrder());
		}

		for (int i = 0; i < 5; i++) {
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
		}
	}
}
