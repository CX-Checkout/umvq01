package befaster.solutions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class PriceAdder {
	private final ImmutableMap<Character, Summer> summers;
	
	public static class Builder {
		private final ImmutableMap.Builder<Character, Summer> builder = new ImmutableMap.Builder();
		
		public Builder withPrice(char sku, int price) {
			builder.put(sku, new Summer.SimpleSummer(sku, price));
			return this;
		}
		
		public Builder withDiscount(char sku, int price, int quantityForDiscount, int discountedCost) {
			builder.put(sku, new Summer.DiscountSummer(sku, price, ImmutableList.of(new Discount(quantityForDiscount, discountedCost))));
			return this;
		}
		
		public Builder withDiscount(char sku, int price, int quantityForDiscount, int discountedCost, int quantityForDiscount1, int discountedCost1) {
			builder.put(sku, new Summer.DiscountSummer(sku, price, ImmutableList.of(new Discount(quantityForDiscount, discountedCost), new Discount(quantityForDiscount1, discountedCost1))));
			return this;
		}
		
		public PriceAdder build() {
			return new PriceAdder(builder.build());
		}
		
		public Builder withFree(char sku, int price, int discountQuantity, char freeSku) {
			builder.put(sku, new Summer.FreeSummer(sku, price, discountQuantity, freeSku));
			return this;
		}
	}
	
	private final Map<Character, Integer> counts = new HashMap<>();

	public PriceAdder(ImmutableMap<Character, Summer> summers) {
		this.summers = summers;
	}
	
	public void add(char sku) {
		int count = counts.getOrDefault(sku, 0);
		count += 1;
		counts.put(sku, count);
	}

	public int sum() {
		for(char sku: counts.keySet()) {
			if(!summers.containsKey(sku)) {
				return -1;
			}
		}
		int sum = 0;
		for(Summer summer: summers.values()) {
			sum += summer.sum(counts, summers);
		}
		return sum;
	}
}
