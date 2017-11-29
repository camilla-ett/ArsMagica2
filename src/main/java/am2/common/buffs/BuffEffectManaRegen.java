package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectManaRegen extends BuffEffect{

	public BuffEffectManaRegen(int duration, int amplifier){
		super(PotionEffectsDefs.MANA_REGEN, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Mana Regen";
	}

}
