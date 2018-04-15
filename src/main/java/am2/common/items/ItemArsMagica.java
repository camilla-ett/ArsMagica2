package am2.common.items;

import am2.common.defs.CreativeTabsDefs;
import am2.common.registry.Registry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemArsMagica extends Item{
	
	public ItemArsMagica() {
		setCreativeTab(CreativeTabsDefs.tabAM2Items);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	public ItemArsMagica registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		Registry.GetItemsToRegister().add(this);
		return this;
	}
}
