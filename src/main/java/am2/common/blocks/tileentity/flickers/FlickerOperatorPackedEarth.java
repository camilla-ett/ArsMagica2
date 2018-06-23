package am2.common.blocks.tileentity.flickers;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.flickers.IFlickerController;
import am2.common.defs.ItemDefs;
import am2.common.utils.AffinityShiftUtils;
import am2.common.utils.InventoryUtilities;
import am2.api.flickers.AbstractFlickerFunctionality;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlickerOperatorPackedEarth extends AbstractFlickerFunctionality{
	
	public final static FlickerOperatorPackedEarth instance = new FlickerOperatorPackedEarth();

	@Override
	public boolean RequiresPower(){
		return false;
	}

	@Override
	public int PowerPerOperation(){
		return 10;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered){
		int searchesPerLoop = 12;

		int radius = 6;
		int diameter = radius * 2 + 1;

		if (!worldObj.isRemote){

			boolean actionPerformed = false;
			for (int i = 0; i < searchesPerLoop && !actionPerformed; ++i){
				TileEntity te = worldObj.getTileEntity(((TileEntity)habitat).getPos().down());
				if (te == null || !(te instanceof IInventory)){
					return false;
				}
				
				BlockPos effectPos = ((TileEntity)habitat).getPos().add(-radius, -1, -radius).add(worldObj.rand.nextInt(diameter), worldObj.rand.nextInt(diameter), worldObj.rand.nextInt(radius));
				
				if (effectPos.getY() < 3)
					effectPos = new BlockPos(effectPos.getX(), 3, effectPos.getY());

				Block block = worldObj.getBlockState(effectPos).getBlock();

				if (worldObj.isAirBlock(effectPos) || block.isReplaceable(worldObj, effectPos)){
					int inventoryIndex = InventoryUtilities.getFirstBlockInInventory((IInventory)te);
					if (inventoryIndex > -1){
						ItemStack stack = ((IInventory)te).getStackInSlot(inventoryIndex);
						InventoryUtilities.decrementStackQuantity((IInventory)te, inventoryIndex, 1);
						worldObj.setBlockState(effectPos, Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getItemDamage()));
						actionPerformed = true;
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> controller, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, controller, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> controller, boolean powered){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return powered ? 1 : 20;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> controller, boolean powered, Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"DDD",
				"RFR",
				" E ",
				Character.valueOf('D'), Blocks.DIRT,
				Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
				Character.valueOf('E'), AffinityShiftUtils.getEssenceForAffinity(Affinity.EARTH),
				Character.valueOf('F'), new ItemStack(ItemDefs.flickerJar, 1, GameRegistry.findRegistry(Affinity.class).getId(Affinity.EARTH))

		};
	}
	
	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation("arsmagica2", "FlickerOperatorPackedEarth");
	}

	@Override
	public Affinity[] getMask() {
		return new Affinity[]{Affinity.EARTH};
	}


}
