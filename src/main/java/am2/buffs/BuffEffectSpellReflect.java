package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import am2.defs.PotionEffectsDefs;

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
