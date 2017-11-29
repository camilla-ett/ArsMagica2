package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffMaxManaIncrease extends BuffEffect{

	public BuffMaxManaIncrease(int duration, int amplifier){
		super(PotionEffectsDefs.MANA_BOOST, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Mana Boost";
	}

}
