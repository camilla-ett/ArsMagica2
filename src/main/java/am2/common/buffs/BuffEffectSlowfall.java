package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectSlowfall extends BuffEffect{

	public BuffEffectSlowfall(int duration, int amplifier){
		super(PotionEffectsDefs.SLOWFALL, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Slowfall";
	}

}
