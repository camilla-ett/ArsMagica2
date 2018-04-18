package am2.common.blocks;

import am2.common.defs.CreativeTabsDefs;
import am2.common.items.ItemBlockSubtypes;
import am2.common.registry.Registry;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class BlockWitchwoodLog extends BlockLog{

	public BlockWitchwoodLog(){
		super();
		setHardness(3.0f);
		setResistance(3.0f);
		setHarvestLevel("axe", 2);
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
		setDefaultState(blockState.getBaseState().withProperty(LOG_AXIS, EnumAxis.Y));
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random){
		return 1;
	}

	/**
	 * returns a number between 0 and 3
	 */
	public static int limitToValidMetadata(int par0){
		return par0 & 3;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List){
		par3List.add(new ItemStack(this));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LOG_AXIS);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[MathHelper.clamp(meta, 0, 3)]);
	}
	
	public BlockWitchwoodLog registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		this.setRegistryName(rl);
		Registry.GetBlocksToRegister().add(this);
		Registry.GetItemsToRegister().add(new ItemBlockSubtypes(this).setRegistryName(rl));
		return this;
	}
}
