package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.api.flickers.AbstractFlickerFunctionality;
import am2.common.utils.SpellUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
		AbstractFlickerFunctionality operator = SpellUtils.GetAbstractFlickerFunctionalityFromID ( meta );
		if (operator == null)
			return "Trash";
		return I18n.format("item.arsmagica2:FlickerFocusPrefix", I18n.format("item.arsmagica2:" + operator.getClass().getSimpleName() + ".name"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		for (AbstractFlickerFunctionality func : ArsMagicaAPI.getFlickerFocusRegistry().getValuesCollection()){
			items.add(new ItemStack(this, 1, func.getID()));
		}
	}
}
