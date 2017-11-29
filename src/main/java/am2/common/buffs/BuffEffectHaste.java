package am2.common.buffs;

import am2.common.defs.IDDefs;
import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class BuffEffectHaste extends BuffEffect{

	private static final AttributeModifier hasteSpeedBoost = (new AttributeModifier(IDDefs.hasteID, "Haste Speed Boost", 0.2D, 2));

	public BuffEffectHaste(int duration, int amplifier){
		super(PotionEffectsDefs.HASTE, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
		IAttributeInstance attributeinstance = entityliving.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		if (attributeinstance.getModifier(IDDefs.hasteID) != null){
			attributeinstance.removeModifier(hasteSpeedBoost);
		}
		
		attributeinstance.applyModifier(new AttributeModifier(IDDefs.hasteID, "Haste Speed Boost", 0.2D + (0.35 * getAmplifier()), 2));
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		IAttributeInstance attributeinstance = entityliving.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		if (attributeinstance.getModifier(IDDefs.hasteID) != null){
			attributeinstance.removeModifier(hasteSpeedBoost);
		}
	}

	@Override
	protected String spellBuffName(){
		return "Haste";
	}

}
