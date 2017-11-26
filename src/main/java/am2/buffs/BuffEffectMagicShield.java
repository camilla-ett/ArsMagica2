package am2.buffs;

import am2.defs.PotionEffectsDefs;

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
