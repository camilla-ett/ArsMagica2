package am2.common.spell.shape;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.defs.SoundDefs;
import am2.common.items.ItemOre;
import am2.common.power.PowerTypes;
import am2.common.spell.SpellCastResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.EnumSet;

public class Self extends SpellShape {

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		SpellCastResult result = spell.applyComponentsToEntity(world, caster, caster);
		if (result != SpellCastResult.SUCCESS) {
			return result;
		}

		return spell.execute(world, caster, target, x, y, z, null);
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				BlockDefs.aum,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
				ItemDefs.lesserFocus,
				"E:" + PowerTypes.NEUTRAL.ID(), 500
		};
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public float manaCostMultiplier() {
		return 0.5f;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return false;
	}

	@Override
	public SoundEvent getSoundForAffinity(Affinity affinity, SpellData stack, World world) {
		return SoundDefs.CAST_MAP.get(affinity);
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}
}
