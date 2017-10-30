package befaster.solutions;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

public class DiscountSkuAdderTest {

	@Test
	public void discounts_are_applied() {
		SkuAdder adder = SkuAdder.discounted(30, 2, 45);
		assertThat(adder.sum(), equalTo(0));
		adder.add();
		assertThat(adder.sum(), equalTo(30));
		adder.add();
		assertThat(adder.sum(), equalTo(45));
		adder.add();
		assertThat(adder.sum(), equalTo(75));
		adder.add();
		assertThat(adder.sum(), equalTo(90));
	}

}
