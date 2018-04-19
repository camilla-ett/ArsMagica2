package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.common.utils.SpellUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEssence extends ItemArsMagica {
	
	public ItemEssence() {
		super();
		hasSubtypes = true;
		setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for ( int i = 0; i < ArsMagicaAPI.getAffinityRegistry ( ).getValuesCollection ( ).size ( ); i++ ) {
			if ( ArsMagicaAPI.getAffinityRegistry ( ).getValuesCollection ( ).get ( i ).equals ( Affinity.NONE ) )
				continue;
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.format ( "item.arsmagica2:essence.name" , SpellUtils.GetAffinityFromID ( stack.getItemDamage ( ) ).getLocalizedName ( ) );
	}
}
