package am2.common.entity.ai.selectors;

import com.google.common.base.Predicate;

import am2.common.entity.EntityLightMage;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCreeper;

public class LightMageEntitySelector implements Predicate<EntityCreature>{

	public static final LightMageEntitySelector instance = new LightMageEntitySelector();

	private LightMageEntitySelector(){
	}

	@Override
	public boolean apply(EntityCreature entity){
		if (entity instanceof EntityCreeper || entity instanceof EntityLightMage)
			return false;
		return true;
	}

}
