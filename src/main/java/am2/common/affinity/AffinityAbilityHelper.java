package am2.common.affinity;

import java.util.Map.Entry;

import am2.ArsMagica2;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.api.event.SpellCastEvent;
import am2.common.affinity.abilities.AbilityAgile;
import am2.common.affinity.abilities.AbilityAntiEndermen;
import am2.common.affinity.abilities.AbilityClearCaster;
import am2.common.affinity.abilities.AbilityColdBlooded;
import am2.common.affinity.abilities.AbilityExpandedLungs;
import am2.common.affinity.abilities.AbilityFastHealing;
import am2.common.affinity.abilities.AbilityFirePunch;
import am2.common.affinity.abilities.AbilityFireResistance;
import am2.common.affinity.abilities.AbilityFireWeakness;
import am2.common.affinity.abilities.AbilityFluidity;
import am2.common.affinity.abilities.AbilityFulmination;
import am2.common.affinity.abilities.AbilityLavaFreeze;
import am2.common.affinity.abilities.AbilityLeafLike;
import am2.common.affinity.abilities.AbilityLightAsAFeather;
import am2.common.affinity.abilities.AbilityLightningStep;
import am2.common.affinity.abilities.AbilityMagicWeakness;
import am2.common.affinity.abilities.AbilityNightVision;
import am2.common.affinity.abilities.AbilityOneWithMagic;
import am2.common.affinity.abilities.AbilityPacifist;
import am2.common.affinity.abilities.AbilityPhotosynthesis;
import am2.common.affinity.abilities.AbilityPoisonResistance;
import am2.common.affinity.abilities.AbilityReflexes;
import am2.common.affinity.abilities.AbilityRelocation;
import am2.common.affinity.abilities.AbilityRooted;
import am2.common.affinity.abilities.AbilityShortCircuit;
import am2.common.affinity.abilities.AbilitySolidBones;
import am2.common.affinity.abilities.AbilitySunlightWeakness;
import am2.common.affinity.abilities.AbilitySwiftSwim;
import am2.common.affinity.abilities.AbilityThorns;
import am2.common.affinity.abilities.AbilityThunderPunch;
import am2.common.affinity.abilities.AbilityWaterFreeze;
import am2.common.affinity.abilities.AbilityWaterWeakness;
import am2.common.extensions.AffinityData;
import am2.common.packet.AMDataWriter;
import am2.common.packet.AMNetHandler;
import am2.common.packet.AMPacketIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AffinityAbilityHelper {
	
	static {
		//AIR
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityLightAsAFeather());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityAgile());
		
		//ARCANE
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityClearCaster());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityMagicWeakness());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityOneWithMagic());
		
		//EARTH
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilitySolidBones());
		
		//ENDER
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityRelocation());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityNightVision());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityWaterWeakness(Affinity.ENDER));
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityPoisonResistance());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilitySunlightWeakness());
		
		//FIRE
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFireResistance());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFirePunch());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityWaterWeakness(Affinity.FIRE));
		
		//ICE
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityLavaFreeze());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityWaterFreeze());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityColdBlooded());
		
		//LIFE
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFastHealing());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityPacifist());
		
		//WATER
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityExpandedLungs());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFluidity());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilitySwiftSwim());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFireWeakness());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityAntiEndermen());
		
		//NATURE
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityRooted());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityThorns());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityLeafLike());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityPhotosynthesis());
		
		//LIGHTNING
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityLightningStep());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityReflexes());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityFulmination());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityShortCircuit());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityThunderPunch());
		GameRegistry.findRegistry(AbstractAffinityAbility.class).register(new AbilityWaterWeakness(Affinity.LIGHTNING));
	}
	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
			if (ability.getKey() != null && ability.getKey().isPressed()) {
				EntityPlayer player = ArsMagica2.proxy.getLocalPlayer();
				if (ability.canApply(player)) {
					AMDataWriter syncPacket = new AMDataWriter();
					syncPacket.add(player.getEntityId());
					syncPacket.add(ability.getRegistryName().toString());
					ability.applyKeyPress(player);
					AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.KEY_ABILITY_PRESS, syncPacket.generate());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			if (!event.getEntityLiving().world.isRemote) {
				for (Entry<String, Integer> entry : AffinityData.For(event.getEntityLiving()).getCooldowns().entrySet()) {
					if (entry.getValue() > 0)
						AffinityData.For(event.getEntityLiving()).addCooldown(entry.getKey(), entry.getValue() - 1);
				}
			}
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getEntityLiving()))
					ability.applyTick((EntityPlayer) event.getEntityLiving());
				else
					ability.removeEffects((EntityPlayer) event.getEntityLiving());
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getEntityLiving()))
					ability.applyHurt((EntityPlayer) event.getEntityLiving(), event, false);
			}
		}
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getSource().getEntity()))
					ability.applyHurt((EntityPlayer) event.getSource().getEntity(), event, true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getEntityLiving()))
					ability.applyFall((EntityPlayer) event.getEntityLiving(), event);
			}
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getEntityLiving()))
					ability.applyDeath((EntityPlayer) event.getEntityLiving(), event);
			}
		}
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getSource().getEntity()))
					ability.applyKill((EntityPlayer) event.getSource().getEntity(), event);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.getEntityLiving()))
					ability.applyJump((EntityPlayer) event.getEntityLiving(), event);
			}
		}
	}
	
	@SubscribeEvent
	public void onSpellCast(SpellCastEvent.Post event) {
		if (event.entityLiving instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.entityLiving))
					ability.applySpellCast((EntityPlayer) event.entityLiving, event);
			}
		}
	}
	
	@SubscribeEvent
	public void onPreSpellCast(SpellCastEvent.Pre event) {
		if (event.entityLiving instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.canApply((EntityPlayer) event.entityLiving))
					ability.applyPreSpellCast((EntityPlayer) event.entityLiving, event);
			}
		}
	}
}
