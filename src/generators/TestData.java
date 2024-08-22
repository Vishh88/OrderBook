package generators;

import java.util.LinkedList;
import java.util.TreeMap;

import logic.Operations;
import models.Order;

public class TestData {
	private OrderGenerator orderGenerator = new OrderGenerator();
	private Operations operation = new Operations();

	public TestData() {
	}
	
	public void LoadTestData(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap) {
		// method to generate test data
		System.out.println("Generating Orders...");
		for (int i = 0; i < 10; i++) {
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateRandomOrder());
		}

		for (int i = 0; i < 5; i++) {
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, true));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSamePriceOrder(i + 3, false));
		}
		askMap.values().remove(new LinkedList<>());
		bidMap.values().remove(new LinkedList<>());
	}

// used to generate new test data in bulk, instead of using the predefined data
	public void GenerateOrders(TreeMap<Double, LinkedList<Order>> bidMap, TreeMap<Double, LinkedList<Order>> askMap, int bidCount, int askCount) {
		for (int i=0; i<bidCount; i++) {
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSameSideOrder(true));
		}
		
		for (int i=0; i<askCount; i++) {
			operation.AddOrder(bidMap, askMap, orderGenerator.GenerateSameSideOrder(false));
		}
		
	}

}
