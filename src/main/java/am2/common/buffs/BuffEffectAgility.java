package am2.common.buffs;

import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectAgility extends BuffEffect{

	public BuffEffectAgility(int duration, int amplifier){
		super(PotionEffectsDefs.AGILITY, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		entityliving.setAIMoveSpeed(entityliving.getAIMoveSpeed() * 1.2f);
	}

	@Override
	protected String spellBuffName(){
		return "Agility";
	}

}
