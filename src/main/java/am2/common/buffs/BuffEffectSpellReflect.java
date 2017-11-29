package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectSpellReflect extends BuffEffect{

	public BuffEffectSpellReflect(int duration, int amplifier){
		super(PotionEffectsDefs.SPELL_REFLECT, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Spell Reflect";
	}

}
