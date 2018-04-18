package am2.common.blocks;

import am2.common.defs.ItemDefs;
import am2.common.registry.Registry;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockWizardsChalk extends BlockAMSpecialRender{
	
	public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

	public BlockWizardsChalk(){
		super(Material.CIRCUITS);
		setBlockBounds(0, 0, 0, 1, 0.02f, 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1.0f;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack (ItemDefs.chalk);
	}
	
	public BlockAM registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		this.setRegistryName(rl);
		Registry.GetBlocksToRegister().add(this);
		return this;
	}
}
