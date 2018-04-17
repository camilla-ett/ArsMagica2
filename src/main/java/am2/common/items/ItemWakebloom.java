package am2.common.items;

import am2.common.defs.BlockDefs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class ItemWakebloom extends ItemBlock{

	public ItemWakebloom(Block block) {
		super(block);
	}
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
		RayTraceResult mop = this.rayTrace(worldIn, playerIn, true);

		if (mop == null){
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		}else{
			if (mop.typeOfHit == Type.BLOCK){

				if (!worldIn.canMineBlockBody(playerIn, mop.getBlockPos())){
					return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
				}

				if (!playerIn.canPlayerEdit(mop.getBlockPos(), mop.sideHit, playerIn.getHeldItem(handIn))){
					return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
				}

				if (worldIn.getBlockState(mop.getBlockPos()) == Blocks.FLOWING_WATER.getDefaultState() || worldIn.getBlockState(mop.getBlockPos()) == Blocks.WATER.getDefaultState()){
					worldIn.setBlockState(mop.getBlockPos().up(), BlockDefs.wakebloom.getDefaultState());

					if (!playerIn.capabilities.isCreativeMode){
						playerIn.getHeldItem(handIn).shrink(1);
					}
				}
			}

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
	}

}
