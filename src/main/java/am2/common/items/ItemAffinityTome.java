package am2.common.items;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.extensions.IAffinityData;
import am2.common.extensions.AffinityData;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAffinityTome extends ItemArsMagica {

	
	public ItemAffinityTome() {
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < GameRegistry.findRegistry(Affinity.class).getValues().size(); i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand){
		
		if (par2World.isRemote) return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer, hand);
		
		if (par1ItemStack.getItemDamage() == GameRegistry.findRegistry(Affinity.class).getKey(Affinity.NONE)){
			IAffinityData data = AffinityData.For(par3EntityPlayer);
			data.setLocked(false);
			for (Affinity aff : GameRegistry.findRegistry(Affinity.class).getValues()){
				data.setAffinityDepth(aff, data.getAffinityDepth(aff) * AffinityData.MAX_DEPTH - 20);
			}
		}else{
			AffinityData.For(par3EntityPlayer).incrementAffinity(GameRegistry.findRegistry(Affinity.class).getObjectById(par1ItemStack.getItemDamage()), 20);
		}
		par1ItemStack.setCount(par1ItemStack.getCount()-1);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, par1ItemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		Affinity aff = GameRegistry.findRegistry(Affinity.class).getObjectById(stack.getItemDamage());
		return I18n.format("item.arsmagica2:tome.name", aff.getLocalizedName());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
