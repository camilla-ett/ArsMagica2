package am2.common.spell;


import java.util.ArrayList;
import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.SpellRegistry;
import am2.api.event.SpellCastEvent;
import am2.api.skill.Skill;
import am2.api.spell.AbstractSpellPart;
import am2.api.spell.SpellData;
import am2.common.defs.SkillDefs;
import am2.common.extensions.EntityExtension;
import am2.common.extensions.SkillData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SpellUnlockManager{

	private ArrayList<UnlockEntry> entries;

	public SpellUnlockManager(){
		init();
	}

	@SubscribeEvent
	public void onSpellCast(SpellCastEvent.Pre event){
		if (event.entityLiving instanceof EntityPlayer){
			if (EntityExtension.For(event.entityLiving).getCurrentMana() < event.manaCost)
				return;
			for (UnlockEntry entry : entries){
				//check unlocks
				if (!event.entityLiving.world.isRemote){
					if (entry.willSpellUnlock(event.spell)){
						entry.unlockFor((EntityPlayer)event.entityLiving);
					}
				}
			}
		}
	}

	public void init(){
		entries = new ArrayList<UnlockEntry>();
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:falling_star")),
				SpellRegistry.getComponentFromName("arsmagica2:magic_damage"),
				SpellRegistry.getModifierFromName("arsmagica2:gravity"),
				SpellRegistry.getComponentFromName("arsmagica2:astral_distortion")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:blizzard")),
				SpellRegistry.getComponentFromName("arsmagica2:storm"),
				SpellRegistry.getComponentFromName("arsmagica2:frost_damage"),
				SpellRegistry.getComponentFromName("arsmagica2:freeze"),
				SpellRegistry.getModifierFromName("arsmagica2:damage")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:fire_rain")),
				SpellRegistry.getComponentFromName("arsmagica2:storm"),
				SpellRegistry.getComponentFromName("arsmagica2:fire_damage"),
				SpellRegistry.getComponentFromName("arsmagica2:ignition"),
				SpellRegistry.getModifierFromName("arsmagica2:damage")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:mana_blast")),
				SpellRegistry.getComponentFromName("arsmagica2:magic_damage"),
				SpellRegistry.getModifierFromName("arsmagica2:damage")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:dismembering")),
				SpellRegistry.getModifierFromName("arsmagica2:piercing"),
				SpellRegistry.getModifierFromName("arsmagica2:damage")));

		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:mana_link")),
				SpellRegistry.getComponentFromName("arsmagica2:mana_drain"),
				SpellRegistry.getComponentFromName("arsmagica2:entangle")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:mana_shield")),
				SpellRegistry.getComponentFromName("arsmagica2:shield"),
				SpellRegistry.getComponentFromName("arsmagica2:reflect"),
				SpellRegistry.getComponentFromName("arsmagica2:life_tap")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:buff_power")),
				SpellRegistry.getComponentFromName("arsmagica2:haste"),
				SpellRegistry.getComponentFromName("arsmagica2:slowfall"),
				SpellRegistry.getComponentFromName("arsmagica2:swift_swim"),
				SpellRegistry.getComponentFromName("arsmagica2:gravity_well"),
				SpellRegistry.getComponentFromName("arsmagica2:leap")));

		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:daylight")),
				SpellRegistry.getComponentFromName("arsmagica2:true_sight"),
				SpellRegistry.getComponentFromName("arsmagica2:divine_intervention"),
				SpellRegistry.getComponentFromName("arsmagica2:light")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:moonrise")),
				SpellRegistry.getComponentFromName("arsmagica2:night_vision"),
				SpellRegistry.getComponentFromName("arsmagica2:ender_intervention"),
				SpellRegistry.getModifierFromName("arsmagica2:lunar")));
		entries.add(new UnlockEntry(GameRegistry.findRegistry(Skill.class).getValue(new ResourceLocation("arsmagica2:prosperity")),
				SpellRegistry.getComponentFromName("arsmagica2:dig"),
				SpellRegistry.getModifierFromName("arsmagica2:feather_touch"),
				SpellRegistry.getModifierFromName("arsmagica2:mining_power")));

		entries.add(new UnlockEntry(SkillDefs.SHIELD_OVERLOAD,
				SpellRegistry.getComponentFromName("arsmagica2:mana_shield"),
				SpellRegistry.getComponentFromName("arsmagica2:mana_drain")));
		
	}

	class UnlockEntry{
		private Skill unlock;
		private AbstractSpellPart[] requiredComponents;

		public UnlockEntry(Skill unlock, AbstractSpellPart... components){
			this.unlock = unlock;
			this.requiredComponents = components;
		}

		public boolean partIsInStage(SpellData spell, AbstractSpellPart part, int stage){
			if (part == null)
				return false;
			for (List<AbstractSpellPart> parts : spell.getStages())
				for (AbstractSpellPart p : parts)
					if (part.getClass().isInstance(p))
						return true;
			
			return false;
		}

		public boolean willSpellUnlock(SpellData spell){
			boolean found = true;
			for (AbstractSpellPart part : requiredComponents){
				if (!partIsInStage(spell, part, 0)){
					found = false;
					break;
				}
			}
			if (found)
				return true;
			return false;
		}

		public void unlockFor(EntityPlayer player){
			if (!player.world.isRemote){
				SkillData.For(player).unlockSkill(unlock.getID());
			}
		}
	}
}
