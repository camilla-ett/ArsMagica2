package am2.common.items;

import am2.common.defs.BlockDefs;
import am2.common.defs.CreativeTabsDefs;
import am2.common.registry.Registry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKeystoneDoor extends Item{

	public static final int KEYSTONE_DOOR = 0;
	public static final int SPELL_SEALED_DOOR = 1;

	public ItemKeystoneDoor(){
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabsDefs.tabAM2Items);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack){
		switch (stack.getItemDamage()){
		case KEYSTONE_DOOR:
			return I18n.format("item.arsmagica2:keystone_door.name");
		case SPELL_SEALED_DOOR:
			return I18n.format("item.arsmagica2:spell_sealed_door.name");
		default:
			return I18n.format("item.arsmagica2:unknown.name");
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP){
			return EnumActionResult.PASS;
		}else{
			pos = pos.up();
			Block block;
			if (player.getHeldItem(hand).getItemDamage() == KEYSTONE_DOOR)
				block = BlockDefs.keystoneDoor;
			else
				block = BlockDefs.spellSealedDoor;

			if (player.canPlayerEdit(pos, facing, player.getHeldItem(hand)) && player.canPlayerEdit(pos.up(), facing, player.getHeldItem(hand))){
				if (!block.canPlaceBlockAt(worldIn, pos)){
					return EnumActionResult.FAIL;
				}else{
					EnumFacing enumfacing = EnumFacing.fromAngle((double)player.rotationYaw);
					int i = enumfacing.getFrontOffsetX();
	                int j = enumfacing.getFrontOffsetZ();
					boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
					ItemDoor.placeDoor(worldIn, pos, enumfacing, block, flag);
					player.getHeldItem(hand).shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}else{
				return EnumActionResult.FAIL;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, KEYSTONE_DOOR));
		list.add(new ItemStack(this, 1, SPELL_SEALED_DOOR));
	}
	
	public Item registerAndName(String name) {
		this.setUnlocalizedName("arsmagica2" + name);
		this.setRegistryName(new ResourceLocation("arsmagica2", name));
		Registry.GetItemsToRegister().add(this);
		return this;
	}
}
