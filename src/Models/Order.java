package Models;

import Generators.UIDGenerator;

public class Order {
	private String Id = UIDGenerator.nextUID();
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

	public String getId() {
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
		return "Order [Id=" + Id + ", Price=" + Price + ", Quantity=" + Quantity + ", Side=" + Side + "]";
	}
	
	

}
