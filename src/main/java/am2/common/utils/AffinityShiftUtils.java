package am2.common.utils;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.event.AffinityChangingEvent;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IEntityExtension;
import am2.api.skill.Skill;
import am2.api.skill.SkillPoint;
import am2.api.spell.*;
import am2.common.defs.ItemDefs;
import am2.common.defs.SkillDefs;
import am2.common.extensions.AffinityData;
import am2.common.extensions.EntityExtension;
import am2.common.extensions.SkillData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Set;

public class AffinityShiftUtils {
	
	public static void doAffinityShift(EntityLivingBase caster, SpellComponent component, SpellShape governingShape){
		if (!(caster instanceof EntityPlayer)) return;
		IAffinityData aff = AffinityData.For(caster);
		Set<Affinity> affList = component.getAffinity();
		for (Affinity affinity : affList){
			float shift = component.getAffinityShift(affinity) * aff.getDiminishingReturnsFactor() * 5;
			//float xp = 0.05f * aff.getDiminishingReturnsFactor();
			if (governingShape.isChanneled()){
				shift /= 4;
				//xp /= 4;
			}
			
			if (caster instanceof EntityPlayer){
				if (SkillData.For(caster).hasSkill(SkillDefs.AFFINITY_GAINS.getID())){
					shift *= 1.1f;
					//xp *= 0.9f;
				}
//				ItemStack chestArmor = ((EntityPlayer)caster).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
//				if (chestArmor != null && ArmorHelper.isInfusionPreset(chestArmor, GenericImbuement.magicXP))
//					xp *= 1.25f;
			}

			if (shift > 0){
				AffinityChangingEvent event = new AffinityChangingEvent((EntityPlayer)caster, affinity, shift);
				MinecraftForge.EVENT_BUS.post(event);
				if (!event.isCanceled())
					aff.incrementAffinity(affinity, event.amount);
			}
//			if (xp > 0){
//				xp *= caster.getAttributeMap().getAttributeInstance(ArsMagicaAPI.xpGainModifier).getAttributeValue();
//				EntityExtension.For(caster).addMagicXP(xp);
//			}
		}
		aff.addDiminishingReturns(governingShape.isChanneled());
	}
	
	public static ItemStack getEssenceForAffinity (Affinity affinity) {
		int meta = 0;
		for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()) {
			if (aff.equals(affinity))
				break;
			meta++;
		}
		return new ItemStack(ItemDefs.essence, 1, meta);
	}

	public static float calculateXPGains(EntityLivingBase caster, SpellData data) {
		IEntityExtension extension = EntityExtension.For(caster);
		float modMultiplier = 0.1F;
		float total = 0F;
		for (List<AbstractSpellPart> parts : data.getStages()) {
			float base = 0F;
			float localMultiplier = 1.0F;
			for (AbstractSpellPart part : parts) {
				float multiplier = 0.1F;
				Skill skill = ArsMagicaAPI.getSkillRegistry().getObject(part.getRegistryName());
				if (skill != null) {
					SkillPoint point = skill.getPoint();
					if (point.getTier() > 0) {
						multiplier *= extension.getCurrentLevel() > point.getMinEarnLevel() + 10 ? Math.max(1F / (extension.getCurrentLevel() - point.getMinEarnLevel() - 10F), 0.1F) : 1F;
					} else
						multiplier *= 2F;
				}
				if (part instanceof SpellComponent)
					base += ((SpellComponent) part).manaCost() * multiplier;
				else if (part instanceof SpellModifier)
					modMultiplier *= (1+multiplier);
				else if (part instanceof SpellShape)
					localMultiplier *= 10 * multiplier * (((SpellShape) part).isChanneled() ? 0.1F : 1F);

			}
			total += base * localMultiplier;
		}
		return total * modMultiplier * 0.01F;
	}
}
