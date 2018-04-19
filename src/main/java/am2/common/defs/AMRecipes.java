package am2.common.defs;

import am2.ArsMagica2;
import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.flickers.AbstractFlickerFunctionality;
import am2.common.LogHelper;
import am2.common.ObeliskFuelHelper;
import am2.common.blocks.BlockArsMagicaBlock.EnumBlockType;
import am2.common.blocks.BlockArsMagicaOre.EnumOreType;
import am2.common.blocks.BlockCrystalMarker;
import am2.common.items.ItemBindingCatalyst;
import am2.common.items.ItemCore;
import am2.common.items.ItemKeystoneDoor;
import am2.common.items.ItemOre;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class AMRecipes {
    public static void addShapedRecipes() {
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magicWallRecipe"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.magicWall, 16), 
        		"VSV",
                'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
                'S', "stone");
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.craftingAltarRecipe"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.craftingAltar, 1), 
        		"V", 
        		"S",
                'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
                'S', "stone");

        ObeliskFuelHelper.instance.registerFuelType(s -> {
            if (s.getItem() == ItemDefs.itemOre && s.getItemDamage() == ItemOre.META_VINTEUM)
                return 200;
            return 0;
        });
        ObeliskFuelHelper.instance.registerFuelType(s -> {
            UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, BlockDefs.liquid_essence);
            FluidStack stack = FluidUtil.getFluidContained(s);
            if (stack == null || stack.getFluid() != BlockDefs.liquid_essence)
                return 0;
            return stack.amount * 2;
        });

        for (AbstractFlickerFunctionality func : ArsMagicaAPI.getFlickerFocusRegistry().getValuesCollection()) {
            if (func != null) {
                Object[] recipeItems = func.getRecipe();
                if (recipeItems != null) {
                    GameRegistry.addShapedRecipe ( new ResourceLocation ( "flickerFocusRecipe" ) ,
                            new ResourceLocation ( "shapedRecipies" ) ,
                            new ItemStack ( ItemDefs.flickerFocus , 1 , func.getMask ( )[ 0 ].getID ( ) ) ,
                            recipeItems );
                } else {
                    LogHelper.info("Flicker operator %s was registered with no recipe.  It is un-craftable.  This may have been intentional.", func.getClass().getSimpleName());
                }
            }
        }


        //essence refiner
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.essenceRefiner"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.essenceRefiner, 1),
                "PDP", 
                "OAO", 
                "PPP",
                'P', "plankWood",
                'O', Blocks.OBSIDIAN,
                'A' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) ,
                'D', "gemDiamond");

        //essence conduit
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.essenceConduit" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.essenceConduit , 1 ),
                " C " , 
                " S " , 
                "SSS" ,
                'S' , "stone" ,
                'C' , "gemChimerite" );

        //summoner
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.summoner"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.summoner, 1), 
        		"GVG", 
        		"GOG", 
        		"OOO",
                'G', "ingotGold",
                'O', Blocks.OBSIDIAN,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        //Calefactor
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.calefactor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.calefactor, 1), 
        		"L L", 
        		"SRS", 
        		"SVS",
                'L', new ItemStack(Items.DYE, 1, 4), //lapis
                'S', "stone",
                'R', "dustRedstone",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        //keystone recepticle
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.keystoneRecepticle"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.keystoneRecepticle, 1), 
        		"SVS", 
        		"EPE", 
        		"SVS",
                'P', new ItemStack(ItemDefs.essence, 1, 9),
                'S', Blocks.STONEBRICK,
                'E', Items.ENDER_EYE,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        //astral barrier
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.astralBarrier"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.astralBarrier, 1), 
        		"WVW", 
        		"E E", 
        		"WVW",
                //Character.valueOf('P'), new ItemStack(ItemDefs.essence, 1, ItemDefs.essence.META_ENDER),
                'W', Blocks.COBBLESTONE_WALL,
                'E', Items.ENDER_EYE,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.seerStone"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.seerStone, 1), 
        		" E ", 
        		"SRS",
                'S', "stone", //stone wall
                'E', Items.ENDER_EYE,
                'R', "dustRedstone");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.keystoneChest"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.keystoneChest), 
        		"WRW", 
        		"WVW", 
        		"WRW",
                'W', "plankWood",
                'R', new ItemStack(ItemDefs.rune, 1, 0),
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.lectern"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.lectern), 
        		"SSS", 
        		" P ",
                'S', "slabWood",
                'P', "plankWood");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.occulus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.occulus), 
        		"SPS", 
        		" S ", 
        		"CVC",
                'S', Blocks.STONEBRICK,
                'C', Items.COAL,
                'P', "blockGlassColorless",
                'V', "gemBlueTopaz");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.manaDrain"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.manaDrain), 
        		"WPW", 
        		"VAV", 
        		"WBW",
                'B', "gemBlueTopaz",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'A', new ItemStack(ItemDefs.essence, 1, Affinity.ARCANE.getID()),
                'W', BlockDefs.magicWall,
                'P', new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE));

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.arcaneReconstructor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.arcaneReconstructor), 
        		"SWS", 
        		"VDV", 
        		"SOS",
                'S', "stone",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'D', "gemDiamond",
                'W', BlockDefs.magicWall,
                'O', Blocks.OBSIDIAN);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.arcaneDeconstructor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.arcaneDeconstructor), 
        		"IGR",
                "WDW",
                "WWW",
                'I', ItemDefs.itemFocus,
                'G', "blockGlassColorless",
                'R', new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE),
                'W', BlockDefs.witchwoodPlanks,
                'D', ItemDefs.deficitCrystal);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.flickerLure"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.flickerLure), 
        		"CIV",
                "SSS",
                'C', "gemChimerite",
                'I', "ingotIron",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', Blocks.STONEBRICK);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.manaBattery"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.manaBattery), 
        		"IVI",
                "VAV",
                "IVI",
                'I', "gemChimerite",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'A' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magicWall"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.magicWall, 16, 0), 
        		"VSV",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "stone");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.craftingAltar"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.craftingAltar), 
        		"V",
                "S",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "stone");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.vinteumTorch"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.vinteumTorch, 4), 
        		"V",
                "S",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "stickWood");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.inscriptionTable"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.inscriptionTable),
                "TPF",
                "SSS",
                "W W",
                'T', Blocks.TORCH,
                'P', ItemDefs.spellParchment,
                'F', Items.FEATHER,
                'S', "slabWood",
                'W', "plankWood");

        //GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONEBRICK, 1, 3), new Object[]{
        //		"SS",
        //		"SS",
        //		Character.valueOf('S'), Blocks.STONEBRICK
        //});

        //Inlays

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.redstoneInlay"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.redstoneInlay, 4, 0), 
        		"RRR",
                "RVR",
                "RRR",
                'R', "dustRedstone",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.ironInlay"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.ironInlay, 4, 0), 
        		"III",
                "IVI",
                "III",
                'I', "ingotIron",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.goldInlay"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.goldInlay, 4, 0), 
        		"GGG",
                "GVG",
                "GGG",
                'G', "ingotGold",
                'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM));

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.particleEmitter"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.particleEmitter), 
        		" C ",
                "CIC",
                " C ",
                'I', BlockDefs.illusionBlock,
                'C', "gemChimerite");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.witchwoodPlanks"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.witchwoodPlanks, 4), 
        		"W",
                'W', BlockDefs.witchwoodLog);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.witchwoodSingleSlab"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.witchwoodSingleSlab, 6), 
        		"WWW",
                'W', BlockDefs.witchwoodPlanks);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.witchwoodStairs"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.witchwoodStairs, 4), 
        		"  W",
                " WW",
                "WWW",
                'W', BlockDefs.witchwoodPlanks);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.everstone"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.everstone), 
        		" B ",
                "CSC",
                " B ",
                'C', "gemChimerite",
                'S', "stone",
                'B', "gemBlueTopaz");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magiciansWorkbench"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.magiciansWorkbench), 
        		"COC",
                "SWS",
                "LHL",
                'C', "craftingTableWood",
                'O', new ItemStack(Blocks.CARPET),
                'W', "logWood",
                'S', "slabWood",
                'L', "plankWood",
                'H', "chestWood");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.slipstreamGenerator"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.slipstreamGenerator), 
        		"WWW",
                "FAF",
                "WWW",
                'W', BlockDefs.witchwoodLog,
                'F', Items.FEATHER,
                'A', new ItemStack(ItemDefs.essence, 1, Affinity.AIR.getID()));

        //Flicker Habitat
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.elementalAttuner"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.elementalAttuner), 
        		"IDI",
                "DBD",
                "IDI",
                'I', "ingotIron",
                'D' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'B', new ItemStack(BlockDefs.blocks, 1, EnumBlockType.CHIMERITE.ordinal()));

        //Import Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_IN), 
        		" G ",
                "GDG",
                " G ",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.YELLOW.getMetadata ( ) ) //Yellow Dye
        );


        //Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_OUT), 
        		" G ",
                "GDG",
                " G ",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLUE.getMetadata ( ) ) //Lapis
        );

        //Final Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_FINAL_DEST), 
        		" G ",
                "GDG",
                " G ",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.GRAY.getMetadata ( ) ) //Lapis
        );

        //Like Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_LIKE_EXPORT), 
        		"GDG",
                "DED",
                "GDG",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.GREEN.getMetadata ( ) ) , //Cactus Green
                'E', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_OUT));

        //Regulate Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_REGULATE_EXPORT), 
        		"GDG",
                "DED",
                "GDG",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.PURPLE.getMetadata ( ) ) , //Purple Dye
                'E', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_OUT));

        //Set Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_SET_EXPORT), 
        		"GDG",
                "DED",
                "GDG",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIGHT_BLUE.getMetadata ( ) ) , //Light Blue Dye
                'E', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_OUT));

        //Regulate Bidirectional
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_REGULATE_MULTI), 
        		"DSD",
                "GEG",
                "DSD",
                'S', "gemSunstone",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.ORANGE.getMetadata ( ) ) ,
                'E', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_REGULATE_EXPORT));

        //Set Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_SET_IMPORT), 
        		"DSD",
                "GEG",
                "DSD",
                'S', "gemSunstone",
                'G', "gemBlueTopaz",
                'D' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.RED.getMetadata ( ) ) ,
                'E', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_SET_EXPORT));

        //Spell Export Gem
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalMarker"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_SPELL_EXPORT), 
        		"C C",
                "RPI",
                "C C",
                'P', new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE),
                'I', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_SET_IMPORT),
                'C' , new ItemStack ( Items.DYE , 1 , EnumDyeColor.CYAN.getMetadata ( ) ) ,
                'R', new ItemStack(BlockDefs.crystalMarker, 1, BlockCrystalMarker.META_REGULATE_MULTI));

        //Gem Conversions
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_LIKE_EXPORT ) , new ItemStack ( Items.DYE , 1 , EnumDyeColor.GREEN.getMetadata ( ) ) );
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) , new ItemStack ( Items.DYE , 1 , EnumDyeColor.PURPLE.getMetadata ( ) ) );
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) , new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIGHT_BLUE.getMetadata ( ) ) );

        //Obelisk
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.obelisk"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.obelisk), 
        		"VSV",
                "SCS",
                "VSV",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "stone",
                'C', new ItemStack(Blocks.STONEBRICK, 1, 3));

        //Armor Infuser
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.armorImbuer"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.armorImbuer), 
        		"ACA",
                "OPO",
                "OOO",
                'A', BlockDefs.craftingAltar,
                'C', new ItemStack(Blocks.CARPET, 1, Short.MAX_VALUE),
                'O', Blocks.OBSIDIAN,
                'P', Blocks.ENCHANTING_TABLE);

        //storage blocks
        createStorageBlockRecipe(new ResourceLocation(ArsMagica2.MODID, "storage.moonstone"), new ResourceLocation(ArsMagica2.MODID, "storage"), new ItemStack(BlockDefs.blocks, 1, EnumBlockType.MOONSTONE.ordinal()), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE));
        createStorageBlockRecipe(new ResourceLocation(ArsMagica2.MODID, "storage.vinteum"), new ResourceLocation(ArsMagica2.MODID, "storage"), new ItemStack(BlockDefs.blocks, 1, EnumBlockType.VINTEUM.ordinal()), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM));
        createStorageBlockRecipe(new ResourceLocation(ArsMagica2.MODID, "storage.bluetopaz"), new ResourceLocation(ArsMagica2.MODID, "storage"), new ItemStack(BlockDefs.blocks, 1, EnumBlockType.BLUETOPAZ.ordinal()), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ));
        createStorageBlockRecipe(new ResourceLocation(ArsMagica2.MODID, "storage.sunstone"), new ResourceLocation(ArsMagica2.MODID, "storage"), new ItemStack(BlockDefs.blocks, 1, EnumBlockType.SUNSTONE.ordinal()), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE));
        createStorageBlockRecipe(new ResourceLocation(ArsMagica2.MODID, "storage.chimerite"), new ResourceLocation(ArsMagica2.MODID, "storage"), new ItemStack(BlockDefs.blocks, 1, EnumBlockType.CHIMERITE.ordinal()), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE));

        //furnace recipes
        GameRegistry.addSmelting(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANECOMPOUND), new ItemStack(ItemDefs.itemOre, 2, ItemOre.META_ARCANEASH), 0);
        GameRegistry.addSmelting(new ItemStack(BlockDefs.witchwoodLog, 1), new ItemStack(Items.COAL, 1, 1), 0.15f);

        addMetaSmeltingRecipe(BlockDefs.ores, EnumOreType.VINTEUM.ordinal(), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM));
        addMetaSmeltingRecipe(BlockDefs.ores, EnumOreType.SUNSTONE.ordinal(), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE));
        addMetaSmeltingRecipe(BlockDefs.ores, EnumOreType.BLUETOPAZ.ordinal(), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ));
        addMetaSmeltingRecipe(BlockDefs.ores, EnumOreType.CHIMERITE.ordinal(), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE));
        addMetaSmeltingRecipe(BlockDefs.ores, EnumOreType.MOONSTONE.ordinal(), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE));

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.illusionBlock"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.illusionBlock, BlockDefs.illusionBlock.GetCraftingQuantity(), 0), BlockDefs.illusionBlock.GetRecipeComponents(false));
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.illusionBlock"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(BlockDefs.illusionBlock, BlockDefs.illusionBlock.GetCraftingQuantity(), 1), BlockDefs.illusionBlock.GetRecipeComponents(true));
        //crafting recipes
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.itemOre"), new ResourceLocation(ArsMagica2.MODID, "shaped"),
                new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANECOMPOUND),
                "BRN", 
                "G G", 
                "NRB",
                'B', "stone",
                'R', "dustRedstone",
                'N', "netherrack",
                'G', "dustGlowstone");

        //spell crafting
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.craftingAltarRecipe"), new ResourceLocation(ArsMagica2.MODID, "shaped"),
                new ItemStack(ItemDefs.spellParchment, 1),
                "S",
                "P",
                "S",
                'S', "stickWood",
                'P', Items.PAPER);

        //spell book
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.spellBook"), new ResourceLocation(ArsMagica2.MODID, "shaped"),
                new ItemStack(ItemDefs.spellBook, 1),
                "SLL",
                "SPP",
                "SLL",
                'S', Items.STRING,
                'L', Items.LEATHER,
                'P', Items.PAPER);

        //crystal wrench
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalWrench"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.crystalWrench),
                "I I",
                "AVD",
                " I ",
                'I', "ingotIron",
                'A', BlockDefs.cerublossom,
                'D', BlockDefs.desertNova,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        //spell book colors
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 0 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BROWN.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//brown
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 1 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.CYAN.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//cyan
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 2 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.GRAY.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//gray
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 3 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIGHT_BLUE.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//lightblue
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 4 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.WHITE.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//white
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 5 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLACK.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//black
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 6 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.ORANGE.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//orange
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 7 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.PURPLE.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//purple
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 8 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLUE.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//blue
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 9 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.GREEN.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//green
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 10 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.YELLOW.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//yellow
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 11 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.RED.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//red
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 12 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIME.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//lime
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 13 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.PINK.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//pink
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 14 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.MAGENTA.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//magenta
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.spellBook , 1 , 15 ) , Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.SILVER.getMetadata ( ) ) , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) ) );//lightgray

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.runeBag"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.runeBag), 
        		"LLL", "W W", "LLL",
                'L', Items.LEATHER,
                'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magicBroom"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.magicBroom, 1), 
        		" S ",
                "ASA",
                " H ",
                'S', "stickWood",
                'A' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) ,
                'H', Blocks.HAY_BLOCK);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.woodenLeg"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.woodenLeg, 1), 
        		"P",
                "L",
                "S",
                'P', "plankWood",
                'L', "slabWood",
                'S', "stickWood");

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.evilBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.evilBook), Ingredient.fromItem(ItemDefs.woodenLeg),
                Ingredient.fromItem(ItemDefs.arcaneCompendium));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.journal" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.journal ) , Ingredient.fromStacks ( new ItemStack ( ItemDefs.essence , 1 , Affinity.ARCANE.getID ( ) ) , new ItemStack ( Items.WRITABLE_BOOK ) ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.manaPotionBundle, 1, 3), Ingredient.fromStacks(
                        new ItemStack(ItemDefs.lesserManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.lesserManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.lesserManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(Items.STRING)
                ));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.manaPotionBundle, 1, (1 << 8) + 3), Ingredient.fromStacks(
                        new ItemStack(ItemDefs.standardManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.standardManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.standardManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(Items.STRING)
                ));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.manaPotionBundle, 1, (2 << 8) + 3), Ingredient.fromStacks(
                        new ItemStack(ItemDefs.greaterManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.greaterManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.greaterManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(Items.STRING)
                ));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.manaPotionBundle, 1, (3 << 8) + 3), Ingredient.fromStacks(
                        new ItemStack(ItemDefs.epicManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.epicManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.epicManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(Items.STRING)
                ));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.manaPotionBundle, 1, (4 << 8) + 3), Ingredient.fromStacks(
                        new ItemStack(ItemDefs.legendaryManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.legendaryManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(ItemDefs.legendaryManaPotion, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(Items.STRING)
                ));

        //blank rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.blankRune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.blankRune, 2),
                " S ", 
                "SSS", 
                "SS ",
                'S', "cobblestone");
        //blue rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLUE.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLUE.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //red rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.RED.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.RED.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //yellow rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.YELLOW.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //orange rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.ORANGE.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //green rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.GREEN.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.GREEN.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //purple rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PURPLE.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.PURPLE.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //white rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.WHITE.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //black rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLACK.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //brown rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BROWN.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.BROWN.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //cyan rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.CYAN.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.CYAN.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //gray rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.GRAY.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.GRAY.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //light blue rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.LIGHT_BLUE.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIGHT_BLUE.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //light gray rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.SILVER.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.SILVER.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //magenta rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.MAGENTA.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.MAGENTA.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //pink rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PINK.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.PINK.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );
        //pink rune
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.rune, 1, EnumDyeColor.LIME.getDyeDamage()),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.LIME.getMetadata ( ) ) ,
                        new ItemStack ( ItemDefs.blankRune ) ) );

        //wizard chalk
        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.chalk" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                new ItemStack(ItemDefs.chalk, 1),
                Ingredient.fromStacks ( new ItemStack ( Items.DYE , 1 , EnumDyeColor.WHITE.getMetadata ( ) ) ,
                new ItemStack(Items.CLAY_BALL),
                        new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                new ItemStack(Items.FLINT),
                        new ItemStack ( Items.PAPER ) ) );

        //empty flicker jar
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.chalk"), new ResourceLocation(ArsMagica2.MODID, "shaped"),
                new ItemStack(ItemDefs.flickerJar, 1),
                "NWN",
                "G G",
                " G ",
                'W', BlockDefs.magicWall,
                'N', "nuggetGold",
                'G', "paneGlassColorless");

        //warding candle
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.wardingCandle"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.wardingCandle), 
        		"S",
                "F",
                "P",
                'S', Items.STRING,
                'F', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ANIMALFAT),
                'P', BlockDefs.witchwoodSingleSlab);

        //magitech goggles
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magitechGoggles"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.magitechGoggles), 
        		"LLL",
                "CGC",
                "TLT",
                'C', "gemChimerite",
                'T', "gemBlueTopaz",
                'L', Items.LEATHER,
                'G', "nuggetGold");

        //magitech staff
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.spellStaffMagitech"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.spellStaffMagitech), 
        		" GT",
                "G G",
                "GG ",
                'T', "gemBlueTopaz",
                'G', "nuggetGold");

        //armor recipes
        //MAGE
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.mageHood"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.mageHood, 1),
                "WLW", 
                "WRW", 
                " B ",
                'W', new ItemStack(Blocks.WOOL, 1, 12),
                'L', Items.LEATHER,
                'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PURPLE.getDyeDamage()),
                'B', new ItemStack(Items.POTIONITEM, 1, 0));
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.mageArmor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.mageArmor, 1),
                "RCR", 
                "WLW", 
                "WWW",
                'W', new ItemStack(Blocks.WOOL, 1, 12),
                'L', Items.LEATHER,
                'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()),
                'C', Items.COAL);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.mageLeggings"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.mageLeggings, 1),
                "WRW", 
                "WGW", 
                "L L",
                'W', new ItemStack(Blocks.WOOL, 1, 12),
                'L', Items.LEATHER,
                'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()),
                'G', Items.GUNPOWDER);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.magicBoots"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.mageBoots, 1),
                "R R", 
                "L L", 
                "WFW",
                'W', new ItemStack(Blocks.WOOL, 1, 12),
                'L', Items.LEATHER,
                'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
                'F', Items.FEATHER);
        //BATTLEMAGE
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.battlemageHood"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.battlemageHood, 1),
                "WLW", 
                "WRW", 
                " E ",
                'W', new ItemStack(Blocks.OBSIDIAN),
                'L', BlockDefs.goldInlay,
                'R', new ItemStack(ItemDefs.rune, 1, 1),
                'E', new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()));
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.battlemageArmor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.battlemageArmor, 1),
                "RER", 
                "WLW", 
                "WWW",
                'W', new ItemStack(Blocks.OBSIDIAN),
                'E', new ItemStack(ItemDefs.essence, 1, Affinity.EARTH.getID()),
                'R', new ItemStack(ItemDefs.rune, 1, 1),
                'L', BlockDefs.goldInlay);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.battlemageLeggings"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.battlemageLeggings, 1),
                "WRW", 
                "LEL", 
                "W W",
                'W', new ItemStack(Blocks.OBSIDIAN),
                'L', BlockDefs.goldInlay,
                'R', new ItemStack(ItemDefs.rune, 1, 1),
                'E', new ItemStack(ItemDefs.essence, 1, Affinity.FIRE.getID()));
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.battlemageBoots"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.battlemageBoots, 1),
                "R R", 
                "WEW", 
                "WLW",
                'W', new ItemStack(Blocks.OBSIDIAN),
                'L', BlockDefs.goldInlay,
                'R', new ItemStack(ItemDefs.rune, 1, 1),
                'E', new ItemStack(ItemDefs.essence, 1, Affinity.AIR.getID()));
        //ARCHMAGE
        /*GameRegistry.addShapedRecipe(new ItemStack(archmageHood, 1),
				new Object[]{
			"WPW", "WRW",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, 6)
		});
		GameRegistry.addShapedRecipe(new ItemStack(archmageArmor, 1),
				new Object[]{
			"RGR", "WPW", "WWW",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, 6),
			Character.valueOf('G'), Item.ingotGold
		});
		GameRegistry.addShapedRecipe(new ItemStack(archmageLeggings, 1),
				new Object[]{
			"WPW", "R R", "W W",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, 6)
		});
		GameRegistry.addShapedRecipe(new ItemStack(archmageBoots, 1),
				new Object[]{
			"P R", "W W", "W W",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, 6)
		});*/

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.essenceBag"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.essenceBag), 
        		"LLL", 
        		"WNW", 
        		"LLL",
                'L', Items.LEATHER,
                'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
                'N', "nuggetGold");

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.crystalPhylactery"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.crystalPhylactery), 
        		" B ", 
        		"GPG", 
        		" W ",
                'B', "gemMoonstone",
                'W', BlockDefs.magicWall,
                'G', "blockGlassColorless",
                'P', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM));

        //lesser mana potion
        //GameRegistry.addShapedRecipe(new ItemStack(Item.potion, 1, ))

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.keystone"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.keystone, 1), 
        		"GIG",
                "IVI",
                "GIG",
                'G', "ingotGold",
                'I', "ingotIron",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.itemOre" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) , Ingredient.fromStacks ( new ItemStack ( BlockDefs.cerublossom ) ,
                new ItemStack(BlockDefs.desertNova),
                new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaCake" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.manaCake , 3 , 0 ) , Ingredient.fromStacks ( new ItemStack ( BlockDefs.cerublossom ) ,
                new ItemStack(BlockDefs.desertNova),
                new ItemStack ( Items.SUGAR ) ,
                new ItemStack ( Items.WHEAT ) ) );

        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM), new ItemStack(ItemDefs.greaterManaPotion));
        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH), new ItemStack(ItemDefs.epicManaPotion));
        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM), new ItemStack(ItemDefs.legendaryManaPotion));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.lesserManaPotion" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.lesserManaPotion ) , Ingredient.fromStacks (
                new ItemStack(Items.WHEAT_SEEDS),
                new ItemStack(Items.SUGAR),
                new ItemStack(Items.POTIONITEM, 1, Short.MAX_VALUE)
        ));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaCake" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.standardManaPotion ) , Ingredient.fromStacks (
                new ItemStack(Items.GUNPOWDER),
                new ItemStack(ItemDefs.lesserManaPotion, 1, Short.MAX_VALUE)
        ));

//		GameRegistry.addShapedRecipe(new ShapelessOreRecipe(new ItemStack(ItemDefs.greaterManaPotion), new Object[]{
//				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
//				new ItemStack(ItemDefs.standardManaPotion, 1, Short.MAX_VALUE)
//		}));
//
//		GameRegistry.addShapedRecipe(new ShapelessOreRecipe(new ItemStack(ItemDefs.epicManaPotion), new Object[]{
//				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
//				new ItemStack(ItemDefs.greaterManaPotion, 1, Short.MAX_VALUE)
//		}));
//
//		GameRegistry.addShapelessRecipe(new ItemStack(ItemDefs.legendaryManaPotion), new Object[]{
//				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM),
//				new ItemStack(ItemDefs.epicManaPotion, 1, Short.MAX_VALUE)
//		});

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.liquidEssenceBottle ) , Ingredient.fromStacks ( new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_CHIMERITE ) ,
                new ItemStack(BlockDefs.tarmaRoot),
                UniversalBucket.getFilledBucket ( ForgeModContainer.getInstance ( ).universalBucket , BlockDefs.liquid_essence ) ) );

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.lesserFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.lesserFocus), ItemDefs.lesserFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.standardFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.standardFocus), ItemDefs.standardFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.greaterFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.greaterFocus), ItemDefs.greaterFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.manaFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.manaFocus), ItemDefs.manaFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.chargeFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.chargeFocus), ItemDefs.chargeFocus.getRecipeItems());

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.playerFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.playerFocus), ItemDefs.playerFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.mobFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.mobFocus), ItemDefs.mobFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.itemFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.itemFocus), ItemDefs.itemFocus.getRecipeItems());
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.creatureFocus"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.creatureFocus), ItemDefs.creatureFocus.getRecipeItems());

        //binding catalysts
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_AXE), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.GOLDEN_AXE);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_PICK), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.GOLDEN_PICKAXE);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_SHOVEL), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.GOLDEN_SHOVEL);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_SWORD), 
        		"SVS",
                "SAS",
                "SVS",
                'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM),
                'S', "slimeball",
                'A', Items.GOLDEN_SWORD);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_HOE), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.GOLDEN_HOE);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_BOW), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.BOW);
        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.bindingCatalyst"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.bindingCatalyst, 1, ItemBindingCatalyst.META_SHIELD), 
        		"SVS",
                "SAS",
                "SVS",
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S', "slimeball",
                'A', Items.SHIELD);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.itemKeystoneDoor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.itemKeystoneDoor, 1, ItemKeystoneDoor.KEYSTONE_DOOR), 
        		"PWP",
                "RRR",
                "PWP",
                'P', BlockDefs.witchwoodPlanks,
                'R', new ItemStack(ItemDefs.blankRune),
                'W', BlockDefs.magicWall);

        GameRegistry.addShapedRecipe(new ResourceLocation(ArsMagica2.MODID, "shaped.itemKeystoneDoor"), new ResourceLocation(ArsMagica2.MODID, "shaped"), new ItemStack(ItemDefs.itemKeystoneDoor, 1, ItemKeystoneDoor.SPELL_SEALED_DOOR), 
        		" G ",
                "SKS",
                " L ",
                'G', ItemDefs.greaterFocus,
                'S', ItemDefs.standardFocus,
                'L', ItemDefs.lesserFocus,
                'K', new ItemStack(ItemDefs.itemKeystoneDoor, 1, ItemKeystoneDoor.KEYSTONE_DOOR));

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.manaMartini" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.manaMartini ) , Ingredient.fromStacks ( new ItemStack ( Blocks.ICE ) ,
                new ItemStack ( Items.POTATO ) ,
                new ItemStack(Items.SUGAR),
                new ItemStack ( Items.STICK ) ,
                new ItemStack ( ItemDefs.standardManaPotion ) ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 0 ) , Ingredient.fromStacks (
                new ItemStack(Items.BOOK),
                new ItemStack(Items.STRING),
                new ItemStack(Items.FEATHER),
                new ItemStack ( Items.DYE , 1 , EnumDyeColor.BLACK.getMetadata ( ) ) )
        );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 1 ) , Ingredient.fromStacks (
                new ItemStack(Blocks.CARPET, 1, Short.MAX_VALUE),
                new ItemStack(Items.BOOK),
                new ItemStack ( ItemDefs.chalk ) )
        );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 2 ) , Ingredient.fromStacks (
                new ItemStack(ItemDefs.wardingCandle),
                new ItemStack(Items.FLINT_AND_STEEL),
                new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ANIMALFAT),
                new ItemStack(Items.GLASS_BOTTLE),
                new ItemStack ( Items.BOOK ) )
        );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.wordkbenchUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.workbenchUpgrade ) , Ingredient.fromStacks ( new ItemStack ( BlockDefs.magiciansWorkbench ) , new ItemStack ( Blocks.CHEST ) , new ItemStack ( Blocks.CRAFTING_TABLE ) , new ItemStack ( Blocks.CRAFTING_TABLE ) , new ItemStack ( Items.GOLD_INGOT ) ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.deficitCrystal" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , new ItemStack ( ItemDefs.deficitCrystal ) , Ingredient.fromStacks ( new ItemStack ( Items.DIAMOND ) , new ItemStack ( Items.ENDER_EYE ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) ) );
    }


    private static void addMetaSmeltingRecipe(Block input, int meta, ItemStack output) {
        ItemStack stack = new ItemStack(input, 1, meta);
        GameRegistry.addSmelting(stack, output, 0);
    }

    private static void createStorageBlockRecipe(ResourceLocation name, ResourceLocation group, ItemStack storageBlock, ItemStack storageItem) {
        GameRegistry.addShapedRecipe(name, group, storageBlock, "III",
                "III",
                "III",
                'I', new ItemStack(storageItem.getItem(), 1, storageItem.getItemDamage()));

        GameRegistry.addShapelessRecipe(name, group, new ItemStack(storageItem.getItem(), 9, storageItem.getItemDamage()), Ingredient.fromStacks(storageBlock));
    }

    private static void createTier2GemConverstionRecipies ( ItemStack stack , ItemStack dyeCode ) {
        if (stack.getItemDamage() != BlockCrystalMarker.META_LIKE_EXPORT) {
            GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , stack ,
                    Ingredient.fromStacks ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_LIKE_EXPORT ) , dyeCode ) );
        }

        if (stack.getItemDamage() != BlockCrystalMarker.META_REGULATE_EXPORT) {
            GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.crystalWrench" ) ,
                    new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) ,
                    stack ,
                    Ingredient.fromStacks ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) , dyeCode ) );
        }

        if (stack.getItemDamage() != BlockCrystalMarker.META_SET_EXPORT) {
            GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shapeless.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shapeless" ) , stack ,
                    Ingredient.fromStacks ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) ,
                            dyeCode ) );
        }
    }
}
