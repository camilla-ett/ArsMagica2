package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;

public class BuffEffectTrueSight extends BuffEffectShield{

	public BuffEffectTrueSight(int duration, int amplifier) {
		super(PotionEffectsDefs.TRUE_SIGHT, duration, amplifier);
	}

	@Override
	protected String spellBuffName(){
		return "True Sight";
	}

}
