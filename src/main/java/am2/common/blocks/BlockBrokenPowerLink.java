package am2.common.blocks;

import am2.ArsMagica2;
import am2.common.armor.ArmorHelper;
import am2.common.armor.infusions.GenericImbuement;
import am2.common.blocks.tileentity.TileEntityBrokenPowerLink;
import am2.common.defs.ItemDefs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockBrokenPowerLink extends BlockAMContainer{

	public BlockBrokenPowerLink(){
		super(Material.CIRCUITS);
		setBlockUnbreakable(); // can't be broken, but can be directly replaced, and has no collisions
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityBrokenPowerLink();
	}


	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (ArsMagica2.proxy.getLocalPlayer() != null &&
				ArsMagica2.proxy.getLocalPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null &&
				(ArsMagica2.proxy.getLocalPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ItemDefs.magitechGoggles)
				|| ArmorHelper.isInfusionPreset(ArsMagica2.proxy.getLocalPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD), GenericImbuement.magitechGoggleIntegration))
			return FULL_BLOCK_AABB;
		return NULL_AABB;
	}

	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return null;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<>();
	}
}
