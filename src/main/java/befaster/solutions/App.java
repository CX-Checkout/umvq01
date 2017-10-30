package befaster.solutions;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;

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
				.withDiscount('A', 50, 3, 130)
				.withDiscount('B', 30, 2, 45)
				.withPrice('C', 20)
				.withPrice('D', 15)
				.build();
	}
}
