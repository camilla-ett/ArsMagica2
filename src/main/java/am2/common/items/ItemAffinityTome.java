package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.extensions.IAffinityData;
import am2.common.extensions.AffinityData;
import am2.common.utils.SpellUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAffinityTome extends ItemArsMagica {

	
	public ItemAffinityTome() {
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for ( int i = 0; i < ArsMagicaAPI.getAffinityRegistry ( ).getValuesCollection ( ).size ( ); i++ ) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
		
		if (worldIn.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);
		
		if (playerIn.getHeldItem(handIn).getItemDamage() == Affinity.NONE.getID()){
			IAffinityData data = AffinityData.For(playerIn);
			data.setLocked(false);
			for ( Affinity aff : ArsMagicaAPI.getAffinityRegistry ( ).getValuesCollection ( ) ) {
				data.setAffinityDepth(aff, data.getAffinityDepth(aff) * AffinityData.MAX_DEPTH - 20);
			}
		}else{
			AffinityData.For ( playerIn ).incrementAffinity ( SpellUtils.GetAffinityFromID ( playerIn.getHeldItem ( handIn ).getItemDamage ( ) ) , 20 );
		}
		playerIn.getHeldItem(handIn).shrink(1);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		Affinity aff = SpellUtils.GetAffinityFromID ( stack.getItemDamage ( ) );
		return I18n.format("item.arsmagica2:tome.name", aff.getLocalizedName());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
