package am2.common.items;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.flickers.AbstractFlickerFunctionality;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlickerFocus extends ItemArsMagica{

	public ItemFlickerFocus(){
		super();
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		AbstractFlickerFunctionality operator = GameRegistry.findRegistry(AbstractFlickerFunctionality.class).getObjectById(meta);
		if (operator == null)
			return "Trash";
		return I18n.format("item.arsmagica2:FlickerFocusPrefix", I18n.format("item.arsmagica2:" + operator.getClass().getSimpleName() + ".name"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List){
		for (AbstractFlickerFunctionality func : GameRegistry.findRegistry(AbstractFlickerFunctionality.class).getValues()){
			par3List.add(new ItemStack(this, 1, GameRegistry.findRegistry(AbstractFlickerFunctionality.class).getKey(func)));
		}
	}
}
