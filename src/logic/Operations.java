package logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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
		lock.writeLock().lock(); //write lock thread so that other thread won't be able to update while this thread is updating
		try {
			if (order.isSide()) { //check if order is a bid order before adding it to the BID TreeMap
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
		} finally { //write unlock thread so that the TreeMaps  can be updated by any thread
			lock.writeLock().unlock();
		}
	}
	
	
	public boolean DeleteOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, String orderId) {
		boolean deleted = false;
		lock.writeLock().lock();
		try {
			if (orderId.startsWith("BID")) {
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = bidMap.entrySet().iterator();
				while(mapIterator.hasNext()) {
					Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
					ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
					while(llIterator.hasNext()) {
						Order iteratorOrder = llIterator.next(); 
						if(iteratorOrder.getId().equalsIgnoreCase(orderId)) {
							llIterator.remove();
							deleted = true;
						}
					}
				}
				bidMap.values().remove(new LinkedList<>());
			}

			if (orderId.startsWith("ASK")) {
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = askMap.entrySet().iterator();
				while(mapIterator.hasNext()) {
					Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
					ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
					while(llIterator.hasNext()) {
						Order iteratorOrder = llIterator.next(); 
						if(iteratorOrder.getId().equalsIgnoreCase(orderId)) {
							llIterator.remove();
							deleted = true;
						}
					}
				}
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
			if (orderId.startsWith("BID")) {
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = bidMap.entrySet().iterator();
				while(mapIterator.hasNext()) {
					Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
					ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
					while(llIterator.hasNext()) {
						Order iteratorOrder = llIterator.next(); 
						if(iteratorOrder.getId().equalsIgnoreCase(orderId)) {
							tempOrder = iteratorOrder;
							tempOrder.setQuantity(newQuantity);
							modifiedKey = tmpMap.getKey();
							llIterator.remove();
						}
					}
				}
				if (modifiedKey != 0) {
					bidMap.get(modifiedKey).addLast(tempOrder);
				} // removes all objects from the map with empty values
				bidMap.values().remove(new LinkedList<>());
			}
			
			

			if (orderId.startsWith("ASK")) {
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = askMap.entrySet().iterator();
				while(mapIterator.hasNext()) {
					Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
					ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
					while(llIterator.hasNext()) {
						Order iteratorOrder = llIterator.next(); 
						if(iteratorOrder.getId().equalsIgnoreCase(orderId)) {
							tempOrder = iteratorOrder;
							tempOrder.setQuantity(newQuantity);
							modifiedKey = tmpMap.getKey();
							llIterator.remove();
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

	public LinkedList<Order> SearchPrice(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, boolean side, double price) {
		if (side) {
			return bidMap.get(price);
		} else { 
			return askMap.get(price);
		} 
	}
	
}
