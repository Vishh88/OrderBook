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
				IterateRemoveLogic(bidMap, order, tmpOrder);
			} else { // If it is a buy limit order we subtract from the ask side
				if (askMap.isEmpty()) {
					System.out.println("There are no more sell orders to buy.");
					return false;
				}
				IterateRemoveLogic(askMap, order, tmpOrder);
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

	private void IterateRemoveLogic(TreeMap<Double, LinkedList<Order>> map, Order order, Order tmpOrder) {
		Iterator<Map.Entry<Double, LinkedList<Order>>> mapIterator = map.entrySet().iterator();
		while (mapIterator.hasNext()) {
			if (order.getPrice() >= map.firstKey()) {
				Map.Entry<Double, LinkedList<Order>> tmpMap = mapIterator.next();
				ListIterator<Order> llIterator = tmpMap.getValue().listIterator();
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
}
