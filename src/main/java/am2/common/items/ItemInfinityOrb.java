package am2.common.items;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.SkillPointRegistry;
import am2.api.skill.SkillPoint;
import am2.common.extensions.EntityExtension;
import am2.common.extensions.SkillData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInfinityOrb extends ItemArsMagica {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		SkillPoint point = SkillPointRegistry.getPointForTier(itemStackIn.getItemDamage());
		if (point == null)
			playerIn.addChatMessage(new TextComponentString("Broken Item : Please use a trash bin."));
		itemStackIn = doGiveSkillPoints(playerIn, itemStackIn, point);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		SkillPoint point = SkillPointRegistry.getPointForTier(stack.getItemDamage());
		if (point == null)
			return "Unavailable Item";
		return I18n.format("item.arsmagica2:inf_orb_" + point.toString().toLowerCase() + ".name");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		SkillPoint point = SkillPointRegistry.getPointForTier(stack.getItemDamage());
		if (point == null)
			tooltip.add("Place this into your inventory to convert it into the usable version");
	}
	
	private ItemStack doGiveSkillPoints(EntityPlayer player, ItemStack stack, SkillPoint type){
		if (EntityExtension.For(player).getCurrentLevel() > 0){
			if (!player.world.isRemote)
				SkillData.For(player).setSkillPoint(type, SkillData.For(player).getSkillPoint(type) + 1);
			if (player.world.isRemote){
				player.addChatMessage(new TextComponentString(I18n.format("am2.tooltip.infOrb" + type.toString())));
			}
			if (!player.capabilities.isCreativeMode)
			stack.setCount(stack.getCount()-1);
			if (stack.getCount() < 1){
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
		}else{
			if (player.world.isRemote){
				int message = player.world.rand.nextInt(10);
				player.addChatMessage(new TextComponentString(I18n.format("am2.tooltip.infOrbFail" + message)));
			}
		}
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
		if (ArsMagicaAPI.hasTier4())
			subItems.add(new ItemStack(itemIn, 1, 3));
		if (ArsMagicaAPI.hasTier5())
			subItems.add(new ItemStack(itemIn, 1, 4));
		if (ArsMagicaAPI.hasTier6())
			subItems.add(new ItemStack(itemIn, 1, 5));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		SkillPoint point = SkillPointRegistry.getPointForTier(stack.getItemDamage());
		if (point == null && stack.getItemDamage() > 0)
			stack.setItemDamage(stack.getItemDamage() - 1);
	}
	
	
}
