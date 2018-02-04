package am2.common;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;

import am2.api.power.IObeliskFuelHelper;
import net.minecraft.item.ItemStack;

public class ObeliskFuelHelper implements IObeliskFuelHelper{
	private List<Function<ItemStack, Integer>> validFuels;

	public static final ObeliskFuelHelper instance = new ObeliskFuelHelper();

	private ObeliskFuelHelper(){
		validFuels = Lists.newArrayList();
	}

	@Override
	public void registerFuelType(Function<ItemStack, Integer> func) {
		if (func != null)
			validFuels.add(func);
	}

	@Override
	public int getFuelBurnTime(ItemStack stack){
		if (stack == null)
			return 0;

		for (Function<ItemStack, Integer> possibleFuel : validFuels){
			int val = possibleFuel.apply(stack);
			if (val > 0)
				return val;
		}
		return 0;
	}
}
