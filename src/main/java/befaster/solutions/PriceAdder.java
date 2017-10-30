package befaster.solutions;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import static befaster.solutions.SkuAdder.discounted;
import static befaster.solutions.SkuAdder.simple;


public class PriceAdder {
	public static class Builder {
		private final ImmutableMap.Builder<Character, SkuAdder> builder = new ImmutableMap.Builder();
		
		public Builder withPrice(char sku, int price) {
			builder.put(sku, simple(price));
			return this;
		}
		
		public Builder withDiscount(char sku, int price, int quantityForDiscount, int discountedCost) {
			builder.put(sku, discounted(price, quantityForDiscount, discountedCost));
			return this;
		}
		
		public PriceAdder build() {
			return new PriceAdder(builder.build());
		}
	}
	
	private final Map<Character, SkuAdder> adders;
	
	private int sum;
	private boolean valid;
	
	public PriceAdder(Map<Character, SkuAdder> adders) {
		this.adders = adders;
		sum = 0;
		valid = true;
	}

	public void add(char sku) {
		if(!valid) {
			return;
		}
		if(sum == -1) {
			return;
		}
		SkuAdder skuAdder = adders.get(sku);
		if(skuAdder == null) {
			valid = false;
		} else {
			skuAdder.add();
		}
	}

	public int sum() {
		if(!valid) {
			return -1;
		}
		int sum = 0;
		for (SkuAdder a: adders.values()) {
			sum += a.sum();
		}
		return sum;
	}

}
