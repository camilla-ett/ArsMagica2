package am2.common.blocks;

import am2.common.defs.CreativeTabsDefs;
import am2.common.registry.Registry;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAMFlower extends BlockBush{

	public BlockAMFlower(){
		super();
		setSoundType(SoundType.PLANT);
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
	}

	public BlockAMFlower registerAndName(ResourceLocation loc){
		setUnlocalizedName(loc.toString());
		Registry.GetBlocksToRegister().add(this);
		Registry.GetItemsToRegister().add(new ItemBlock(this));
		return this;
	}

	public boolean canGrowOn(World worldIn, BlockPos pos) {
		return canBlockStay(worldIn, pos, worldIn.getBlockState(pos));
	}
	
}
