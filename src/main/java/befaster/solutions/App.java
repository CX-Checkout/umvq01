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
				.withFree('F', 10, 2, 'F')
				.withPrice('G', 20)
				.withDiscount('H', 10, 5, 45, 10, 80)
				.withPrice('I', 35)
				.withPrice('J', 60)
				.withDiscount('K', 80, 2, 150)
				.withPrice('L', 90)
				.withPrice('M', 15)
				.withFree('N', 40, 3, 'M')
				.withPrice('O', 10)
				.withDiscount('P', 50, 5, 200)
				.withDiscount('Q', 30, 3, 80)
				.withFree('R', 50, 3, 'Q')
				.withPrice('S', 30)
				.withPrice('T', 20)
				.withFree('U', 40, 3, 'U')
				.withDiscount('V', 50, 2, 90, 3, 130)
				.withPrice('W', 20)
				.withPrice('X', 90)
				.withPrice('Y', 10)
				.withPrice('Z', 50)
				.build();
	}
}
