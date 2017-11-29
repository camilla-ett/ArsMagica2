package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectBurnoutReduction extends BuffEffect{
	public BuffEffectBurnoutReduction(int duration, int amplifier){
		super(PotionEffectsDefs.BURNOUT_REDUCTION, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Burnout Reduction";
	}
}
