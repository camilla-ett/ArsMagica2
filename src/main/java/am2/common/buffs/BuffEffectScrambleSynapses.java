package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectScrambleSynapses extends BuffEffect{

	public BuffEffectScrambleSynapses(int duration, int amplifier){
		super(PotionEffectsDefs.SCRAMBLE_SYNAPSES, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Scramble Synapses";
	}

}
