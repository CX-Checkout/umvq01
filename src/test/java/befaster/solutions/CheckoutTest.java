package befaster.solutions;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

public class CheckoutTest {

	@Test
	public void empty_basket() {
		assertThat(App.checkout(""), equalTo(0));
	}
	
	@Test
	public void single_item() {
		assertThat(App.checkout("A"), equalTo(50));
		assertThat(App.checkout("B"), equalTo(30));
		assertThat(App.checkout("C"), equalTo(20));
		assertThat(App.checkout("D"), equalTo(15));
	}
	
	@Test
	public void multiple_items() {
		assertThat(App.checkout("ABCD"), equalTo(115));
		assertThat(App.checkout("AA"), equalTo(100));
		assertThat(App.checkout("CDAC"), equalTo(105));
	}
	
	@Test
	public void invalid_basket() {
		assertThat(App.checkout("1"), equalTo(-1));
	}
	
	@Test
	public void multibuy_discount() {
		assertThat(App.checkout("AAA"), equalTo(130));
		assertThat(App.checkout("BB"), equalTo(45));
		assertThat(App.checkout("BACDDCABAA"), equalTo(295));
		assertThat(App.checkout("AAAAA"), equalTo(200));
		assertThat(App.checkout("AAAAAAAAAAAAAAA"), equalTo(600));
	}
	
	@Test
	public void free_discount() {
		assertThat(App.checkout("EBE"), equalTo(80));
		assertThat(App.checkout("FFF"), equalTo(20));
	}
}
