package generators;

import java.text.DecimalFormat;
import java.util.Random;

import models.Order;

public class OrderGenerator {

	Random random = new Random();
	DecimalFormat df = new DecimalFormat("#.##");
	public OrderGenerator() {
		
	}
	
	public Order GenerateRandomOrder() {
		Order newOrder = new Order();
		newOrder.setPrice(Double.parseDouble(df.format(random.nextDouble()*100)));
		newOrder.setQuantity(random.nextInt(100));
		newOrder.setSide(random.nextBoolean());
		return newOrder;
	}
	
	public Order GenerateSamePriceOrder(double price, boolean side) {
		Order newOrder = new Order();
		newOrder.setPrice(price);
		newOrder.setQuantity(random.nextInt(100));
		newOrder.setSide(side);		
		return newOrder;
	}
	
	public Order GenerateSameSideOrder(boolean side) {
		Order newOrder = new Order();
		newOrder.setPrice(Double.parseDouble(df.format(random.nextDouble()*100)));
		newOrder.setQuantity(random.nextInt(100));
		newOrder.setSide(side);		
		return newOrder;
	}	

}
