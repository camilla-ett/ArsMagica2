package am2.common.blocks;

import am2.common.items.ItemWakebloom;
import am2.common.registry.Registry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockWakebloom extends BlockAMFlower{

	public BlockWakebloom(){
		super();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos){
		return EnumPlantType.Water;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getBlock().equals(Blocks.WATER);
	}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state){
		return canPlaceBlockAt(worldIn, pos);
	}
	
	@Override
	public BlockAMFlower registerAndName(ResourceLocation loc) {
		setUnlocalizedName(loc.toString());
		Registry.GetBlocksToRegister().add(this);
		Registry.GetItemsToRegister().add(new ItemWakebloom(this));
		return this;
	}
}
