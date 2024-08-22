package logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import models.Order;

public class MatchingEngine {
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	public MatchingEngine() {
	}
	
	public boolean ExecuteOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, Order order) {
		Order tmpOrder = new Order();
		lock.writeLock().lock(); // write lock thread so that other thread won't be able to update while this
									// thread is updating
		try {
			if (!order.isSide()) { // If it is a sell limit order we subtract from the bid side
				if (bidMap.isEmpty()) {
					System.out.println("There are no more buy orders to sell.");
					return false;
				}
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = bidMap.entrySet().iterator();
				while (mapIterator.hasNext()) {
					if (order.getPrice() <= bidMap.firstKey()) { //checking if the new order price is less than the highest price BID, if not, then the executed order does not exist
						Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
						ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
						while (llIterator.hasNext()) {
							tmpOrder = llIterator.next();
							int newQuantity = order.getQuantity() - tmpOrder.getQuantity(); //sets the new quantity
							if (newQuantity == 0) { //checks if the new quantity is zero, means it perfectly matched with an existing order
								llIterator.remove(); 
								break; // we should break out of the loop here, it matched perfectly so there is no need to go to another order.
							} else if (newQuantity > 0) { //checks if the new quantity is greater than zero means that the execute order has a greater quantity and we need to check the next order as well.  
								llIterator.remove(); 
								order.setQuantity(newQuantity);
							} else { //else if the new quantity is less than zero, means the current order has more quantity than what is left from the execute order.  We should break out of the loop here
								newQuantity = newQuantity * (-1); // makes the new quantity positive and assigns it to the current order. 
								tmpOrder.setQuantity(newQuantity); // this is updating the order and not modifying it, thus the order priority does not change
								break;
							}
						}
						if (tmpMap.getValue().isEmpty()) {
							mapIterator.remove();
						}
					} else {
						break;
					}
				}
			} else { // If it is a buy limit order we subtract from the ask side
				if (askMap.isEmpty()) {
					System.out.println("There are no more sell orders to buy.");
					return false;
				}
				Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = askMap.entrySet().iterator();
				while (mapIterator.hasNext()) {
					if (order.getPrice() >= askMap.firstKey()) {//checking if the new order price is greater than the lowest price ASK, if not, then the executed order does not exist
						Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
						ListIterator<Order> llIterator = tmpMap.getValue().listIterator(); // same comments above will be used for the below iterations
						while (llIterator.hasNext()) {
							tmpOrder = llIterator.next();
							int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
							if (newQuantity == 0) {
								llIterator.remove();
								break;
							} else if (newQuantity > 0) {
								llIterator.remove();
								order.setQuantity(newQuantity);
							} else {
								newQuantity = newQuantity * (-1);
								tmpOrder.setQuantity(newQuantity);
								break;
							}
						}
						if (tmpMap.getValue().isEmpty()) {
							mapIterator.remove();
						}
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally { // write unlock thread so that the TreeMaps can be updated by any thread
			bidMap.values().remove(new LinkedList<>());
			askMap.values().remove(new LinkedList<>());
			lock.writeLock().unlock();
		}
		return true;
	}
}
