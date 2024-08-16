package models;

import java.util.UUID;

public class Order {
	private String Id = UUID.randomUUID().toString();;
	private double Price;
	private int Quantity;
	private boolean Side;
	
	public Order(double price, int quantity, boolean side) {
		Price = price;
		Quantity = quantity;
		Side = side;
		if(side) {
			Id = "BID-" + Id;
		}
		else {
			Id = "ASK-" + Id;
		}
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
		if(side) {
			Id = "BID-" + Id;
		}
		else {
			Id = "ASK-" + Id;
		}
	}

	@Override
	public String toString() {
		return "OrderId: " + Id + ", Price= " + Price + ", Quantity= " + Quantity + ", Side= " + Side;
	}
	
	

}
