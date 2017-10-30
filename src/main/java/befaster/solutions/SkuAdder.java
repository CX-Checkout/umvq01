package befaster.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

public interface SkuAdder {
	public interface simple {

	}

	void add();
	int sum();
	void remove();
	
	static SkuAdder simple(int price) {
		return new SimpleSkuAdder(price);
	}
	
	static SkuAdder discounted(int price, int discountQuantity, int discountPrice) {
		return new DiscountSkuAdder(price, ImmutableList.of(new DiscountSkuAdder.Discount(discountQuantity, discountPrice)));
	}
	
	static SkuAdder discounted(int price, int discountQuantity, int discountPrice, int discountQuantity1, int discountPrice1) {
		return new DiscountSkuAdder(price, ImmutableList.of(new DiscountSkuAdder.Discount(discountQuantity, discountPrice), new DiscountSkuAdder.Discount(discountQuantity1, discountPrice1)));
	}
	
	static SkuAdder free(int price, int discountQuantity, SkuAdder skuAdder) {
		return new FreeSkuAdder(price, discountQuantity, skuAdder);
	}
	
	class SimpleSkuAdder implements SkuAdder {
		private final int price;
		private int sum;
		
		public SimpleSkuAdder(int price) {
			this.price = price;
			sum = 0;
		}

		@Override
		public void add() {
			sum += price;
		}

		@Override
		public int sum() {
			return sum;
		}

		@Override
		public void remove() {
			if(sum > 0) {
				sum -= price;
			}
		}
	}
	
	class DiscountSkuAdder implements SkuAdder {
		public static class Discount {
			private final int discountQuantity;
			private final int discountPrice;
			
			public Discount(int discountQuantity, int discountPrice) {
				this.discountQuantity = discountQuantity;
				this.discountPrice = discountPrice;
			}
			
			public int getDiscountQuantity() {
				return discountQuantity;
			}
			
			public int getDiscountPrice() {
				return discountPrice;
			}
		}
		private final int price;
		private final List<Discount> discounts;
		
		private int count;
		
		public DiscountSkuAdder(int price, List<Discount> discounts) {
			this.price = price;
			//need to reverse sort
			
			this.discounts = new ArrayList<>(discounts);
			this.discounts.sort(Comparator.comparingInt(d -> -d.getDiscountQuantity()));
		}
		
		@Override
		public void add() {
			count += 1;
		}
	
		@Override
		public int sum() {
			int count = this.count;
			int sum = 0;
			for(Discount discount: discounts) {
				if(count == 0) {
					break;
				}
				int discountMultiplier = count / discount.discountQuantity;
				sum += discountMultiplier * discount.getDiscountPrice();
				count = count % discount.getDiscountQuantity();
			}
			
			return sum + count * price;
		}

		@Override
		public void remove() {
			if(count > 0) {
				count -= 1;
			}
		}
	}
	
	class FreeSkuAdder extends SimpleSkuAdder {
		private final int discountQuantity;
		private final SkuAdder skuAdder;
		private int count;

		public FreeSkuAdder(int price, int discountQuantity, SkuAdder skuAdder) {
			super(price);
			this.discountQuantity = discountQuantity;
			this.skuAdder = skuAdder;
			count = 0;
		}
		
		@Override
		public void add() {
			super.add();
			count += 1;
		}
		
		@Override
		public void remove() {
			super.remove();
			if(count > 0) {
				count -= 1;
			}
		}
	}
}
