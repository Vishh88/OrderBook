package models;

import java.util.concurrent.atomic.AtomicLong;

public class Order {
	private static AtomicLong counter = new AtomicLong(0);
	private long Id = counter.incrementAndGet();
	private double Price;
	private int Quantity;
	private boolean Side;
	
	public Order(double price, int quantity, boolean side) {
		super();
		Price = price;
		Quantity = quantity;
		Side = side;
	}
	
	public Order() {
		
	}

	public long getId() {
		return Id;
	}
		
	public double getPrice() {
		return Price;
	}
	
	public void setPrice(double price) {
		Price = price;
	}
	
	public int getQuantity() {
		return Quantity;
	}
	
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	
	public boolean isSide() {
		return Side;
	}
	
	public void setSide(boolean side) {
		Side = side;
	}

	@Override
	public String toString() {
		return "\nOrderId: " + Id + ", Price= " + Price + ", Quantity= " + Quantity + ", Side= " + Side;
	}
	
	

}
