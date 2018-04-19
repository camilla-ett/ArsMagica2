package am2.api.handlers;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.event.SpellSoundMapEvent;
import am2.common.LogHelper;
import am2.common.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class SoundHandler {

    public static Map<Affinity, SoundEvent> LOOP_MAP;
    public static Map<Affinity, SoundEvent> CAST_MAP;

    public static SoundEvent LOOP_AIR = getLoopSound("air");
    public static SoundEvent LOOP_ARCANE = getLoopSound("arcane");
    public static SoundEvent LOOP_EARTH = getLoopSound("earth");
    public static SoundEvent LOOP_ENDER = getLoopSound("ender");
    public static SoundEvent LOOP_FIRE = getLoopSound("fire");
    public static SoundEvent LOOP_ICE = getLoopSound("ice");
    public static SoundEvent LOOP_LIFE = getLoopSound("life");
    public static SoundEvent LOOP_LIGHTNING = getLoopSound("lightning");
    public static SoundEvent LOOP_NATURE = getLoopSound("nature");
    public static SoundEvent LOOP_NONE = getLoopSound("none");
    public static SoundEvent LOOP_WATER = getLoopSound("water");

    public static SoundEvent CAST_AIR = getCastSound("air");
    public static SoundEvent CAST_ARCANE = getCastSound("arcane");
    public static SoundEvent CAST_EARTH = getCastSound("earth");
    public static SoundEvent CAST_ENDER = getCastSound("ender");
    public static SoundEvent CAST_FIRE = getCastSound("fire");
    public static SoundEvent CAST_ICE = getCastSound("ice");
    public static SoundEvent CAST_LIFE = getCastSound("life");
    public static SoundEvent CAST_LIGHTNING = getCastSound("lightning");
    public static SoundEvent CAST_NATURE = getCastSound("nature");
    public static SoundEvent CAST_NONE = getCastSound("none");
    public static SoundEvent CAST_WATER = getCastSound("water");

    public static SoundEvent RUNE_CAST = new SoundEvent ( new ResourceLocation ( ArsMagica2.MODID , "spell.rune.cast" ) ).setRegistryName ( new ResourceLocation ( ArsMagica2.MODID , "spell.rune.cast" ) );
    public static SoundEvent CONTINGENCY = new SoundEvent ( new ResourceLocation ( ArsMagica2.MODID , "spell.contingency.cast" ) ).setRegistryName ( new ResourceLocation ( ArsMagica2.MODID , "spell.contingency.cast" ) );
    public static SoundEvent BINDING_CAST = new SoundEvent ( new ResourceLocation ( ArsMagica2.MODID , "spell.binding.cast" ) ).setRegistryName ( new ResourceLocation ( ArsMagica2.MODID , "spell.binding.cast" ) );



    public static final SoundEvent AIR_GUARDIAN_HIT = register("arsmagica2:mob.airguardian.hit");
    public static final SoundEvent AIR_GUARDIAN_DEATH = register("arsmagica2:mob.airguardian.death");
    public static final SoundEvent AIR_GUARDIAN_IDLE = register("arsmagica2:mob.airguardian.idle");

    public static final SoundEvent ARCANE_GUARDIAN_HIT = register("arsmagica2:mob.arcaneguardian.hit");
    public static final SoundEvent ARCANE_GUARDIAN_DEATH = register("arsmagica2:mob.arcaneguardian.death");
    public static final SoundEvent ARCANE_GUARDIAN_IDLE = register("arsmagica2:mob.arcaneguardian.idle");
    public static final SoundEvent ARCANE_GUARDIAN_SPELL = register("arsmagica2:mob.arcaneguardian.spell");

    public static final SoundEvent LIGHTNING_GUARDIAN_IDLE = register("arsmagica2:mob.lightningguardian.idle");
    public static final SoundEvent LIGHTNING_GUARDIAN_ATTACK = register("arsmagica2:mob.lightningguardian.attack");
    public static final SoundEvent LIGHTNING_GUARDIAN_ATTACK_STATIC = register("arsmagica2:mob.lightningguardian.attack_static");
    public static final SoundEvent LIGHTNING_GUARDIAN_LIGHTNING_ROD_1 = register("arsmagica2:mob.lightningguardian.lightning_rod_1");
    public static final SoundEvent LIGHTNING_GUARDIAN_LIGHTNING_ROD_START = register("arsmagica2:mob.lightningguardian.lightning_rod_start");
    public static final SoundEvent LIGHTNING_GUARDIAN_STATIC = register("arsmagica2:mob.lightningguardian.static");
    public static final SoundEvent LIGHTNING_GUARDIAN_HIT = register("arsmagica2:mob.lightningguardian.hit");
    public static final SoundEvent LIGHTNING_GUARDIAN_DEATH = register("arsmagica2:mob.lightningguardian.death");

    public static final SoundEvent NATURE_GUARDIAN_WHIRL_LOOP = register("arsmagica2:mob.natureguardian.whirlloop");
    public static final SoundEvent NATURE_GUARDIAN_HIT = register("arsmagica2:mob.natureguardian.hit");
    public static final SoundEvent NATURE_GUARDIAN_IDLE = register("arsmagica2:mob.natureguardian.idle");
    public static final SoundEvent NATURE_GUARDIAN_DEATH = register("arsmagica2:mob.natureguardian.death");
    public static final SoundEvent NATURE_GUARDIAN_ATTACK = register("arsmagica2:mob.natureguardian.attack");

    public static final SoundEvent LIFE_GUARDIAN_SUMMON = register("arsmagica2:mob.lifeguardian.summon");
    public static final SoundEvent LIFE_GUARDIAN_HIT = register("arsmagica2:mob.lifeguardian.hit");
    public static final SoundEvent LIFE_GUARDIAN_DEATH = register("arsmagica2:mob.lifeguardian.death");
    public static final SoundEvent LIFE_GUARDIAN_IDLE = register("arsmagica2:mob.lifeguardian.idle");
    public static final SoundEvent LIFE_GUARDIAN_HEAL = register("arsmagica2:mob.lifeguardian.heal");

    public static final SoundEvent WINTER_GUARDIAN_LAUNCH_ARM = register("arsmagica2:mob.winterguardian.launcharm");
    public static final SoundEvent WINTER_GUARDIAN_IDLE = register("arsmagica2:mob.winterguardian.idle");
    public static final SoundEvent WINTER_GUARDIAN_HIT = register("arsmagica2:mob.winterguardian.hit");
    public static final SoundEvent WINTER_GUARDIAN_DEATH = register("arsmagica2:mob.winterguardian.death");
    public static final SoundEvent WINTER_GUARDIAN_ATTACK = register("arsmagica2:mob.winterguardian.attack");

    public static final SoundEvent EARTH_GUARDIAN_HIT = register("arsmagica2:mob.earthguardian.hit");
    public static final SoundEvent EARTH_GUARDIAN_DEATH = register("arsmagica2:mob.earthguardian.death");
    public static final SoundEvent EARTH_GUARDIAN_IDLE = register("arsmagica2:mob.earthguardian.idle");
    public static final SoundEvent EARTH_GUARDIAN_ATTACK = register("arsmagica2:mob.earthguardian.attack");

    public static final SoundEvent ENDER_GUARDIAN_ROAR = register("arsmagica2:mob.enderguardian.roar");
    public static final SoundEvent ENDER_GUARDIAN_FLAP = register("arsmagica2:mob.enderguardian.flap");
    public static final SoundEvent ENDER_GUARDIAN_HIT = register("arsmagica2:mob.enderguardian.hit");
    public static final SoundEvent ENDER_GUARDIAN_DEATH = register("arsmagica2:mob.enderguardian.death");
    public static final SoundEvent ENDER_GUARDIAN_IDLE = register("arsmagica2:mob.enderguardian.idle");
    public static final SoundEvent ENDER_GUARDIAN_ATTACK = register("arsmagica2:mob.enderguardian.attack");

    public static final SoundEvent FIRE_GUARDIAN_HIT = register("arsmagica2:mob.fireguardian.hit");
    public static final SoundEvent FIRE_GUARDIAN_DEATH = register("arsmagica2:mob.fireguardian.death");
    public static final SoundEvent FIRE_GUARDIAN_IDLE = register("arsmagica2:mob.fireguardian.idle");
    public static final SoundEvent FIRE_GUARDIAN_ATTACK = register("arsmagica2:mob.fireguardian.attack");

    public static final SoundEvent WATER_GUARDIAN_HIT = register("arsmagica2:mob.waterguardian.hit");
    public static final SoundEvent WATER_GUARDIAN_IDLE = register("arsmagica2:mob.waterguardian.idle");
    public static final SoundEvent WATER_GUARDIAN_DEATH = register("arsmagica2:mob.waterguardian.death");
    public static final SoundEvent WATER_GUARDIAN_ATTACK = register("arsmagica2:mob.waterguardian.attack");

    public static final SoundEvent MANA_ELEMENTAL_HIT = register("arsmagica2:mob.manaelemental.hit");
    public static final SoundEvent MANA_ELEMENTAL_IDLE = register("arsmagica2:mob.manaelemental.living");
    public static final SoundEvent MANA_ELEMENTAL_DEATH = register("arsmagica2:mob.manaelemental.death");

    public static final SoundEvent HECATE_IDLE = register("arsmagica2:mob.hecate.idle");
    public static final SoundEvent HECATE_DEATH = register("arsmagica2:mob.hecate.death");
    public static final SoundEvent HECATE_HIT = register("arsmagica2:mob.hecate.hit");

    public static final SoundEvent GATEWAY_OPEN = register("arsmagica2:misc.gateway.open");
    public static final SoundEvent RECONSTRUCTOR_COMPLETE = register("arsmagica2:misc.reconstructor.complete");
    public static final SoundEvent CALEFACTOR_BURN = register("arsmagica2:misc.calefactor.burn");
    public static final SoundEvent CRAFTING_ALTAR_CREATE_SPELL = register("arsmagica2:misc.craftingaltar.create_spell");

    public static final SoundEvent MOO_IDLE = register("arsmagica2:mob.moo.idle");
    public static final SoundEvent MOO_DEATH = register("arsmagica2:mob.moo.death");
    public static final SoundEvent MOO_HIT = register("arsmagica2:mob.moo.hit");

    private static SoundEvent register(String rl){
        SoundEvent temp = new SoundEvent(new ResourceLocation(rl));
        LogHelper.info("Adding sound to Registry: " + rl);
        Registry.GetSoundsToRegister().add(temp.setRegistryName(new ResourceLocation(rl)));
        return temp;
    }
    private static void register(SoundEvent event) {
        LogHelper.info ( "Adding sound to Registry: " + event.getSoundName ( ) );
        Registry.GetSoundsToRegister ( ).add ( event ); //.setRegistryName(event.getSoundName())) Maybe this is not needed.
    }

    private static SoundEvent getLoopSound(String aff) {
        ResourceLocation rl = new ResourceLocation(ArsMagica2.MODID, "spell.loop." + aff);
        return new SoundEvent(rl).setRegistryName(rl);
    }

    private static SoundEvent getCastSound(String aff) {
        ResourceLocation rl = new ResourceLocation(ArsMagica2.MODID, "spell.cast." + aff);
        return new SoundEvent(rl).setRegistryName(rl);
    }

    public static void registerSounds() {
        register(LOOP_AIR);
        register(LOOP_ARCANE);
        register(LOOP_EARTH);
        register(LOOP_ENDER);
        register(LOOP_FIRE);
        register(LOOP_ICE);
        register(LOOP_LIFE);
        register(LOOP_LIGHTNING);
        register(LOOP_NATURE);
        register(LOOP_NONE);
        register(LOOP_WATER);

        register(CAST_AIR);
        register(CAST_ARCANE);
        register(CAST_EARTH);
        register(CAST_ENDER);
        register(CAST_FIRE);
        register(CAST_ICE);
        register(CAST_LIFE);
        register(CAST_LIGHTNING);
        register(CAST_NATURE);
        register(CAST_NONE);
        register(CAST_WATER);

        register(RUNE_CAST);
        register(CONTINGENCY);
        register(BINDING_CAST);
    }


    public static void createSoundMaps() {
        SpellSoundMapEvent event = new SpellSoundMapEvent(new ResourceLocation(ArsMagica2.MODID, "loop"));
        event.put(Affinity.AIR, LOOP_AIR);
        event.put(Affinity.ARCANE, LOOP_ARCANE);
        event.put(Affinity.EARTH, LOOP_EARTH);
        event.put(Affinity.ENDER, LOOP_ENDER);
        event.put(Affinity.FIRE, LOOP_FIRE);
        event.put(Affinity.ICE, LOOP_ICE);
        event.put(Affinity.LIFE, LOOP_LIFE);
        event.put(Affinity.LIGHTNING, LOOP_LIGHTNING);
        event.put(Affinity.NATURE, LOOP_NATURE);
        event.put(Affinity.NONE, LOOP_NONE);
        event.put(Affinity.WATER, LOOP_WATER);
        MinecraftForge.EVENT_BUS.post(event);
        LOOP_MAP = event.getMap();

        event = new SpellSoundMapEvent(new ResourceLocation(ArsMagica2.MODID, "cast"));
        event.put(Affinity.AIR, CAST_AIR);
        event.put(Affinity.ARCANE, CAST_ARCANE);
        event.put(Affinity.EARTH, CAST_EARTH);
        event.put(Affinity.ENDER, CAST_ENDER);
        event.put(Affinity.FIRE, CAST_FIRE);
        event.put(Affinity.ICE, CAST_ICE);
        event.put(Affinity.LIFE, CAST_LIFE);
        event.put(Affinity.LIGHTNING, CAST_LIGHTNING);
        event.put(Affinity.NATURE, CAST_NATURE);
        event.put(Affinity.NONE, CAST_NONE);
        event.put(Affinity.WATER, CAST_WATER);
        MinecraftForge.EVENT_BUS.post(event);
        CAST_MAP = event.getMap();

    }
}
