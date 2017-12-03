package am2.api.event;

import am2.api.spell.SpellData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellCastEvent extends Event {
	
	public SpellData spell;
	public float manaCost;
	public EntityLivingBase entityLiving;
	public float burnout;
	
	public SpellCastEvent(EntityLivingBase caster, SpellData spell, float manaCost) {
		this.spell = spell;
		this.manaCost = manaCost;
		this.entityLiving = caster;
	}
	
	public static class Pre extends SpellCastEvent {


		public Pre(EntityLivingBase caster, SpellData spell, float manaCost) {
			super(caster, spell, manaCost);
		}
		
	}
	
	public static class Post extends SpellCastEvent {

		public Post(EntityLivingBase caster, SpellData spell, float manaCost) {
			super(caster, spell, manaCost);
		}
		
	}

}
