package am2.client.items.rendering;

import am2.api.ArsMagicaAPI;
import am2.api.flickers.AbstractFlickerFunctionality;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlickerOperatorRenderer implements ItemMeshDefinition{
	
	public FlickerOperatorRenderer() {
	}
	
	public FlickerOperatorRenderer addModels(Item item) {
		for (AbstractFlickerFunctionality func : GameRegistry.findRegistry(AbstractFlickerFunctionality.class).getValues())
			ModelBakery.registerItemVariants(item, new ModelResourceLocation(func.getTexture(), "inventory"));
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(new ResourceLocation("arsmagica2:FlickerOperatorBlank"), "inventory"));
		return this;
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		AbstractFlickerFunctionality func = GameRegistry.findRegistry(AbstractFlickerFunctionality.class).getObjectById(stack.getItemDamage());
		if (func == null) return new ModelResourceLocation(new ResourceLocation("arsmagica2:FlickerOperatorBlank"), "inventory");
		return new ModelResourceLocation(func.getTexture(), "inventory");
	}
	
}
