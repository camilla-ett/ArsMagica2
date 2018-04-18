package am2.common.blocks;

import am2.common.defs.CreativeTabsDefs;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static am2.common.registry.Registry.GetBlocksToRegister;
import static am2.common.registry.Registry.GetItemsToRegister;

public class BlockAMFlower extends BlockBush{

	public BlockAMFlower(){
		super();
		setSoundType(SoundType.PLANT);
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
	}

	public BlockAMFlower registerAndName(ResourceLocation loc) {
        this.setUnlocalizedName(loc.toString());
        this.setRegistryName(loc);
        GetBlocksToRegister().add(this);
        GetItemsToRegister().add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        return this;
    }

	public boolean canGrowOn(World worldIn, BlockPos pos) {
		return canBlockStay(worldIn, pos, worldIn.getBlockState(pos));
	}
	
}
