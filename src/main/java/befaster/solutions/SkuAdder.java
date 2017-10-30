package befaster.solutions;

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
		return new DiscountSkuAdder(price, discountQuantity, discountPrice);
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
			sum -= price;
		}
	}
	
	class DiscountSkuAdder implements SkuAdder {
		private final int price;
		private final int discountQuantity;
		private final int discountPrice;
		
		private int count;
		
		public DiscountSkuAdder(int price, int discountQuantity, int discountPrice) {
			this.price = price;
			this.discountQuantity = discountQuantity;
			this.discountPrice = discountPrice;
		}
		
		@Override
		public void add() {
			count += 1;
		}
	
		@Override
		public int sum() {
			int discountMultiplier = count / discountQuantity;
			int discountRemainder = count % discountQuantity;
			return discountMultiplier * discountPrice + discountRemainder * price;
		}

		@Override
		public void remove() {
			count -= 1;
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
			if(count % discountQuantity == 0) {
				skuAdder.remove();
			}
		}
		
		@Override
		public void remove() {
			super.remove();
			if(count % discountQuantity != 0) {
				skuAdder.remove();
			}
			count -= 1;
		}
	}
}
