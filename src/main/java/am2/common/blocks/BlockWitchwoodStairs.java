package am2.common.blocks;

import am2.common.defs.CreativeTabsDefs;
import am2.common.items.ItemBlockSubtypes;
import am2.common.registry.Registry;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class BlockWitchwoodStairs extends BlockStairs{

	public BlockWitchwoodStairs(IBlockState state){
		super(state);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
		this.setHarvestLevel("axe", 2);
		this.setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
	}

	public BlockStairs registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		this.setRegistryName(rl);
		Registry.GetBlocksToRegister().add(this);
		Registry.GetItemsToRegister().add(new ItemBlockSubtypes(this));
		return this;
	}
}
