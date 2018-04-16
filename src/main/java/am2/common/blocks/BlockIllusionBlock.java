package am2.common.blocks;

import am2.common.blocks.tileentity.TileEntityIllusionBlock;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class BlockIllusionBlock extends BlockAMContainer {

	public static final PropertyEnum<EnumIllusionType> ILLUSION_TYPE = PropertyEnum.create("illusion_type", EnumIllusionType.class);
	
	public BlockIllusionBlock() {
		super(Material.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ILLUSION_TYPE, EnumIllusionType.DEFAULT));
		this.setLightOpacity(255);

		this.setHardness(3.0f);
		this.setResistance(3.0f);

	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ILLUSION_TYPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ILLUSION_TYPE).ordinal();
	}

	/* //TODO: To JSON
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntityIllusionBlock te = (TileEntityIllusionBlock) blockAccess.getTileEntity(pos);
		return te == null || te.getMimicBlock() == null || te.getMimicBlock() == Blocks.AIR.getDefaultState() || te.getMimicBlock().shouldSideBeRendered(blockAccess, pos, side);
	}
		@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

		@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}



	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ILLUSION_TYPE, EnumIllusionType.values()[meta]);
	} */
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityIllusionBlock();
	}
	

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.INVISIBLE;
	}

	/* //TODO: Rework as Entity was removed from parameters

	@Override
    public static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox) {
		if (entity instanceof EntityLivingBase && ((EntityLivingBase)entityBox).isPotionActive(PotionEffectsDefs.TRUE_SIGHT))
			return;
		if (state.getBlock() instanceof BlockIllusionBlock && getIllusionType(state).isSolid())
			addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
	}*/
	
	public static EnumIllusionType getIllusionType(IBlockState state) {
		return state.getValue(ILLUSION_TYPE);
	}
	

	public Object[] GetRecipeComponents(boolean alwaysPassable){
		if (alwaysPassable){
			return new Object[]{
					"BRB", "RGR", "BRB",
					'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
					'G', Blocks.GLASS,
					'B', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE)
			};
		}else{
			return new Object[]{
					"BRB", "R R", "BRB",
					'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
					'B', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE)
			};
		}
	}

	public int GetCraftingQuantity(){
		return 4;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
	}

	public enum EnumIllusionType implements IStringSerializable {
		DEFAULT(true, true),
		NON_COLLIDE(false, false);
		
		private final boolean isSolid;
		private final boolean canBeRevealed;
		
		EnumIllusionType(boolean isSolid, boolean canBeRevealed) {
			this.isSolid = isSolid;
			this.canBeRevealed = canBeRevealed;
		}
		
		public boolean isSolid() {
			return this.isSolid;
		}
		
		public boolean canBeRevealed() {
			return this.canBeRevealed;
		}

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
		
	}
	
}
