package am2.common.armor;

import am2.client.utils.ModelLibrary;
import am2.common.defs.ItemDefs;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFireGuardianEars extends AMArmor{

	public ItemFireGuardianEars(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3, EntityEquipmentSlot par4){
		super(inheritFrom, enumarmormaterial, par3, par4);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return ModelLibrary.instance.fireEars;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format("am2.tooltip.fire_ears"));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
		return "arsmagica2:textures/mobs/bosses/fire_guardian.png";
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		items.add(ItemDefs.fireEarsEnchanted.copy());
	}

	@Override
	public void onArmorTick(World world, EntityPlayer plr, ItemStack stack) {
		super.onArmorTick(world, plr, stack);
		Potion fireResist = Potion.getPotionFromResourceLocation(new ResourceLocation("fire_resistance").toString());
		if (!plr.isPotionActive(fireResist))
			plr.addPotionEffect(new PotionEffect(fireResist));
	}
}
