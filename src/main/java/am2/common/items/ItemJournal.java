package am2.common.items;

import am2.common.utils.EntityUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemJournal extends ItemArsMagica{

	private static final String KEY_NBT_XP = "Stored_XP";
	private static final String KEY_NBT_OWNER = "Owner";

	public ItemJournal(){
		super();
	}
	
	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return true;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		String owner = getOwner(stack);
		if (owner == null){
			tooltip.add(I18n.format("am2.tooltip.unowned"));
			tooltip.add(I18n.format("am2.tooltip.journalUse"));
			return;
		}else{
			tooltip.add(String.format(I18n.format("am2.tooltip.journalOwner")));
			tooltip.add(String.format(I18n.format("am2.tooltip.journalOwner2"), owner));
		}

			tooltip.add(String.format(I18n.format("am2.tooltip.containedXP"), getXPInJournal(stack))); //TODO Add Owner Check

		if (owner.isEmpty())
			tooltip.add(I18n.format("am2.tooltip.journalUse"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn){

		if (!player.world.isRemote){
			if (getOwner(player.getHeldItem(handIn)) == null){
				setOwner(player.getHeldItem(handIn), player);
			}else if (!getOwner(player.getHeldItem(handIn)).equals(player.getName())){
			  player.sendMessage(new TextComponentString(I18n.format("am2.tooltip.notYourJournal")));
				return super.onItemRightClick(world, player, handIn);
			}

			if (player.isSneaking()){
				int removedXP = EntityUtils.deductXP(10, player);
				addXPToJournal(player.getHeldItem(handIn), removedXP);
			}else{
				int amt = Math.min(getXPInJournal(player.getHeldItem(handIn)), 10);
				if (amt > 0){
					player.addExperience(amt);
					deductXPFromJournal(player.getHeldItem(handIn), amt);
				}
			}
		}

		return super.onItemRightClick(world, player, handIn);
	}

	private void addXPToJournal(ItemStack journal, int amount){
		if (!journal.hasTagCompound())
			journal.setTagCompound(new NBTTagCompound());
		journal.getTagCompound().setInteger(KEY_NBT_XP, journal.getTagCompound().getInteger(KEY_NBT_XP) + amount);
	}

	private void deductXPFromJournal(ItemStack journal, int amount){
		addXPToJournal(journal, -amount);
	}

	private int getXPInJournal(ItemStack journal){
		if (!journal.hasTagCompound())
			return 0;
		return journal.getTagCompound().getInteger(KEY_NBT_XP);
	}

	private String getOwner(ItemStack journal){
		if (!journal.hasTagCompound())
			return null;
		return journal.getTagCompound().getString(KEY_NBT_OWNER);
	}

	private void setOwner(ItemStack journal, EntityPlayer player){
		if (!journal.hasTagCompound())
			journal.setTagCompound(new NBTTagCompound());
		journal.getTagCompound().setString(KEY_NBT_OWNER, player.getName());
	}

}
