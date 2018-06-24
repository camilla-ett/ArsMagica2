package am2.common.items;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < GameRegistry.findRegistry(Affinity.class).getValues().size(); i++) {
			if (GameRegistry.findRegistry(Affinity.class).getValues().get(i).equals(Affinity.NONE))
				continue;
			subItems.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.format("item.arsmagica2:essence.name", GameRegistry.findRegistry(Affinity.class).getObjectById(stack.getItemDamage()).getLocalizedName());
	}
}
