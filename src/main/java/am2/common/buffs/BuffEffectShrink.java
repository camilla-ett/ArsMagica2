package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectShrink extends BuffEffect{

	public BuffEffectShrink(int duration, int amplifier){
		super(PotionEffectsDefs.SHRINK, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Shrunken";
	}

}
