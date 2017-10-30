package befaster.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class PriceAdder {
	private final ImmutableMap<Character, Summer> summers;
	private final ImmutableMap<Set<Character>, Group> groups;
	
	public static class Builder {
		private final ImmutableMap.Builder<Character, Summer> builder = new ImmutableMap.Builder();
		private final ImmutableMap.Builder<Set<Character>, Group> groups = new ImmutableMap.Builder();
		
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
			return new PriceAdder(builder.build(), groups.build());
		}
		
		public Builder withFree(char sku, int price, int discountQuantity, char freeSku) {
			if(sku == freeSku) {
				builder.put(sku, new Summer.SelfFreeSummer(sku, price, discountQuantity));
			} else {
				builder.put(sku, new Summer.FreeSummer(sku, price, discountQuantity, freeSku));
			}
			return this;
		}

		public Builder withGroup(int discountQuantity, int discountPrice, Character... skus) {
			Set<Character> skuSet = ImmutableSet.copyOf(skus);
			groups.put(skuSet, new Group(skuSet, discountQuantity, discountPrice));
			return this;
		}
	}
	
	public static class Group implements Summer {
		private final Set<Character> skuSet;
		private final int discountQuantity;
		private final int discountPrice;
		
		public Group(Set<Character> skuSet, int discountQuantity, int discountPrice) {
			this.skuSet = skuSet;
			this.discountQuantity = discountQuantity;
			this.discountPrice = discountPrice;
		}
		
		public int getDiscountQuantity() {
			return discountQuantity;
		}
		
		public int getDiscountPrice() {
			return discountPrice;
		}

		@Override
		public int sum(Map<Character, Integer> counts, Map<Character, Summer> summers) {
			int count = 0;
			int sum = 0;
			List<SimpleSummer> orderedPrices = new ArrayList<>();
			for(char sku: skuSet) {
				count += counts.getOrDefault(sku, 0);
				Summer summer = summers.get(sku);
				sum += summer.sum(counts, summers);
				if(summer instanceof SimpleSummer) {
					orderedPrices.add((SimpleSummer) summer);
				}
			}
			orderedPrices.sort(Comparator.comparing(s -> {
				if(s != null) {
					return -s.getPrice();
				}
				return 0;
			}));
			Map<Character, Integer> countsCopy = new HashMap<>(counts);
			int discountUnits = count / discountQuantity;
			int toDiscount = discountUnits * discountQuantity;
			int discountSum = (count / discountQuantity) * discountPrice;
			for(SimpleSummer s: orderedPrices) {
				if(toDiscount < 1) {
					break;
				}
				char sku = s.getSku();
				Integer c = countsCopy.get(sku);
				int toRemove = toDiscount > c ? c : toDiscount;
				countsCopy.put(sku, c - toRemove);
				toDiscount -= toRemove;
			}
			for(Summer s: orderedPrices) {
				discountSum += s.sum(countsCopy, summers);
			}
			return discountSum - sum;
		}
		
		public Set<Character> getSkuSet() {
			return skuSet;
		}
	}
	
	private final Map<Character, Integer> counts = new HashMap<>();

	public PriceAdder(ImmutableMap<Character, Summer> summers, ImmutableMap<Set<Character>, Group> groups) {
		this.summers = summers;
		this.groups = groups;
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
		
		for(Group g: groups.values()) {
			sum += g.sum(counts, summers);
		}
		return sum;
	}
}
