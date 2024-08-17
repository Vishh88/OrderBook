package logic;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import models.Order;

public class MatchingEngine {
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public MatchingEngine() {
	}
	
	public void ExecuteOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, Order order) {
		Double doubleObj = new Double(order.getPrice());
		LinkedList<Order> orderLinkedList = new LinkedList<>();
		lock.writeLock().lock(); //write lock thread so that other thread won't be able to update while this thread is updating
		try {
			if (!order.isSide()) { //If it is a sell limit order we subtract from the bid side
				if (!bidMap.containsKey(doubleObj)) {
					
					orderLinkedList.add(order);
					bidMap.put(doubleObj, orderLinkedList);
				} else {
					bidMap.get(doubleObj).add(order);
				}
			} else { //If it is a buy limit order we subtract from the ask side
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

}
