package logic;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import models.Order;

public class Displays {

	public Displays() {
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

}
