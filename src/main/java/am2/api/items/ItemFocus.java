package am2.api.items;

import am2.common.defs.CreativeTabsDefs;
import am2.common.registry.Registry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public abstract class ItemFocus extends Item{
	
	public ItemFocus() {
		setCreativeTab(CreativeTabsDefs.tabAM2Items);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	public abstract Object[] getRecipeItems();

	public abstract String getInGameName();
	
	public ItemFocus registerAndName(String name) {
		this.setRegistryName(new ResourceLocation("arsmagica2", name));
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		Registry.GetItemsToRegister().add(this);
		return this;
	}
}
