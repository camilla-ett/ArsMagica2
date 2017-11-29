package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectGravityWell extends BuffEffect{

	public BuffEffectGravityWell(int duration, int amplifier){
		super(PotionEffectsDefs.GRQVITY_WELL, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){

	}

	@Override
	protected String spellBuffName(){
		return "Gravity Well";
	}

}
