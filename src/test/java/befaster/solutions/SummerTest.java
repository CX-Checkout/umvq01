package befaster.solutions;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class SummerTest {

	@Test
	public void simple() {
		assertThat(new Summer.SimpleSummer('A', 50).sum(ImmutableMap.of('A', 4), ImmutableMap.of()), equalTo(200));
	}

	@Test
	public void discount() {
		assertThat(new Summer.DiscountSummer('A', 50, ImmutableList.of(new Discount(3, 130), new Discount(5, 200))).sum(ImmutableMap.of('A', 9), ImmutableMap.of()), equalTo(380));
	}
	
	@Test
	public void free() {
		assertThat(new Summer.FreeSummer('A', 50, 2, 'B').sum(ImmutableMap.of('A', 2, 'B', 1), ImmutableMap.of('B', new Summer.SimpleSummer('B', 30))), equalTo(70));
		assertThat(new Summer.FreeSummer('A', 50, 2, 'B').sum(ImmutableMap.of('A', 2), ImmutableMap.of('B', new Summer.SimpleSummer('B', 30))), equalTo(100));
		assertThat(new Summer.SelfFreeSummer('A', 50, 2).sum(ImmutableMap.of('A', 3), ImmutableMap.of()), equalTo(100));
	}
	
	@Test
	public void group() {
		PriceAdder.Group group = new PriceAdder.Group(ImmutableSet.of('X', 'Y', 'Z'), 3, 45);
		int sum = group.sum(ImmutableMap.of('X', 1, 'Y', 2, 'Z', 1), ImmutableMap.of('X', new Summer.SimpleSummer('X', 17), 'Y', new Summer.SimpleSummer('Y', 20), 'Z', new Summer.SimpleSummer('Z', 21)));
		assertThat(sum, equalTo(-16));
	}
}
