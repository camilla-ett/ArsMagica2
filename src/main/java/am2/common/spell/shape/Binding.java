package am2.common.spell.shape;

import am2.api.affinity.Affinity;
import am2.api.extensions.ISpellCaster;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.common.defs.ItemDefs;
import am2.common.defs.SoundDefs;
import am2.common.items.ItemBindingCatalyst;
import am2.common.items.ItemOre;
import am2.common.spell.SpellCastResult;
import am2.common.utils.InventoryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;

public class Binding extends SpellShape {

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		if (!(caster instanceof EntityPlayer)) {
			return SpellCastResult.EFFECT_FAILED;
		}

		EntityPlayer player = (EntityPlayer) caster;
		ItemStack heldStack = player.getActiveItemStack();
		if (heldStack == null || heldStack.getItem() != ItemDefs.spell) {
			return SpellCastResult.EFFECT_FAILED;
		}
		int bindingType = getBindingType(spell);
		switch (bindingType) {
			case ItemBindingCatalyst.META_AXE:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundAxe);
				break;
			case ItemBindingCatalyst.META_PICK:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundPickaxe);
				break;
			case ItemBindingCatalyst.META_SWORD:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundSword);
				break;
			case ItemBindingCatalyst.META_SHOVEL:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundShovel);
				break;
			case ItemBindingCatalyst.META_HOE:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundHoe);
				break;
			case ItemBindingCatalyst.META_BOW:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundBow);
				break;
			case ItemBindingCatalyst.META_SHIELD:
				heldStack = InventoryUtilities.replaceItem(heldStack, ItemDefs.BoundShield);
				break;
		}
		player.inventory.setInventorySlotContents(player.inventory.currentItem, heldStack);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE),
				Items.WOODEN_SWORD,
				Items.STONE_SHOVEL,
				Items.IRON_HOE,
				Items.GOLDEN_AXE,
				Items.DIAMOND_PICKAXE,
				new ItemStack(ItemDefs.bindingCatalyst, 1, OreDictionary.WILDCARD_VALUE)
		};
	}

	@Override
	public float manaCostMultiplier() {
		return 1;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return true;
	}

	@Override
	public SoundEvent getSoundForAffinity(Affinity affinity, SpellData stack, World world) {
		return SoundDefs.BINDING_CAST;
	}


//	public void setBindingType(ItemStack craftStack, ItemStack addedBindingCatalyst){
//		SpellUtils.instance.setSpellMetadata(craftStack, "binding_type", "" + addedBindingCatalyst.getItemDamage());
//	}

	public int getBindingType(SpellData spell) {
		int type = 0;
		try {
			type = spell.getStoredData().getInteger("BindingType");
		} catch (Throwable t) {

		}
		return type;
	}

	public int getBindingType(ISpellCaster spell) {
		int type = 0;
		try {
			type = spell.getCommonStoredData().getInteger("BindingType");
		} catch (Throwable t) {

		}
		return type;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		for (Object obj : recipe) {
			if (obj instanceof ItemStack) {
				ItemStack is = (ItemStack) obj;
				if (is.getItem().equals(ItemDefs.bindingCatalyst))
					tag.setString("BindingType", "" + is.getItemDamage());
			}
		}
	}
}
