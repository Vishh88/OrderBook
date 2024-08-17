package logic;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import models.Order;

public class MatchingEngine {
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Operations operation = new Operations();

	public MatchingEngine() {
	}
	
	public boolean ExecuteOrder(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, Order order) {
		Double doubleObj = new Double(order.getPrice());
		Order tmpOrder = new Order();
		lock.writeLock().lock(); //write lock thread so that other thread won't be able to update while this thread is updating
		try {
			if (!order.isSide()) { //If it is a sell limit order we subtract from the bid side
				if (!bidMap.containsKey(doubleObj)) {
					while(order.getPrice() > bidMap.firstKey()) {
						tmpOrder = bidMap.firstEntry().getValue().element();
						int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
						if(newQuantity > 0) {
							operation.DeleteOrder(bidMap, askMap, tmpOrder.getId());
							Order newOrder = new Order();
							newOrder.setPrice(order.getPrice());
							newOrder.setQuantity(newQuantity);
							newOrder.setSide(false);
							return ExecuteOrder(bidMap, askMap, newOrder); //recursion
						}
						else {
							newQuantity = newQuantity*(-1);
							operation.ModifyOrder(bidMap, askMap, tmpOrder.getId(), newQuantity);
							return true;
						}
					}

				} else {
					tmpOrder = bidMap.get(doubleObj).element();
					int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
					if(newQuantity > 0) {
						operation.DeleteOrder(bidMap, askMap, tmpOrder.getId());
						Order newOrder = new Order();
						newOrder.setPrice(order.getPrice());
						newOrder.setQuantity(newQuantity);
						newOrder.setSide(false);
						return ExecuteOrder(bidMap, askMap, newOrder); //recursion
					}
					else {
						newQuantity = newQuantity*(-1);
						operation.ModifyOrder(bidMap, askMap, tmpOrder.getId(), newQuantity);
						return true;
					}
				}
			} else { //If it is a buy limit order we subtract from the ask side
				if (!askMap.containsKey(doubleObj)) {
					while(order.getPrice() > askMap.firstKey()) {
						tmpOrder = askMap.firstEntry().getValue().element();
						int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
						if(newQuantity > 0) {
							operation.DeleteOrder(bidMap, askMap, tmpOrder.getId());
							Order newOrder = new Order();
							newOrder.setPrice(order.getPrice());
							newOrder.setQuantity(newQuantity);
							newOrder.setSide(true);
							return ExecuteOrder(bidMap, askMap, newOrder); //recursion
						}
						else {
							newQuantity = newQuantity*(-1);
							operation.ModifyOrder(bidMap, askMap, tmpOrder.getId(), newQuantity);
							return true;
						}
					}
				} else {
					tmpOrder = askMap.get(doubleObj).element();
					int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
					if(newQuantity > 0) {
						operation.DeleteOrder(bidMap, askMap, tmpOrder.getId());
						Order newOrder = new Order();
						newOrder.setPrice(order.getPrice());
						newOrder.setQuantity(newQuantity);
						newOrder.setSide(true);
						return ExecuteOrder(bidMap, askMap, newOrder); //recursion
					}
					else {
						newQuantity = newQuantity*(-1);
						operation.ModifyOrder(bidMap, askMap, tmpOrder.getId(), newQuantity);
						return true;
					}
				}
			}
		} finally { //write unlock thread so that the TreeMaps  can be updated by any thread
			lock.writeLock().unlock();
		}
		return false;
	}
	
	private void DeleteCreateLogic(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, Order tmpOrder, Order order) {
		tmpOrder = bidMap.firstEntry().getValue().element();
		int newQuantity = order.getQuantity() - tmpOrder.getQuantity();
		if(newQuantity > 0) {
			operation.DeleteOrder(bidMap, askMap, tmpOrder.getId());
			Order newOrder = new Order();
			newOrder.setPrice(order.getPrice());
			newOrder.setQuantity(newQuantity);
			newOrder.setSide(false);
			ExecuteOrder(bidMap, askMap, newOrder); //recursion
		}
		else {
			newQuantity = newQuantity*(-1);
			operation.ModifyOrder(bidMap, askMap, tmpOrder.getId(), newQuantity);
		}
		
	}

}
