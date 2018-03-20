package am2.common.config;

import java.io.File;

import am2.api.math.AMVector2;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleController;
import am2.common.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AMConfig extends Configuration{

	private final String KEY_PlayerSpellsDamageTerrain = "Player_Spells_Destroy_Terrain";
	private final String KEY_NPCSpellsDamageTerrain = "NPC_Spells_Destroy_Terrain";
//	private final String KEY_TowergenGridSize = "Towergen_Grid_Size";
//	private final String KEY_EnableWorldGen = "EnableWorldGen";
	private final String KEY_RetroactiveWorldGen = "RetroactiveWorldGen";

	public static final float MANA_BURNOUT_RATIO = 0.38f;

	private final String KEY_OldCalculations = "OldXPCalcultations";
	
	private final String KEY_SecondarySkillTreeTierCap = "SecondarySkillTreeTierCap";
	private final String KEY_DigBreaksTEs = "DigBreaksTileEntities";
	private final String KEY_DisplayManaInInventory = "DisplayManaInInventory";
//	private final String KEY_SpellBookUIPosition = "SpellBookUIPosition";
	private final String KEY_ManaCap = "Mana_Cap";
	private final String KEY_mageSpawnRate = "MageSpawnRate";
	private final String KEY_waterElementalSpawnRate = "WaterElementalSpawnRate";
	private final String KEY_hecateSpawnRate = "HecateSpawnRate";
	private final String KEY_dryadSpawnRate = "DryadSpawnRate";
	private final String KEY_manaElementalSpawnRate = "ManaElementalSpawnRate";
	private final String KEY_manaCreeperSpawnRate = "ManaCreeperSpawnRate";
	private final String KEY_darklingSpawnRate = "DarklingSpawnRate";
	private final String KEY_earthElementalSpawnRate = "EarthElementalSpawnRate";
	private final String KEY_fireElementalSpawnRate = "FireElementalSpawnRate";
	private final String KEY_flickerSpawnRate = "FlickerSpawnRate";

//	private final String KEY_RandomSpellRecipes = "RandomSpellRecipes";
	private final String KEY_DamageMultiplier = "DamageMultiplier";

	private final String KEY_UseSpecialRenderers = "Use_Special_Renderers";
//	private final String KEY_LowResParticles = "Low_Res_Particles";
	private final String KEY_FrictionCoefficient = "FrictionCoefficient";

	private final String KEY_MageVillagerProfessionID = "mage_villager_profession_id";

	private final String KEY_DigDisabledBlocks = "dig_blacklist";
	private final String KEY_WorldgenBlacklist = "worldgen_blacklist";

//	private final String KEY_GetRandomSpellNames = "suggest_spell_names";
	private final String KEY_DisarmAffectsPlayers = "disarm_affects_players";

//	private final String KEY_MMFBiomeID = "MMFBiomeID";
//	private final String KEY_MMFDimensionID = "MMFDimensionID";
	private final String KEY_WitchwoodForestBiomeID = "WitchwoodForestBiomeID";
	private final String KEY_WitchwoodForestRarity = "Witchwood_Forest_Biome_Rarity";

	private final String KEY_ForgeSmeltsVillagers = "ForgeSmeltsVillagers";
	private final String KEY_EverstoneRepairRate = "EverstoneRepairRate";

	private final String KEY_witchwoodLeavesFall = "WitchwoodLeafParticles";
	private final String KEY_CandlesAreRovingLights = "CandlesAreRovingLights";
	private final String KEY_Appropriation_Block_Blacklist = "Appropriation_Block_Blacklist";
	private final String KEY_Appropriation_Mob_Blacklist = "Appropriation_Mob_Blacklist";

	private final String KEY_AllowVersionChecks = "Allow_Version_Checks";
	private final String KEY_AllowCompendiumUpdates = "Allow_Compendium_Updates";
	private final String KEY_MeteorMinSpawnLevel = "Meteor_Spawn_Min_Level";
	private final String KEY_HazardousGateways = "Hazardous_Gateways";
	private final String KEY_CanDryadsDespawn = "Can_Dryads_Despawn";

	private final String KEY_ArmorXPInfusionFactor = "Armor_XP_Infusion_Factor";

	private final String KEY_SavePowerOnWorldSave = "PND_File_WSave";

	private final String KEY_EnableWitchwoodForest = "Enable_Witchwood_Forests";

	private final String KEY_allowCreativeTargets = "Allow_Creative_Targets";

	/**
	 * Beta Particles
	 **/
	private final String KEY_AuraType = "AuraType";
	private final String KEY_AuraBehaviour = "AuraBehaviour";
	private final String KEY_AuraScale = "AuraScale";
	private final String KEY_AuraColor = "AuraColor";
	private final String KEY_AuraQuanity = "AuraQuantity";
	private final String KEY_AuraDelay = "AuraDelay";
	private final String KEY_AuraSpeed = "AuraSpeed";
	private final String KEY_AuraAlpha = "AuraAlpha";
	private final String KEY_AuraColorRandomize = "AuraColorRandomize";
	private final String KEY_AuraColorDefault = "AuraColorDefault";
	/** End Beta Particles **/

	/**
	 * GUI Config
	 **/
	private final String KEY_ManaHudPositionX = "ManaHudPositionX";
	private final String KEY_BurnoutHudPositionX = "BurnoutHudPositionX";
	private final String KEY_BuffsPositivePositionX = "BuffsPositivePositionX";
	private final String KEY_BuffsNegativePositionX = "BuffsNegativePositionX";
	private final String KEY_LevelPositionX = "LevelPositionX";
	private final String KEY_AffinityPositionX = "AffinityPositionX";
	private final String KEY_ArmorPositionHeadX = "ArmorPositionHeadX";
	private final String KEY_ArmorPositionChestX = "ArmorPositionChestX";
	private final String KEY_ArmorPositionLegsX = "ArmorPositionLegsX";
	private final String KEY_ArmorPositionBootsX = "ArmorPositionBootsX";

	private final String KEY_ManaHudPositionY = "ManaHudPositionY";
	private final String KEY_BurnoutHudPositionY = "BurnoutHudPositionY";
	private final String KEY_BuffsPositivePositionY = "BuffsPositivePositionY";
	private final String KEY_BuffsNegativePositionY = "BuffsNegativePositionY";
	private final String KEY_LevelPositionY = "LevelPositionY";
	private final String KEY_AffinityPositionY = "AffinityPositionY";
	private final String KEY_ArmorPositionHeadY = "ArmorPositionHeadY";
	private final String KEY_ArmorPositionChestY = "ArmorPositionChestY";
	private final String KEY_ArmorPositionLegsY = "ArmorPositionLegsY";
	private final String KEY_ArmorPositionBootsY = "ArmorPositionBootsY";
	private final String KEY_XPBarPositionX = "XPBarPositionX";
	private final String KEY_XPBarPositionY = "XPBarPositionY";
	private final String KEY_ContingencyPositionX = "ContingencyPositionX";
	private final String KEY_ContingencyPositionY = "ContingencyPositionY";
	private final String KEY_ManaNumericPositionX = "ManaNumericX";
	private final String KEY_ManaNumericPositionY = "ManaNumericY";
	private final String KEY_BurnoutNumericPositionX = "BurnoutNumericX";
	private final String KEY_BurnoutNumericPositionY = "BurnoutNumericY";
	private final String KEY_XPNumericPositionX = "XPNumericX";
	private final String KEY_XPNumericPositionY = "XPNumericY";
	private final String KEY_SpellBookPositionX = "SpellBookX";
	private final String KEY_SpellBookPositionY = "SpellBookY";
	private final String KEY_EnderAffinityAbilityCooldown = "EnderAffinityAbilityCD";

	private final String KEY_ManaShieldingPositionX = "ManaShieldingX";
	private final String KEY_ManaShieldingPositionY = "ManaShieldingY";

	private final String KEY_StagedCompendium = "Staged Compendium";

	private final String KEY_ShowHudMinimally = "ShowHudMinimally";
	private final String KEY_ShowArmorUI = "ShowArmorUI";
	private final String KEY_moonstoneMeteorsDestroyTerrain = "MoonstoneMeteorDestroyTerrain";

	private final String KEY_ShowBuffs = "ShowBuffTimers";
	private final String KEY_ShowNumerics = "ShowNumericValues";
	private final String KEY_ShowXPAlways = "ShowXPAlways";
	private final String KEY_ShowHUDBars = "ShowHUDBars";
	private final String KEY_ColourblindMode = "ColourblindMode";
	/**
	 * End GUI Config
	 **/

	private static final String CATEGORY_BETA = "beta";
	private static final String CATEGORY_MOBS = "mobs";
	private static final String CATEGORY_UI = "guis";

	private int GFXLevel;
	private boolean PlayerSpellsDamageTerrain;
	private boolean NPCSpellsDamageTerrain;
	private float DamageMultiplier;
	private boolean UseSpecialRenderers;
	private boolean DisplayManaInInventory;
	private boolean IsImbueEnabled;
	private boolean RetroWorldGen;
	private boolean moonstoneMeteorsDestroyTerrain;
	private boolean suggestSpellNames;
	private boolean forgeSmeltsVillagers;
	private boolean witchwoodLeafParticles;
	private int everstoneRepairRate;

	private int witchwoodForestID;

	private float FrictionCoefficient;

	private int secondarySkillTreeTierCap;
	private int mageVillagerProfessionID;
	private String[] digBlacklist;
	private int[] worldgenBlacklist;
	private boolean enableWitchwoodForest;
	private int witchwoodForestRarity;
	
	private boolean allowCreativeTargets;

	private String[] appropriationBlockBlacklist;
	private Class<? extends Entity>[] appropriationMobBlacklist;

	private int AuraType;
	private int AuraBehaviour;
	private float AuraScale;
	private float AuraAlpha;
	private int AuraColor;
	private int AuraDelay;
	private int AuraQuantity;
	private double AuraSpeed;
	private boolean AuraRandomColor;
	private boolean AuraDefaultColor;
	private double ArmorXPInfusionFactor;
	private double manaCap;
	private int enderAffinityAbilityCooldown;

	private AMVector2 manaHudPosition;
	private AMVector2 burnoutHudPosition;
	private AMVector2 positiveBuffsPosition;
	private AMVector2 negativeBuffsPosition;
	private AMVector2 levelPosition;
	private AMVector2 affinityPosition;
	private AMVector2 armorPositionHead;
	private AMVector2 armorPositionChest;
	private AMVector2 armorPositionLegs;
	private AMVector2 armorPositionBoots;
	private AMVector2 xpBarPosition;
	private AMVector2 contingencyPosition;
	private AMVector2 manaNumericPosition;
	private AMVector2 burnoutNumericPosition;
	private AMVector2 XPNumericPosition;
	private AMVector2 SpellBookPosition;
	private AMVector2 manaShieldingPosition;
	private boolean showBuffs;
	private boolean showNumerics;
	private boolean showHudMinimally;
	private boolean showXPAlways;
	private boolean showArmorUI;
	private boolean stagedCompendium;
	private boolean showHudBars;
	private boolean colourblindMode;
	private boolean candlesAreRovingLights;
	private int meteorMinSpawnLevel;
	private boolean hazardousGateways;
	private boolean disarmAffectsPlayers;
	private boolean digBreaksTileEntities;
	private boolean savePowerOnWorldSave;

	private boolean allowCompendiumUpdates;
	private boolean allowVersionChecks;
	private boolean canDryadsDespawn;

	private boolean oldXpCalculations;

	public static final String DEFAULT_LANGUAGE = "en_US";

	public AMConfig(File file){
		super(file);
		this.load();
		this.addCustomCategoryComment(CATEGORY_BETA, "This applies to those who have beta auras unlocked only");
		this.addCustomCategoryComment(CATEGORY_MOBS, "Spawn control for different AM mobs.");
		this.getCategory(CATEGORY_UI).setShowInGui(false);
	}

	@SuppressWarnings("unchecked")
	public void init(){

		this.PlayerSpellsDamageTerrain = this.get(CATEGORY_GENERAL, this.KEY_PlayerSpellsDamageTerrain, true).getBoolean(true);
		this.NPCSpellsDamageTerrain = this.get(CATEGORY_GENERAL, this.KEY_NPCSpellsDamageTerrain, false).getBoolean(false);

		this.DamageMultiplier = (float) this.get(CATEGORY_GENERAL, this.KEY_DamageMultiplier, 1.0, "How much the damage in Ars Magica is scaled.").getDouble(1.0);

		this.UseSpecialRenderers = this.get(CATEGORY_GENERAL, this.KEY_UseSpecialRenderers, true, "Render spell effects on equipped scrolls rather than the scroll itself (only applies to the in-game one, the one on your hotbar remains unchanged)").getBoolean(true);

		boolean def = !Loader.isModLoaded("NotEnoughItems");
		this.DisplayManaInInventory = this.get(CATEGORY_GENERAL, this.KEY_DisplayManaInInventory, def, "This will toggle mana display on and off in your inventory.  Default 'O' key in game.").getBoolean(def);

		this.FrictionCoefficient = (float) this.get(CATEGORY_GENERAL, this.KEY_FrictionCoefficient, 0.8, "This is the multiplier used to determine velocity lost when a spell projectile bounces. 0.0 is a complete stop, 1.0 is no loss.").getDouble(0.8);

		Property retroWorldGenProp = this.get(CATEGORY_GENERAL, this.KEY_RetroactiveWorldGen, false, "Set this to true to enable retroactive worldgen for Ars Magica structures and ores.  *WARNING* This may break your save!  Do a backup first!  Note: This will automatically turn off after running the game once.");
		this.RetroWorldGen = retroWorldGenProp.getBoolean(false);

		if (this.RetroWorldGen){
			retroWorldGenProp.set(false);
		}

		this.secondarySkillTreeTierCap = this.get(CATEGORY_GENERAL, this.KEY_SecondarySkillTreeTierCap, 99, "Sets how far a player may progress into secondary skill trees.").getInt();
		this.mageVillagerProfessionID = this.get(CATEGORY_GENERAL, this.KEY_MageVillagerProfessionID, 29).getInt();

		this.manaHudPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_ManaHudPositionX, 0.7104166746139526).getDouble(0.7104166746139526), this.get(CATEGORY_UI, this.KEY_ManaHudPositionY, 0.9137254953384399).getDouble(0.9137254953384399));
		this.burnoutHudPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_BurnoutHudPositionX, 0.13333334028720856).getDouble(0.13333334028720856), this.get(CATEGORY_UI, this.KEY_BurnoutHudPositionY, 0.9176470637321472).getDouble(0.9176470637321472));
		this.positiveBuffsPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_BuffsPositivePositionX, 0.5145833492279053).getDouble(0.5145833492279053), this.get(CATEGORY_UI, this.KEY_BuffsPositivePositionY, 0.47843137383461).getDouble(0.47843137383461));
		this.negativeBuffsPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_BuffsNegativePositionX, 0.46666666865348816).getDouble(0.46666666865348816), this.get(CATEGORY_UI, this.KEY_BuffsNegativePositionY, 0.47843137383461).getDouble(0.47843137383461));
		this.levelPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_LevelPositionX, 0.49791666865348816).getDouble(0.49791666865348816), this.get(CATEGORY_UI, this.KEY_LevelPositionY, 0.8117647171020508).getDouble(0.8117647171020508));
		this.affinityPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_AffinityPositionX, 0.9770833253860474).getDouble(0.9770833253860474), this.get(CATEGORY_UI, this.KEY_AffinityPositionY, 0.9).getDouble(0.9));
		this.armorPositionChest = new AMVector2(this.get(CATEGORY_UI, this.KEY_ArmorPositionChestX, 0.004166666883975267).getDouble(0.004166666883975267), this.get(CATEGORY_UI, this.KEY_ArmorPositionChestY, 0.5568627715110779).getDouble(0.5568627715110779));
		this.armorPositionHead = new AMVector2(this.get(CATEGORY_UI, this.KEY_ArmorPositionHeadX, 0.004166666883975267).getDouble(0.004166666883975267), this.get(CATEGORY_UI, this.KEY_ArmorPositionHeadY, 0.5176470875740051).getDouble(0.5176470875740051));
		this.armorPositionLegs = new AMVector2(this.get(CATEGORY_UI, this.KEY_ArmorPositionLegsX, 0.004166666883975267).getDouble(0.004166666883975267), this.get(CATEGORY_UI, this.KEY_ArmorPositionLegsY, 0.5960784554481506).getDouble(0.5960784554481506));
		this.armorPositionBoots = new AMVector2(this.get(CATEGORY_UI, this.KEY_ArmorPositionBootsX, 0.004166666883975267).getDouble(0.004166666883975267), this.get(CATEGORY_UI, this.KEY_ArmorPositionBootsY, 0.6352941393852234).getDouble(0.6352941393852234));
		this.xpBarPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_XPBarPositionX, 0.31041666865348816).getDouble(0.31041666865348816), this.get(CATEGORY_UI, this.KEY_XPBarPositionY, 0.7843137383460999).getDouble(0.7843137383460999));
		this.contingencyPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_ContingencyPositionX, 0.0020833334419876337).getDouble(0.0020833334419876337), this.get(CATEGORY_UI, this.KEY_ContingencyPositionY, 0.9333333373069763).getDouble(0.9333333373069763));

		this.manaNumericPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_ManaNumericPositionX, 0.7437499761581421).getDouble(0.7437499761581421), this.get(CATEGORY_UI, this.KEY_ManaNumericPositionY, 0.8941176533699036).getDouble(0.8941176533699036));
		this.burnoutNumericPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_BurnoutNumericPositionX, 0.21041665971279144).getDouble(0.21041665971279144), this.get(CATEGORY_UI, this.KEY_BurnoutNumericPositionY, 0.9058823585510254).getDouble(0.9058823585510254));
		this.XPNumericPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_XPNumericPositionX, 0.47083333134651184).getDouble(0.47083333134651184), this.get(CATEGORY_UI, this.KEY_XPNumericPositionY, 0.7450980544090271).getDouble(0.7450980544090271));
		this.SpellBookPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_SpellBookPositionX, 0.0).getDouble(0.0), this.get(CATEGORY_UI, this.KEY_SpellBookPositionY, 0.0).getDouble(0.0));
		this.manaShieldingPosition = new AMVector2(this.get(CATEGORY_UI, this.KEY_ManaShieldingPositionX, 0.7104166746139526).getDouble(0.7104166746139526), this.get(CATEGORY_UI, this.KEY_ManaShieldingPositionY, 0.9352226853370667).getDouble(0.9352226853370667));
		this.showHudMinimally = this.get(CATEGORY_UI, this.KEY_ShowHudMinimally, false, "Set this to true to only show the AM HUD when a spell is equipped").getBoolean(false);
		this.showArmorUI = this.get(CATEGORY_UI, this.KEY_ShowArmorUI, true).getBoolean(true);
		this.showBuffs = this.get(CATEGORY_UI, this.KEY_ShowBuffs, true).getBoolean(true);
		this.showNumerics = this.get(CATEGORY_UI, this.KEY_ShowNumerics, false).getBoolean(false);
		this.showXPAlways = this.get(CATEGORY_UI, this.KEY_ShowXPAlways, false).getBoolean(false);
		this.showHudBars = this.get(CATEGORY_UI, this.KEY_ShowHUDBars, true).getBoolean(true);

		this.witchwoodForestID = this.get(CATEGORY_GENERAL, this.KEY_WitchwoodForestBiomeID, -1, "Sets the biome ID that Witchwood Forest will use. Default: -1 (automatic))").getInt();
		this.witchwoodLeafParticles = this.get(CATEGORY_GENERAL, this.KEY_witchwoodLeavesFall, true, "Disable this if you experience low FPS in witchwood forests").getBoolean(true);
		this.enableWitchwoodForest = this.get(CATEGORY_GENERAL, this.KEY_EnableWitchwoodForest, true, "Disable this if you prefer the witchwood forest to not generate").getBoolean(true);
		this.witchwoodForestRarity = this.get(CATEGORY_GENERAL, this.KEY_WitchwoodForestRarity, 6, "Sets how rare witchwood forests are.  Lower is more rare.").getInt();

		this.allowCreativeTargets = this.get(CATEGORY_GENERAL, this.KEY_allowCreativeTargets, true, "Disable this to prevent spell effects on creative players").getBoolean(true);

		this.moonstoneMeteorsDestroyTerrain = this.get(CATEGORY_GENERAL, this.KEY_moonstoneMeteorsDestroyTerrain, true, "Should moonstone meteors destroy terrain when landing?  Keep in mind they will never land on anything other than grass.").getBoolean(true);

		this.suggestSpellNames = this.get(CATEGORY_GENERAL, this.KEY_moonstoneMeteorsDestroyTerrain, true, "Set this to true to allow AM2 to get random spell names from Seventh Sanctum, and suggest them when naming spells.  Naturally, an internet connection is required.  Keep in mind, while I try to keep things family friendly, it's possible that not all names generated are so.").getBoolean(true);

		this.forgeSmeltsVillagers = this.get(CATEGORY_GENERAL, this.KEY_ForgeSmeltsVillagers, true, "Set this to true to have the forge component smelt villagers into emeralds.  This counts as an attack and lowers your reputation.").getBoolean(true);

		this.everstoneRepairRate = this.get(CATEGORY_GENERAL, this.KEY_EverstoneRepairRate, 180).getInt();

		this.stagedCompendium = this.get(CATEGORY_GENERAL, this.KEY_StagedCompendium, true, "Set this to false to have the compendium show everything, and not unlock as you go.").getBoolean(true);

		this.colourblindMode = this.get(CATEGORY_GENERAL, this.KEY_ColourblindMode, false, "Set this to true to have AM2 list out colours for skill points and essence types rather than showing them as a colour.").getBoolean(false);

		this.candlesAreRovingLights = this.get(CATEGORY_GENERAL, this.KEY_CandlesAreRovingLights, true, "Set this to false to disable candles being able to act as roving lights, which improves performance.").getBoolean(true);

		this.allowCompendiumUpdates = this.get(CATEGORY_GENERAL, this.KEY_AllowCompendiumUpdates, true, "If true, AM2 will automatically download compendium updates when available for your mod version.").getBoolean(true);
		this.allowVersionChecks = this.get(CATEGORY_GENERAL, this.KEY_AllowVersionChecks, true, "If true, AM2 will notify you via the compendium when new versions are available.  It will not spam chat on login.  You will not be notified of updates that are not for your current Minecraft version.").getBoolean(true);

		this.meteorMinSpawnLevel = this.get(CATEGORY_GENERAL, this.KEY_MeteorMinSpawnLevel, 10, "You must reach this magic level before Moonstone meteors will fall near you.").getInt();

		this.hazardousGateways = this.get(CATEGORY_GENERAL, this.KEY_HazardousGateways, true, "Set this to false in order to disable gateways sending you partial distances if you don't have enough power.").getBoolean(true);

		this.ArmorXPInfusionFactor = this.get(CATEGORY_GENERAL, this.KEY_ArmorXPInfusionFactor, 1.0, "Alter this to change the rate at which armor XP infuses.").getDouble();
		this.disarmAffectsPlayers = this.get(CATEGORY_GENERAL, this.KEY_DisarmAffectsPlayers, true, "If false, disarm won't work on players.").getBoolean(true);
		this.manaCap = this.get(CATEGORY_GENERAL, this.KEY_ManaCap, 0, "Sets the maximum mana a player can have (0 for no cap)").getDouble(0);

		this.digBreaksTileEntities = this.get(CATEGORY_GENERAL, this.KEY_DigBreaksTEs, true, "Can the dig component break blocks that have a tile entity?").getBoolean(true);

		this.savePowerOnWorldSave = this.get(CATEGORY_GENERAL, this.KEY_SavePowerOnWorldSave, true, "Set this to false if you are experiencing tick lage due to AM2 saving power data alongside the world save.  This will instead cache the power data in memory to be saved later.  This comes with more risk in the event of a crash, and a larger memory footprint, but increased performance. Can be used alongside chunk unload save config. Power data is still always saved at world unload (server shutdown).").getBoolean(true);

		this.canDryadsDespawn = this.get(CATEGORY_MOBS, this.KEY_CanDryadsDespawn, true, "Set this to false if you don't want dryads to despawn.").getBoolean(true);

		this.enderAffinityAbilityCooldown = this.get(CATEGORY_GENERAL, this.KEY_EnderAffinityAbilityCooldown, 100, "Set this to the number of ticks between ender affinity teleports.").getInt();

		this.oldXpCalculations = this.get(CATEGORY_GENERAL, this.KEY_OldCalculations, true, "Enable Old XP calculations (before 1.5.0C-8)").setRequiresMcRestart(true).getBoolean(true);

		String digBlacklistString = this.get(CATEGORY_GENERAL, this.KEY_DigDisabledBlocks, "", "Comma-separated list of block IDs that dig cannot break.  If a block is flagged as unbreackable in code, Dig will already be unable to break it.  There is no need to set it here (eg, bedrock, etc.).  Dig also makes use of Forge block harvest checks.  This is mainly for fine-tuning.").getString();
		this.digBlacklist = digBlacklistString.split(",");

		String worldgenBlackList = this.get(CATEGORY_GENERAL, this.KEY_WorldgenBlacklist, "-27,-28,-29", "Comma-separated list of dimension IDs that AM should *not* do worldgen in.").getString();
		String[] split = worldgenBlackList.split(",");
		this.worldgenBlacklist = new int[split.length];
		int count = 0;
		for (String s : split){
			if (s.equals("")) continue;
			try{
				this.worldgenBlacklist[count] = Integer.parseInt(s.trim());
			}catch (Throwable t){
				LogHelper.info("Malformed item in worldgen blacklist (%s).  Skipping.", s);
				t.printStackTrace();
				this.worldgenBlacklist[count] = -1;
			}finally{
				count++;
			}
		}

		String apBlockBL = this.get(CATEGORY_GENERAL, this.KEY_Appropriation_Block_Blacklist, "", "Comma-separated list of block IDs that appropriation cannot pick up.").getString();
		this.appropriationBlockBlacklist = apBlockBL.split(",");

		String apEntBL = this.get(CATEGORY_GENERAL, this.KEY_Appropriation_Mob_Blacklist, "", "Comma-separated list of *fully qualified* Entity class names that appropriation cannot pick up - example, am2.entities.EntityDryad.  They are case sensitive.").getString();
		split = apEntBL.split(",");
		this.appropriationMobBlacklist = new Class[split.length];
		count = 0;
		for (String s : split){
			if (s.equals("")) continue;
			try{
				this.appropriationMobBlacklist[count] = (Class<? extends Entity>) Class.forName(s);
			}catch (Throwable t){
				LogHelper.info("Malformed item in appropriation entity blacklist (%s).  Skipping.", s);
				t.printStackTrace();
				this.appropriationMobBlacklist[count] = null;
			}finally{
				count++;
			}
		}

		this.initDirectProperties();

		this.save();
	}

	@SideOnly(Side.CLIENT)
	public void clientInit(){
		this.AuraType = this.get(CATEGORY_BETA, this.KEY_AuraType, 15).getInt(15);
		//AuraType %= AMParticle.particleTypes.length;
		this.AuraBehaviour = this.get(CATEGORY_BETA, this.KEY_AuraBehaviour, 0).getInt(0);
		this.AuraBehaviour %= ParticleController.AuraControllerOptions.length;
		this.AuraAlpha = (float)(this.get(CATEGORY_BETA, this.KEY_AuraAlpha, 1.0D)).getDouble(1.0D);
		this.AuraScale = (float)(this.get(CATEGORY_BETA, this.KEY_AuraScale, 1.0D).getDouble(1.0));
		this.AuraColor = this.get(CATEGORY_BETA, this.KEY_AuraColor, 0xFFFFFF).getInt(0xFFFFFF);
		this.AuraQuantity = this.get(CATEGORY_BETA, this.KEY_AuraQuanity, 1).getInt(1);
		this.AuraDelay = this.get(CATEGORY_BETA, this.KEY_AuraDelay, 5).getInt(5);
		this.AuraSpeed = this.get(CATEGORY_BETA, this.KEY_AuraSpeed, 0.02D).getDouble(0.02D);
		this.AuraRandomColor = this.get(CATEGORY_BETA, this.KEY_AuraColorRandomize, true).getBoolean(true);
		this.AuraDefaultColor = this.get(CATEGORY_BETA, this.KEY_AuraColorDefault, true).getBoolean(true);

		this.GFXLevel = 2 - Minecraft.getMinecraft().gameSettings.particleSetting;

		this.save();
	}

	//====================================================================================
	// Getters - Cached
	//====================================================================================

	public boolean FullGFX(){
		return this.GFXLevel == 2;
	}

	public boolean LowGFX(){
		return this.GFXLevel == 1;
	}

	public boolean NoGFX(){
		return this.GFXLevel == 0;
	}

	public boolean NPCSpellsDamageTerrain(){
		return this.NPCSpellsDamageTerrain;
	}

	public boolean PlayerSpellsDamageTerrain(){
		return this.PlayerSpellsDamageTerrain;
	}

	public int getGFXLevel(){
		return this.GFXLevel;
	}

	public float getDamageMultiplier(){
		return this.DamageMultiplier;
	}

	public boolean getIsImbueEnchantEnabled(){
		return this.IsImbueEnabled;
	}

	public int getImbueProcCost(int enchantID){
		return 0;
	}

	public boolean useSpecialRenderers(){
		return this.UseSpecialRenderers;
	}

	public boolean displayManaInInventory(){
		return this.DisplayManaInInventory;
	}

	public double getFrictionCoefficient(){
		return this.FrictionCoefficient;
	}

	public boolean retroactiveWorldgen(){
		return this.RetroWorldGen;
	}

	public int getSkillTreeSecondaryTierCap(){
		return this.secondarySkillTreeTierCap;
	}

	public int getVillagerProfessionID(){
		return this.mageVillagerProfessionID;
	}

	public AMVector2 getManaHudPosition(){
		return this.manaHudPosition;
	}

	public AMVector2 getBurnoutHudPosition(){
		return this.burnoutHudPosition;
	}

	public AMVector2 getPositiveBuffsPosition(){
		return this.positiveBuffsPosition;
	}

	public AMVector2 getNegativeBuffsPosition(){
		return this.negativeBuffsPosition;
	}

	public AMVector2 getLevelPosition(){
		return this.levelPosition;
	}

	public AMVector2 getAffinityPosition(){
		return this.affinityPosition;
	}

	public AMVector2 getArmorPositionHead(){
		return this.armorPositionHead;
	}

	public AMVector2 getArmorPositionChest(){
		return this.armorPositionChest;
	}

	public AMVector2 getArmorPositionLegs(){
		return this.armorPositionLegs;
	}

	public AMVector2 getArmorPositionBoots(){
		return this.armorPositionBoots;
	}

	public AMVector2 getXPBarPosition(){
		return this.xpBarPosition;
	}

	public AMVector2 getContingencyPosition(){
		return this.contingencyPosition;
	}

	public AMVector2 getManaNumericPosition(){
		return this.manaNumericPosition;
	}

	public AMVector2 getBurnoutNumericPosition(){
		return this.burnoutNumericPosition;
	}

	public AMVector2 getXPNumericPosition(){
		return this.XPNumericPosition;
	}

	public AMVector2 getSpellBookPosition(){
		return this.SpellBookPosition;
	}

	public boolean getShowBuffs(){
		return this.showBuffs;
	}

	public boolean getShowNumerics(){
		return this.showNumerics;
	}

	public String[] getDigBlacklist(){
		return this.digBlacklist;
	}

	public int[] getWorldgenBlacklist(){
		return this.worldgenBlacklist;
	}

	public boolean moonstoneMeteorsDestroyTerrain(){
		return this.moonstoneMeteorsDestroyTerrain;
	}

	public boolean suggestSpellNames(){
		return this.suggestSpellNames;
	}

	public int getWitchwoodForestID(){
		return this.witchwoodForestID;
	}

	public int getEverstoneRepairRate(){
		return this.everstoneRepairRate;
	}

	public boolean showHudMinimally(){
		return this.showHudMinimally;
	}

	public boolean stagedCompendium(){
		return this.stagedCompendium;
	}

	public boolean showXPAlways(){
		return this.showXPAlways;
	}

	public boolean showHudBars(){
		return this.showHudBars;
	}

	public boolean witchwoodLeafPFX(){
		return this.witchwoodLeafParticles;
	}

	public boolean colourblindMode(){
		return this.colourblindMode;
	}

	public String[] getAppropriationBlockBlacklist(){
		return this.appropriationBlockBlacklist;
	}

	public Class<? extends Entity>[] getAppropriationMobBlacklist(){
		return this.appropriationMobBlacklist;
	}

	public boolean allowVersionChecks(){
		return this.allowVersionChecks;
	}

	public boolean allowCompendiumUpdates(){
		return this.allowCompendiumUpdates;
	}

	public boolean getHazardousGateways(){
		return this.hazardousGateways;
	}

	public double getArmorXPInfusionFactor(){
		return this.ArmorXPInfusionFactor;
	}

	public boolean getDisarmAffectsPlayers(){
		return this.disarmAffectsPlayers;
	}

	public double getManaCap(){
		return this.manaCap;
	}

	public boolean getDigBreaksTileEntities(){
		return this.digBreaksTileEntities;
	}

	public boolean savePowerDataOnWorldSave(){
		return this.savePowerOnWorldSave;
	}


	public boolean canDraydsDespawn(){
		return this.canDryadsDespawn;
	}

	public int getMeteorMinSpawnLevel(){
		return this.meteorMinSpawnLevel;
	}

	public boolean forgeSmeltsVillagers(){
		return this.forgeSmeltsVillagers;
	}

	public boolean showArmorUI(){
		return this.showArmorUI;
	}

	public boolean candlesAreRovingLights(){
		return this.candlesAreRovingLights;
	}

	public int getEnderAffinityAbilityCooldown(){
		return this.enderAffinityAbilityCooldown;
	}

	public boolean getEnableWitchwoodForest(){
		return this.enableWitchwoodForest;
	}

	public int getWitchwoodForestRarity(){
		return this.witchwoodForestRarity;
	}

	public boolean getAllowCreativeTargets(){
		return this.allowCreativeTargets;
	}

	//====================================================================================
	// Getters - Aura
	//====================================================================================

	public int getAuraIndex(){
		return this.AuraType;
	}

	public int getAuraBehaviour(){
		return this.AuraBehaviour;
	}

	public boolean getAuraColorRandom(){
		return this.AuraRandomColor;
	}

	public boolean getAuraColorDefault(){
		return this.AuraDefaultColor;
	}

	public float getAuraScale(){
		return this.AuraScale;
	}

	public int getAuraColor(){
		return this.AuraColor;
	}

	public int getAuraDelay(){
		return this.AuraDelay;
	}

	public int getAuraQuantity(){
		return this.AuraQuantity;
	}

	public float getAuraSpeed(){
		return (float) this.AuraSpeed;
	}

	public float getAuraAlpha(){
		return this.AuraAlpha;
	}

	//====================================================================================
	// Getters - Direct
	//====================================================================================
	//ping the direct properties once so that they show up in config
	public void initDirectProperties(){
		this.get(CATEGORY_MOBS, this.KEY_hecateSpawnRate, 2).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_mageSpawnRate, 1).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_waterElementalSpawnRate, 3).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_manaElementalSpawnRate, 2).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_dryadSpawnRate, 5).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_manaCreeperSpawnRate, 3).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_darklingSpawnRate, 5).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_earthElementalSpawnRate, 2).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_fireElementalSpawnRate, 2).setRequiresMcRestart(true);
		this.get(CATEGORY_MOBS, this.KEY_flickerSpawnRate, 2).setRequiresMcRestart(true);
	}

	public int GetHecateSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_hecateSpawnRate, 2).setRequiresMcRestart(true);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetMageSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_mageSpawnRate, 1).setRequiresMcRestart(true);
		return Math.max(prop.getInt(1), 0);
	}

	public int GetWaterElementalSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_waterElementalSpawnRate, 3).setRequiresMcRestart(true);
		return Math.max(prop.getInt(3), 0);
	}

	public int GetManaElementalSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_manaElementalSpawnRate, 2).setRequiresMcRestart(true);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetDryadSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_dryadSpawnRate, 5).setRequiresMcRestart(true);
		return Math.max(prop.getInt(5), 0);
	}

	public int GetManaCreeperSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_manaCreeperSpawnRate, 3).setRequiresMcRestart(true);
		return Math.max(prop.getInt(3), 0);
	}

	public int GetDarklingSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_darklingSpawnRate, 5).setRequiresMcRestart(true);
		return Math.max(prop.getInt(5), 0);
	}

	public int GetEarthElementalSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_earthElementalSpawnRate, 2).setRequiresMcRestart(true);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetFireElementalSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_fireElementalSpawnRate, 2).setRequiresMcRestart(true);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetFlickerSpawnRate(){
		Property prop = this.get(CATEGORY_MOBS, this.KEY_flickerSpawnRate, 4).setRequiresMcRestart(true);
		return Math.max(prop.getInt(4), 0);
	}

	//====================================================================================
	// Setters
	//====================================================================================

	@SideOnly(Side.CLIENT)
	public void setAuraIndex(int index){
		if (index < 0) index = 0;
		if (index >= AMParticle.particleTypes.length) index = AMParticle.particleTypes.length - 1;

		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraType, 15);
		prop.set(index);

		this.AuraType = index;
	}

	public void setAuraBehaviour(int index){
		if (index < 0) index = 0;
		if (index >= ParticleController.AuraControllerOptions.length)
			index = ParticleController.AuraControllerOptions.length - 1;

		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraBehaviour, 0);
		prop.set(index);

		this.AuraBehaviour = index;
	}

	public void setAuraColorRandom(boolean value){
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraColorRandomize, false);
		prop.set(value);

		this.AuraRandomColor = value;
	}

	public void setAuraColorDefault(boolean value){
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraColorDefault, true);
		prop.set(value);

		this.AuraDefaultColor = value;
	}

	public void setAuraScale(float scale){
		if (scale < 1) scale = 1;
		if (scale > 200) scale = 200;
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraScale, 50D);
		prop.set(scale);

		this.AuraScale = scale;
	}

	public void setAuraColor(int color){
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraColor, 0xFFFFFF);
		prop.set(color);

		this.AuraColor = color;
	}

	public void setAuraAlpha(float alpha){
		if (alpha < 0) alpha = 0;
		if (alpha > 100) alpha = 100;
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraAlpha, 255D);
		prop.set(alpha);

		this.AuraAlpha = alpha;
	}

	public void setAuraQuantity(int quantity){
		if (quantity < 1) quantity = 1;
		else if (quantity > 5) quantity = 5;
		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraAlpha, 2);
		prop.set(quantity);

		this.AuraQuantity = quantity;
	}

	public void setAuraDelay(int delay){
		if (delay < 1) delay = 1;
		else if (delay > 200) delay = 200;

		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraDelay, 5);
		prop.set(delay);

		this.AuraDelay = delay;
	}

	public void setAuraSpeed(float speed){
		if (speed < 0.01f) speed = 0.01f;
		else if (speed > 10f) speed = 10f;

		Property prop = this.get(CATEGORY_BETA, this.KEY_AuraSpeed, 0.02f);
		prop.set(speed);

		this.AuraSpeed = speed;
	}

	public void setDisplayManaInInventory(boolean value){
		boolean def = !Loader.isModLoaded("NotEnoughItems");
		Property prop = this.get(CATEGORY_GENERAL, this.KEY_DisplayManaInInventory, def, "This will toggle mana display on and off in your inventory.  Default 'O' key in game.");
		prop.set(value);

		this.DisplayManaInInventory = value;
	}

	public void disableRetroactiveWorldgen(){
		Property prop = this.get(CATEGORY_GENERAL, this.KEY_RetroactiveWorldGen, false, "Set this to true to enable retroactive worldgen for Ars Magica structures and ores.  *WARNING* This may break your save!  Do a backup first!");
		prop.set(false);

		this.RetroWorldGen = false;
	}

	public void setGuiPositions(AMVector2 manaHud, AMVector2 burnoutHud, AMVector2 levelHud, AMVector2 affinityHud, AMVector2 posBuffsHud, AMVector2 negBuffsHud, AMVector2 armorHead, AMVector2 armorChest, AMVector2 armorLegs, AMVector2 armorBoots, AMVector2 xpBar, AMVector2 contingency, AMVector2 manaNumeric, AMVector2 burnoutNumeric, AMVector2 XPNumeric, AMVector2 spellBookPos, AMVector2 manaShieldingPos, boolean showBuffs, boolean showNumerics, boolean minimalHud, boolean showArmorUI, boolean showXPAlways, boolean showHudBars){
		this.manaHudPosition = manaHud;
		this.burnoutHudPosition = burnoutHud;
		this.levelPosition = levelHud;
		this.affinityPosition = affinityHud;
		this.positiveBuffsPosition = posBuffsHud;
		this.negativeBuffsPosition = negBuffsHud;
		this.armorPositionHead = armorHead;
		this.armorPositionChest = armorChest;
		this.armorPositionLegs = armorLegs;
		this.armorPositionBoots = armorBoots;
		this.xpBarPosition = xpBar;
		this.contingencyPosition = contingency;
		this.manaNumericPosition = manaNumeric;
		this.burnoutNumericPosition = burnoutNumeric;
		this.XPNumericPosition = XPNumeric;
		this.SpellBookPosition = spellBookPos;
		this.manaShieldingPosition = manaShieldingPos;
		this.showBuffs = showBuffs;
		this.showNumerics = showNumerics;
		this.showHudMinimally = minimalHud;
		this.showArmorUI = showArmorUI;
		this.showXPAlways = showXPAlways;
		this.showHudBars = showHudBars;
	}

	public void saveGuiPositions(){
		this.updateAMVector2(this.KEY_ManaHudPositionX, this.KEY_ManaHudPositionY, this.manaHudPosition);
		this.updateAMVector2(this.KEY_BurnoutHudPositionX, this.KEY_BurnoutHudPositionY, this.burnoutHudPosition);
		this.updateAMVector2(this.KEY_LevelPositionX, this.KEY_LevelPositionY, this.levelPosition);
		this.updateAMVector2(this.KEY_AffinityPositionX, this.KEY_AffinityPositionY, this.affinityPosition);
		this.updateAMVector2(this.KEY_BuffsPositivePositionX, this.KEY_BuffsPositivePositionY, this.positiveBuffsPosition);
		this.updateAMVector2(this.KEY_BuffsNegativePositionX, this.KEY_BuffsNegativePositionY, this.negativeBuffsPosition);
		this.updateAMVector2(this.KEY_ArmorPositionHeadX, this.KEY_ArmorPositionHeadY, this.armorPositionHead);
		this.updateAMVector2(this.KEY_ArmorPositionChestX, this.KEY_ArmorPositionChestY, this.armorPositionChest);
		this.updateAMVector2(this.KEY_ArmorPositionLegsX, this.KEY_ArmorPositionLegsY, this.armorPositionLegs);
		this.updateAMVector2(this.KEY_ArmorPositionBootsX, this.KEY_ArmorPositionBootsY, this.armorPositionBoots);
		this.updateAMVector2(this.KEY_XPBarPositionX, this.KEY_XPBarPositionY, this.xpBarPosition);
		this.updateAMVector2(this.KEY_ContingencyPositionX, this.KEY_ContingencyPositionY, this.contingencyPosition);
		this.updateAMVector2(this.KEY_ManaNumericPositionX, this.KEY_ManaNumericPositionY, this.manaNumericPosition);
		this.updateAMVector2(this.KEY_BurnoutNumericPositionX, this.KEY_BurnoutNumericPositionY, this.burnoutNumericPosition);
		this.updateAMVector2(this.KEY_XPNumericPositionX, this.KEY_XPNumericPositionY, this.XPNumericPosition);
		this.updateAMVector2(this.KEY_SpellBookPositionX, this.KEY_SpellBookPositionY, this.SpellBookPosition);
		this.updateAMVector2(this.KEY_ManaShieldingPositionX, this.KEY_ManaShieldingPositionY, this.manaShieldingPosition);

		Property buffProp;
		buffProp = this.get(CATEGORY_UI, this.KEY_ShowBuffs, true);
		buffProp.set(this.showBuffs);

		Property numProp;
		numProp = this.get(CATEGORY_UI, this.KEY_ShowNumerics, false);
		numProp.set(this.showNumerics);

		Property armorProp;
		armorProp = this.get(CATEGORY_UI, this.KEY_ShowArmorUI, true);
		armorProp.set(this.showArmorUI);

		Property minimalProp;
		minimalProp = this.get(CATEGORY_UI, this.KEY_ShowHudMinimally, false);
		minimalProp.set(this.showHudMinimally);

		Property xpShow;
		xpShow = this.get(CATEGORY_UI, this.KEY_ShowXPAlways, false);
		xpShow.set(this.showXPAlways);

		Property barShow;
		barShow = this.get(CATEGORY_UI, this.KEY_ShowHUDBars, true);
		barShow.set(this.showHudBars);

		this.save();
	}

	public void setSkillTreeSecondaryTierCap(int skillTreeLock){
		this.secondarySkillTreeTierCap = skillTreeLock;
	}

	private void updateAMVector2(String keyX, String keyY, AMVector2 value){
		Property prop;
		prop = this.get(CATEGORY_UI, keyX, 0);
		prop.set(value.x);

		prop = this.get(CATEGORY_UI, keyY, 0);
		prop.set(value.y);
	}

	public void setManaCap(double cap){
		this.manaCap = cap;
	}

	public AMVector2 getManaShieldingPosition() {
		return this.manaShieldingPosition;
	}

	public boolean getOldXpCalculations() {
		return this.oldXpCalculations;
	}

	public void setOldXpCalculations(boolean b) {
		this.oldXpCalculations = oldXpCalculations;
	}
}
