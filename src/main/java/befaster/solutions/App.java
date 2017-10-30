package befaster.solutions;

public class App {
	public static int checkout(String skus) {
		PriceAdder priceAdder = getPriceAdder();
		for(char sku: skus.toCharArray()) {
			priceAdder.add(sku);
		}
		return priceAdder.sum();
	}
	
	private static PriceAdder getPriceAdder() {
		return new PriceAdder.Builder()
				.withDiscount('A', 50, 3, 130, 5, 200)
				.withDiscount('B', 30, 2, 45)
				.withPrice('C', 20)
				.withPrice('D', 15)
				.withFree('E', 40, 2, 'B')
				.build();
	}
}
