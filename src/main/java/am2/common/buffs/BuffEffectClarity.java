package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectClarity extends BuffEffect{

	public BuffEffectClarity(int duration, int amplifier){
		super(PotionEffectsDefs.CLARITY, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Clarity";
	}

}
