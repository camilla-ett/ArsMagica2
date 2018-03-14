package am2.common.spell.shape;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.defs.SoundDefs;
import am2.common.extensions.EntityExtension;
import am2.common.items.ItemOre;
import am2.common.spell.ContingencyType;
import am2.common.spell.SpellCastResult;
import am2.common.utils.AffinityShiftUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.EnumSet;

public class Contingency_Hit extends SpellShape {

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				Items.CLOCK,
				AffinityShiftUtils.getEssenceForAffinity(Affinity.LIGHTNING),
				Items.DIAMOND_SWORD,
				BlockDefs.tarmaRoot,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE),
				"E:*", 5000
		};
	}

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		EntityExtension.For(target != null ? target : caster).setContingency(ContingencyType.DAMAGE, spell);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}


	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public float manaCostMultiplier() {
		return 10;
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
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}

	@Override
	public SoundEvent getSoundForAffinity(Affinity affinity, SpellData stack, World world) {
		return SoundDefs.CONTINGENCY;
	}
}