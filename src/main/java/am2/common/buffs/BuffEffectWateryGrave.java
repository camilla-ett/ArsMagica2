package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectWateryGrave extends BuffEffect{

	public BuffEffectWateryGrave(int duration, int amplifier){
		super(PotionEffectsDefs.WATERY_GRAVE, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Watery Grave";
	}

}
