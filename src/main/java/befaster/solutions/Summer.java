package befaster.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Summer {
	int sum(Map<Character, Integer> counts, Map<Character, Summer> summers);
	
	class SimpleSummer implements Summer {
		private final char sku;
		private final int price;
		
		public SimpleSummer(char sku, int price) {
			this.sku = sku;
			this.price = price;
		}

		@Override
		public int sum(Map<Character, Integer> counts, Map<Character, Summer> summers) {
			int count = count(counts);
			return sum(count);
		}

		protected Integer count(Map<Character, Integer> counts) {
			return counts.getOrDefault(sku, 0);
		}

		protected int sum(int count) {
			return count * price;
		}
	}
	
	class DiscountSummer extends SimpleSummer {
		private final List<Discount> discounts;
		
		public DiscountSummer(char sku, int price, List<Discount> discounts) {
			super(sku, price);
			this.discounts = new ArrayList<>(discounts);
			this.discounts.sort(Comparator.comparingInt(d -> -d.getDiscountQuantity()));
		}
		
		@Override
		public int sum(Map<Character, Integer> counts, Map<Character, Summer> summers) {
			int count = count(counts);
			int sum = 0;
			for(Discount discount: discounts) {
				if(count == 0) {
					break;
				}
				int quantity = discount.getDiscountQuantity();
				sum += (count / quantity) * discount.getDiscountPrice();
				count = count % quantity;
			}
			return sum + sum(count);
		}
	}
	
	class FreeSummer extends SimpleSummer {
		
		private final int discountQuantity;
		private final char freeSku;
		
		public FreeSummer(char sku, int price, int discountQuantity, char freeSku) {
			super(sku, price);
			this.discountQuantity = discountQuantity;
			this.freeSku = freeSku;
		}
		
		@Override
		public int sum(Map<Character, Integer> counts, Map<Character, Summer> summers) {
			int thisCount = count(counts);
			int sum = super.sum(counts, summers);
			Summer summer = summers.get(freeSku);
			int original = summer.sum(counts, summers);
			HashMap<Character, Integer> copy = new HashMap<>(counts);
			int count = copy.getOrDefault(freeSku, 0);
			if(count > 0) {
				count -= thisCount / discountQuantity;
			}
			copy.put(freeSku, count);
			int mod = summer.sum(copy, summers);
			return sum + mod - original;
		}
	}
	
	class SelfFreeSummer extends SimpleSummer {
		private final int discountQuantity;

		public SelfFreeSummer(char sku, int price, int discountQuantity) {
			super(sku, price);
			this.discountQuantity = discountQuantity;
		}
		
		@Override
		public int sum(Map<Character, Integer> counts, Map<Character, Summer> summers) {
			int count = count(counts);
			int freeCount = count / (discountQuantity + 1);
			return sum(count - freeCount);
		}
	}
}
