package am2.common.items;

import am2.common.blocks.BlockCrystalMarker;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.resources.I18n;

public class ItemBlockCrystalMarker extends ItemBlockSubtypes {

	public ItemBlockCrystalMarker(Block block) {
		super(block);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.format("tile.arsmagica2:" + BlockCrystalMarker.crystalMarkerTypes[MathHelper.clamp_int(stack.getItemDamage(), 0, BlockCrystalMarker.crystalMarkerTypes.length - 1)].toLowerCase() + ".name");
	}
}
