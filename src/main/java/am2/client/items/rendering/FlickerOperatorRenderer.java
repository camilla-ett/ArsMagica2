package am2.client.items.rendering;

import am2.api.ArsMagicaAPI;
import am2.api.flickers.AbstractFlickerFunctionality;
import am2.common.utils.SpellUtils;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FlickerOperatorRenderer implements ItemMeshDefinition{
	
	public FlickerOperatorRenderer() {
	}
	
	public FlickerOperatorRenderer addModels(Item item) {
		for ( AbstractFlickerFunctionality func : ArsMagicaAPI.getFlickerFocusRegistry ( ).getValuesCollection ( ) )
			ModelBakery.registerItemVariants(item, new ModelResourceLocation(func.getTexture(), "inventory"));
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(new ResourceLocation("arsmagica2:flickeroperatorblank"), "inventory"));
		return this;
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		AbstractFlickerFunctionality func = SpellUtils.GetAbstractFlickerFunctionalityFromID ( stack.getItemDamage ( ) );
		if (func == null) return new ModelResourceLocation(new ResourceLocation("arsmagica2:flickeroperatorblank"), "inventory");
		return new ModelResourceLocation(func.getTexture(), "inventory");
	}
	
}
