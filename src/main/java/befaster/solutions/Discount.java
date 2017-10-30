package befaster.solutions;

public class Discount {
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
