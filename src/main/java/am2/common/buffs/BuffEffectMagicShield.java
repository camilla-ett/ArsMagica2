package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;

public class BuffEffectMagicShield extends BuffEffectShield{

	public BuffEffectMagicShield(int duration,
								 int amplifier){
		super(PotionEffectsDefs.MAGIC_SHIELD, duration, amplifier);
	}

	@Override
	protected String spellBuffName(){
		return "Magic Shield";
	}

}
