package am2.common.blocks;

import net.minecraft.block.BlockIce;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static am2.common.registry.Registry.GetBlocksToRegister;
import static am2.common.registry.Registry.GetItemsToRegister;

public class BlockFrost extends BlockIce {
	
	public BlockFrost() {
		super();
		setTickRandomly(true);
		setHardness(0.5F);
		setSoundType(SoundType.GLASS);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		worldIn.setBlockToAir(pos);
	}

	public BlockFrost registerAndName(ResourceLocation rl) {
        this.setRegistryName(rl);
        this.setUnlocalizedName(rl.toString());
        GetBlocksToRegister().add(this);
        GetItemsToRegister().add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        return this;
    }
}
