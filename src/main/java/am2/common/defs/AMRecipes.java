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
    public static void addShapedRecipes ( ) {
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magicWallRecipe" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.magicWall , 16 ) , "VSV" ,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S' , "stone" );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.craftingAltarRecipe" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.craftingAltar , 1 ) , "V" ,
                "S" ,
                'V' , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) ,
                'S' , "stone" );

        ObeliskFuelHelper.instance.registerFuelType ( s -> {
            if ( s.getItem ( ) == ItemDefs.itemOre && s.getItemDamage ( ) == ItemOre.META_VINTEUM )
                return 200;
            return 0;
        } );
        ObeliskFuelHelper.instance.registerFuelType ( s -> {
            UniversalBucket.getFilledBucket ( ForgeModContainer.getInstance ( ).universalBucket , BlockDefs.liquid_essence );
            FluidStack stack = FluidUtil.getFluidContained ( s );
            if ( stack == null || stack.getFluid ( ) != BlockDefs.liquid_essence )
                return 0;
            return stack.amount * 2;
        } );

        for ( AbstractFlickerFunctionality func : ArsMagicaAPI.getFlickerFocusRegistry ( ).getValuesCollection ( ) ) {
            if ( func != null ) {
                Object[] recipeItems = func.getRecipe ( );
                if ( recipeItems != null ) {
                    GameRegistry.addShapedRecipe ( new ResourceLocation ( "flickerFocusRecipe" ) , new ResourceLocation ( "shapedRecipies" ) , new ItemStack ( ItemDefs.flickerFocus , 1 , func.getMask ( )[ 0 ].getID ( ) ) , recipeItems );
                } else {
                    LogHelper.info ( "Flicker operator %s was registered with no recipe.  It is un-craftable.  This may have been intentional." , func.getClass ( ).getSimpleName ( ) );
                }
            }
        }


        //essence refiner
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.essenceRefiner" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.essenceRefiner , 1 ) ,
                "PDP" , "OAO" , "PPP" ,
                'P' , "plankWood" ,
                'O' , Blocks.OBSIDIAN ,
                'A' , "arcaneAsh" ,
                'D' , "gemDiamond" );

        //essence conduit
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.essenceConduit" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.essenceConduit , 1 ) , " C " , " S " , "SSS" ,
                "S" , "stone" ,
                "C" , "gemChimerite" );

        //summoner
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.summoner" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.summoner , 1 ) , "GVG" , "GOG" , "OOO" ,
                'G' , "ingotGold" ,
                'O' , Blocks.OBSIDIAN ,
                'V' , "dustVinteum" );

        //Calefactor
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.calefactor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.calefactor , 1 ) , "L L" ,
                "SRS" ,
                "SVS" ,
                'L' , new ItemStack ( Items.DYE , 1 , 4 ) , //lapis
                'S' , "stone" ,
                'R' , "dustRedstone" ,
                'V' , "dustVinteum" );

        //keystone recepticle
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.keystoneRecepticle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.keystoneRecepticle , 1 ) , "SVS" , "EPE" , "SVS" ,
                Character.valueOf ( 'P' ) , new ItemStack ( ItemDefs.essence , 1 , 9 ) ,
                Character.valueOf ( 'S' ) , Blocks.STONEBRICK ,
                Character.valueOf ( 'E' ) , Items.ENDER_EYE ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        //astral barrier
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.astralBarrier" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.astralBarrier , 1 ) , "WVW" , "E E" , "WVW" ,
                //Character.valueOf('P'), new ItemStack(ItemDefs.essence, 1, ItemDefs.essence.META_ENDER),
                Character.valueOf ( 'W' ) , Blocks.COBBLESTONE_WALL ,
                Character.valueOf ( 'E' ) , Items.ENDER_EYE ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.seerStone" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.seerStone , 1 ) , " E " , "SRS" ,
                Character.valueOf ( 'S' ) , "stone" , //stone wall
                Character.valueOf ( 'E' ) , Items.ENDER_EYE ,
                Character.valueOf ( 'R' ) , "dustRedstone" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.keystoneChest" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.keystoneChest ) , "WRW" , "WVW" , "WRW" ,
                Character.valueOf ( 'W' ) , "plankWood" ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , 0 ) ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.lectern" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.lectern ) , "SSS" , " P " ,
                Character.valueOf ( 'S' ) , "slabWood" ,
                Character.valueOf ( 'P' ) , "plankWood" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.occulus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.occulus ) , "SPS" , " S " , "CVC" ,
                Character.valueOf ( 'S' ) , Blocks.STONEBRICK ,
                Character.valueOf ( 'C' ) , Items.COAL ,
                Character.valueOf ( 'P' ) , "blockGlassColorless" ,
                Character.valueOf ( 'V' ) , "gemBlueTopaz" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaDrain" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.manaDrain ) , "WPW" , "VAV" , "WBW" ,
                Character.valueOf ( 'B' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'A' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.ARCANE.getID () ) ,
                Character.valueOf ( 'W' ) , BlockDefs.magicWall ,
                Character.valueOf ( 'P' ) , new ItemStack ( ItemDefs.core , 1 , ItemCore.META_PURE ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.arcaneReconstructor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.arcaneReconstructor ) , "SWS" , "VDV" , "SOS" ,
                Character.valueOf ( 'S' ) , "stone" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'D' ) , "gemDiamond" ,
                Character.valueOf ( 'W' ) , BlockDefs.magicWall ,
                Character.valueOf ( 'O' ) , Blocks.OBSIDIAN );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.arcaneDeconstructor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.arcaneDeconstructor ) , "IGR" ,
                "WDW" ,
                "WWW" ,
                Character.valueOf ( 'I' ) , ItemDefs.itemFocus ,
                Character.valueOf ( 'G' ) , "blockGlassColorless" ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.core , 1 , ItemCore.META_PURE ) ,
                Character.valueOf ( 'W' ) , BlockDefs.witchwoodPlanks ,
                Character.valueOf ( 'D' ) , ItemDefs.deficitCrystal );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.flickerLure" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.flickerLure ) , "CIV" ,
                "SSS" ,
                Character.valueOf ( 'C' ) , "gemChimerite" ,
                Character.valueOf ( 'I' ) , "ingotIron" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , Blocks.STONEBRICK );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaBattery" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.manaBattery ) , "IVI" ,
                "VAV" ,
                "IVI" ,
                Character.valueOf ( 'I' ) , "gemChimerite" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'A' ) , "arcaneAsh" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magicWall" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.magicWall , 16 , 0 ) , "VSV" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "stone" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.craftingAltar" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.craftingAltar ) , "V" ,
                "S" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "stone" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.vinteumTorch" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.vinteumTorch , 4 ) , "V" ,
                "S" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "stickWood" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.inscriptionTable" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.inscriptionTable ) , "TPF" ,
                "SSS" ,
                "W W" ,
                Character.valueOf ( 'T' ) , Blocks.TORCH ,
                Character.valueOf ( 'P' ) , ItemDefs.spellParchment ,
                Character.valueOf ( 'F' ) , Items.FEATHER ,
                Character.valueOf ( 'S' ) , "slabWood" ,
                Character.valueOf ( 'W' ) , "plankWood" );

        //GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONEBRICK, 1, 3), new Object[]{
        //		"SS",
        //		"SS",
        //		Character.valueOf('S'), Blocks.STONEBRICK
        //});

        //Inlays

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.redstoneInlay" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.redstoneInlay , 4 , 0 ) , "RRR" ,
                "RVR" ,
                "RRR" ,
                Character.valueOf ( 'R' ) , "dustRedstone" ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.ironInlay" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.ironInlay , 4 , 0 ) , "III" ,
                "IVI" ,
                "III" ,
                Character.valueOf ( 'I' ) , "ingotIron" ,
                Character.valueOf ( 'V' ) , "arcaneAsh" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.goldInlay" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.goldInlay , 4 , 0 ) , "GGG" ,
                "GVG" ,
                "GGG" ,
                Character.valueOf ( 'G' ) , "ingotGold" ,
                Character.valueOf ( 'V' ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.particleEmitter" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.particleEmitter ) , " C " ,
                "CIC" ,
                " C " ,
                Character.valueOf ( 'I' ) , BlockDefs.illusionBlock ,
                Character.valueOf ( 'C' ) , "gemChimerite" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.witchwoodPlanks" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.witchwoodPlanks , 4 ) , "W" ,
                Character.valueOf ( 'W' ) , BlockDefs.witchwoodLog );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.witchwoodSingleSlab" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.witchwoodSingleSlab , 6 ) , "WWW" ,
                Character.valueOf ( 'W' ) , BlockDefs.witchwoodPlanks );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.witchwoodStairs" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.witchwoodStairs , 4 ) , "  W" ,
                " WW" ,
                "WWW" ,
                Character.valueOf ( 'W' ) , BlockDefs.witchwoodPlanks );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.everstone" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.everstone ) , " B " ,
                "CSC" ,
                " B " ,
                Character.valueOf ( 'C' ) , "gemChimerite" ,
                Character.valueOf ( 'S' ) , "stone" ,
                Character.valueOf ( 'B' ) , "gemBlueTopaz" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magiciansWorkbench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.magiciansWorkbench ) , "COC" ,
                "SWS" ,
                "LHL" ,
                Character.valueOf ( 'C' ) , "craftingTableWood" ,
                Character.valueOf ( 'O' ) , new ItemStack ( Blocks.CARPET ) ,
                Character.valueOf ( 'W' ) , "logWood" ,
                Character.valueOf ( 'S' ) , "slabWood" ,
                Character.valueOf ( 'L' ) , "plankWood" ,
                Character.valueOf ( 'H' ) , "chestWood" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.slipstreamGenerator" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.slipstreamGenerator ) , "WWW" ,
                "FAF" ,
                "WWW" ,
                Character.valueOf ( 'W' ) , BlockDefs.witchwoodLog ,
                Character.valueOf ( 'F' ) , Items.FEATHER ,
                Character.valueOf ( 'A' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.AIR.getID ( ) ) );

        //Flicker Habitat
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.elementalAttuner" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.elementalAttuner ) , "IDI" ,
                "DBD" ,
                "IDI" ,
                Character.valueOf ( 'I' ) , "ingotIron" ,
                Character.valueOf ( 'D' ) , "dustVinteum" ,
                Character.valueOf ( 'B' ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.CHIMERITE.ordinal ( ) ) );

        //Import Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_IN ) , " G " ,
                "GDG" ,
                " G " ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeYellow" //Yellow Dye
        );


        //Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_OUT ) , " G " ,
                "GDG" ,
                " G " ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeBlue" //Lapis
        );

        //Final Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_FINAL_DEST ) , " G " ,
                "GDG" ,
                " G " ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeGray" //Lapis
        );

        //Like Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_LIKE_EXPORT ) , "GDG" ,
                "DED" ,
                "GDG" ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeGreen" , //Cactus Green
                Character.valueOf ( 'E' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_OUT ) );

        //Regulate Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) , "GDG" ,
                "DED" ,
                "GDG" ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyePurple" , //Purple Dye
                Character.valueOf ( 'E' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_OUT ) );

        //Set Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) , "GDG" ,
                "DED" ,
                "GDG" ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeLightBlue" , //Light Blue Dye
                Character.valueOf ( 'E' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_OUT ) );

        //Regulate Bidirectional
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_MULTI ) , "DSD" ,
                "GEG" ,
                "DSD" ,
                Character.valueOf ( 'S' ) , "gemSunstone" ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeOrange" ,
                Character.valueOf ( 'E' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) );

        //Set Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_IMPORT ) , "DSD" ,
                "GEG" ,
                "DSD" ,
                Character.valueOf ( 'S' ) , "gemSunstone" ,
                Character.valueOf ( 'G' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'D' ) , "dyeRed" ,
                Character.valueOf ( 'E' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) );

        //Spell Export Gem
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalMarker" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SPELL_EXPORT ) , "C C" ,
                "RPI" ,
                "C C" ,
                Character.valueOf ( 'P' ) , new ItemStack ( ItemDefs.core , 1 , ItemCore.META_PURE ) ,
                Character.valueOf ( 'I' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_IMPORT ) ,
                Character.valueOf ( 'C' ) , "dyeCyan" ,
                Character.valueOf ( 'R' ) , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_MULTI ) );

        //Gem Conversions
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_LIKE_EXPORT ) , "dyeGreen" );
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) , "dyePurple" );
        createTier2GemConverstionRecipies ( new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) , "dyeLightBlue" );

        //Obelisk
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.obelisk" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.obelisk ) , "VSV" ,
                "SCS" ,
                "VSV" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "stone" ,
                Character.valueOf ( 'C' ) , new ItemStack ( Blocks.STONEBRICK , 1 , 3 ) );

        //Armor Infuser
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.armorImbuer" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.armorImbuer ) , "ACA" ,
                "OPO" ,
                "OOO" ,
                Character.valueOf ( 'A' ) , BlockDefs.craftingAltar ,
                Character.valueOf ( 'C' ) , new ItemStack ( Blocks.CARPET , 1 , Short.MAX_VALUE ) ,
                Character.valueOf ( 'O' ) , Blocks.OBSIDIAN ,
                Character.valueOf ( 'P' ) , Blocks.ENCHANTING_TABLE );

        //storage blocks
        createStorageBlockRecipe ( new ResourceLocation ( ArsMagica2.MODID , "storage.moonstone" ) , new ResourceLocation ( ArsMagica2.MODID , "storage" ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.MOONSTONE.ordinal ( ) ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_MOONSTONE ) );
        createStorageBlockRecipe ( new ResourceLocation ( ArsMagica2.MODID , "storage.vinteum" ) , new ResourceLocation ( ArsMagica2.MODID , "storage" ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.VINTEUM.ordinal ( ) ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );
        createStorageBlockRecipe ( new ResourceLocation ( ArsMagica2.MODID , "storage.bluetopaz" ) , new ResourceLocation ( ArsMagica2.MODID , "storage" ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.BLUETOPAZ.ordinal ( ) ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_BLUE_TOPAZ ) );
        createStorageBlockRecipe ( new ResourceLocation ( ArsMagica2.MODID , "storage.sunstone" ) , new ResourceLocation ( ArsMagica2.MODID , "storage" ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.SUNSTONE.ordinal ( ) ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_SUNSTONE ) );
        createStorageBlockRecipe ( new ResourceLocation ( ArsMagica2.MODID , "storage.chimerite" ) , new ResourceLocation ( ArsMagica2.MODID , "storage" ) , new ItemStack ( BlockDefs.blocks , 1 , EnumBlockType.CHIMERITE.ordinal ( ) ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_CHIMERITE ) );

        //furnace recipes
        GameRegistry.addSmelting ( new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANECOMPOUND ) , new ItemStack ( ItemDefs.itemOre , 2 , ItemOre.META_ARCANEASH ) , 0 );
        GameRegistry.addSmelting ( new ItemStack ( BlockDefs.witchwoodLog , 1 ) , new ItemStack ( Items.COAL , 1 , 1 ) , 0.15f );

        addMetaSmeltingRecipe ( BlockDefs.ores , EnumOreType.VINTEUM.ordinal ( ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) );
        addMetaSmeltingRecipe ( BlockDefs.ores , EnumOreType.SUNSTONE.ordinal ( ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_SUNSTONE ) );
        addMetaSmeltingRecipe ( BlockDefs.ores , EnumOreType.BLUETOPAZ.ordinal ( ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_BLUE_TOPAZ ) );
        addMetaSmeltingRecipe ( BlockDefs.ores , EnumOreType.CHIMERITE.ordinal ( ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_CHIMERITE ) );
        addMetaSmeltingRecipe ( BlockDefs.ores , EnumOreType.MOONSTONE.ordinal ( ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_MOONSTONE ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.illusionBlock" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.illusionBlock , BlockDefs.illusionBlock.GetCraftingQuantity ( ) , 0 ) , BlockDefs.illusionBlock.GetRecipeComponents ( false ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.illusionBlock" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( BlockDefs.illusionBlock , BlockDefs.illusionBlock.GetCraftingQuantity ( ) , 1 ) , BlockDefs.illusionBlock.GetRecipeComponents ( true ) );
        //crafting recipes
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.itemOre" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANECOMPOUND ) ,
                "BRN" , "G G" , "NRB" ,
                Character.valueOf ( 'B' ) , "stone" ,
                Character.valueOf ( 'R' ) , "dustRedstone" ,
                Character.valueOf ( 'N' ) , "netherrack" ,
                Character.valueOf ( 'G' ) , "dustGlowstone" );

        //spell crafting
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.craftingAltarRecipe" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.spellParchment , 1 ) ,
                "S" ,
                "P" ,
                "S" ,
                Character.valueOf ( 'S' ) , "stickWood" ,
                Character.valueOf ( 'P' ) , Items.PAPER );

        //spell book
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.spellBook , 1 ) ,
                "SLL" ,
                "SPP" ,
                "SLL" ,
                Character.valueOf ( 'S' ) , Items.STRING ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'P' ) , Items.PAPER );

        //crystal wrench
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.crystalWrench ) ,
                "I I" ,
                "AVD" ,
                " I " ,
                Character.valueOf ( 'I' ) , "ingotIron" ,
                Character.valueOf ( 'A' ) , BlockDefs.cerublossom ,
                Character.valueOf ( 'D' ) , BlockDefs.desertNova ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        //spell book colors
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 0 ) , "dyeBrown" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //brown
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 1 ) , "dyeCyan" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //cyan
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 2 ) , "dyeGray" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //gray
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 3 ) , "dyeLightBlue" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //light blue
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 4 ) , "dyeWhite" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //white
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 5 ) , "dyeBlack" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //black
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 6 ) , "dyeOrange" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //orange
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 7 ) , "dyePurple" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //purple
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 8 ) , "dyeBlue" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //blue
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 9 ) , "dyeGreen" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //green
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 10 ) , "dyeYellow" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //yellow
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 11 ) , "dyeRed" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //red
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 12 ) , "dyeLime" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //lime
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 13 ) , "dyePink" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //pink
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 14 ) , "dyeMagenta" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //magenta
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellBook , 1 , 15 ) , "dyeLightGray" , new ItemStack ( ItemDefs.spellBook , 1 , OreDictionary.WILDCARD_VALUE ) );  //light gray

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.runeBag" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.runeBag ) , "LLL" , "W W" , "LLL" ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , OreDictionary.WILDCARD_VALUE ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magicBroom" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.magicBroom , 1 ) , " S " ,
                "ASA" ,
                " H " ,
                Character.valueOf ( 'S' ) , "stickWood" ,
                Character.valueOf ( 'A' ) , "arcaneAsh" ,
                Character.valueOf ( 'H' ) , Blocks.HAY_BLOCK );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.woodenLeg" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.woodenLeg , 1 ) , "P" ,
                "L" ,
                "S" ,
                Character.valueOf ( 'P' ) , "plankWood" ,
                Character.valueOf ( 'L' ) , "slabWood" ,
                Character.valueOf ( 'S' ) , "stickWood" );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.evilBook" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.evilBook ) , Ingredient.fromItem ( ItemDefs.woodenLeg ) ,
                Ingredient.fromItem ( ItemDefs.arcaneCompendium ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.journal" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.journal ) , Ingredient.fromStacks ( new ItemStack ( ItemDefs.essence , 1 , Affinity.ARCANE.getID ( ) ) , new ItemStack ( Items.WRITABLE_BOOK ) ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.manaPotionBundle , 1 , ( 0 << 8 ) + 3 ) , Ingredient.fromStacks (
                        new ItemStack ( ItemDefs.lesserManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.lesserManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.lesserManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( Items.STRING )
                ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.manaPotionBundle , 1 , ( 1 << 8 ) + 3 ) , Ingredient.fromStacks (
                        new ItemStack ( ItemDefs.standardManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.standardManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.standardManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( Items.STRING )
                ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.manaPotionBundle , 1 , ( 2 << 8 ) + 3 ) , Ingredient.fromStacks (
                        new ItemStack ( ItemDefs.greaterManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.greaterManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.greaterManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( Items.STRING )
                ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.manaPotionBundle , 1 , ( 3 << 8 ) + 3 ) , Ingredient.fromStacks (
                        new ItemStack ( ItemDefs.epicManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.epicManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.epicManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( Items.STRING )
                ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaPotionBundle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.manaPotionBundle , 1 , ( 4 << 8 ) + 3 ) , Ingredient.fromStacks (
                        new ItemStack ( ItemDefs.legendaryManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.legendaryManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( ItemDefs.legendaryManaPotion , 1 , OreDictionary.WILDCARD_VALUE ) ,
                        new ItemStack ( Items.STRING )
                ) );

        //blank rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.blankRune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.blankRune , 2 ) ,
                " S " , "SSS" , "SS " ,
                Character.valueOf ( 'S' ) , "cobblestone" );
        //blue rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.BLUE.getDyeDamage ( ) ) ,
                "dyeBlue" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //red rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.RED.getDyeDamage ( ) ) ,
                "dyeRed" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //yellow rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.YELLOW.getDyeDamage ( ) ) ,
                "dyeYellow" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //orange rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.ORANGE.getDyeDamage ( ) ) ,
                "dyeOrange" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //green rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.GREEN.getDyeDamage ( ) ) ,
                "dyeGreen" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //purple rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.PURPLE.getDyeDamage ( ) ) ,
                "dyePurple" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //white rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.WHITE.getDyeDamage ( ) ) ,
                "dyeWhite" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //black rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.BLACK.getDyeDamage ( ) ) ,
                "dyeBlack" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //brown rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.BROWN.getDyeDamage ( ) ) ,
                "dyeBrown" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //cyan rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.CYAN.getDyeDamage ( ) ) ,
                "dyeCyan" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //gray rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.GRAY.getDyeDamage ( ) ) ,
                "dyeGray" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //light blue rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.LIGHT_BLUE.getDyeDamage ( ) ) ,
                "dyeLightBlue" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //light gray rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.SILVER.getDyeDamage ( ) ) ,
                "dyeLightGray" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //magenta rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.MAGENTA.getDyeDamage ( ) ) ,
                "dyeMagenta" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //pink rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.PINK.getDyeDamage ( ) ) ,
                "dyePink" ,
                new ItemStack ( ItemDefs.blankRune ) );
        //pink rune
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.rune" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.LIME.getDyeDamage ( ) ) ,
                "dyeLime" ,
                new ItemStack ( ItemDefs.blankRune ) );

        //wizard chalk
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.chalk" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.chalk , 1 ) ,
                "dyeWhite" ,
                new ItemStack ( Items.CLAY_BALL ) ,
                "dustVinteum" ,
                new ItemStack ( Items.FLINT ) ,
                new ItemStack ( Items.PAPER ) );

        //empty flicker jar
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.chalk" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) ,
                new ItemStack ( ItemDefs.flickerJar , 1 ) ,
                "NWN" ,
                "G G" ,
                " G " ,
                Character.valueOf ( 'W' ) , BlockDefs.magicWall ,
                Character.valueOf ( 'N' ) , "nuggetGold" ,
                Character.valueOf ( 'G' ) , "paneGlassColorless" );

        //warding candle
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.wardingCandle" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.wardingCandle ) , "S" ,
                "F" ,
                "P" ,
                Character.valueOf ( 'S' ) , Items.STRING ,
                Character.valueOf ( 'F' ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ANIMALFAT ) ,
                Character.valueOf ( 'P' ) , BlockDefs.witchwoodSingleSlab );

        //magitech goggles
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magitechGoggles" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.magitechGoggles ) , "LLL" ,
                "CGC" ,
                "TLT" ,
                Character.valueOf ( 'C' ) , "gemChimerite" ,
                Character.valueOf ( 'T' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'G' ) , "nuggetGold" );

        //magitech staff
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.spellStaffMagitech" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.spellStaffMagitech ) , " GT" ,
                "G G" ,
                "GG " ,
                Character.valueOf ( 'T' ) , "gemBlueTopaz" ,
                Character.valueOf ( 'G' ) , "nuggetGold" );

        //armor recipes
        //MAGE
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.mageHood" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.mageHood , 1 ) ,
                "WLW" , "WRW" , " B " ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , 12 ) ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.PURPLE.getDyeDamage ( ) ) ,
                Character.valueOf ( 'B' ) , new ItemStack ( Items.POTIONITEM , 1 , 0 ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.mageArmor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.mageArmor , 1 ) ,
                "RCR" , "WLW" , "WWW" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , 12 ) ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.WHITE.getDyeDamage ( ) ) ,
                Character.valueOf ( 'C' ) , Items.COAL );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.mageLeggings" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.mageLeggings , 1 ) ,
                "WRW" , "WGW" , "L L" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , 12 ) ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.YELLOW.getDyeDamage ( ) ) ,
                Character.valueOf ( 'G' ) , Items.GUNPOWDER );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.magicBoots" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.mageBoots , 1 ) ,
                "R R" , "L L" , "WFW" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , 12 ) ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , EnumDyeColor.BLACK.getDyeDamage ( ) ) ,
                Character.valueOf ( 'F' ) , Items.FEATHER );
        //BATTLEMAGE
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.battlemageHood" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.battlemageHood , 1 ) ,
                "WLW" , "WRW" , " E " ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.OBSIDIAN ) ,
                Character.valueOf ( 'L' ) , BlockDefs.goldInlay ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , 1 ) ,
                Character.valueOf ( 'E' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.WATER.getID ( ) ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.battlemageArmor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.battlemageArmor , 1 ) ,
                "RER" , "WLW" , "WWW" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.OBSIDIAN ) ,
                Character.valueOf ( 'E' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.EARTH.getID ( ) ) ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , 1 ) ,
                Character.valueOf ( 'L' ) , BlockDefs.goldInlay );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.battlemageLeggings" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.battlemageLeggings , 1 ) ,
                "WRW" , "LEL" , "W W" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.OBSIDIAN ) ,
                Character.valueOf ( 'L' ) , BlockDefs.goldInlay ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , 1 ) ,
                Character.valueOf ( 'E' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.FIRE.getID ( ) ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.battlemageBoots" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.battlemageBoots , 1 ) ,
                "R R" , "WEW" , "WLW" ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.OBSIDIAN ) ,
                Character.valueOf ( 'L' ) , BlockDefs.goldInlay ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.rune , 1 , 1 ) ,
                Character.valueOf ( 'E' ) , new ItemStack ( ItemDefs.essence , 1 , Affinity.AIR.getID ( ) ) );
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

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.essenceBag" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.essenceBag ) , "LLL" , "WNW" , "LLL" ,
                Character.valueOf ( 'L' ) , Items.LEATHER ,
                Character.valueOf ( 'W' ) , new ItemStack ( Blocks.WOOL , 1 , OreDictionary.WILDCARD_VALUE ) ,
                Character.valueOf ( 'N' ) , "nuggetGold" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalPhylactery" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.crystalPhylactery ) , " B " , "GPG" , " W " ,
                Character.valueOf ( 'B' ) , "gemMoonstone" ,
                Character.valueOf ( 'W' ) , BlockDefs.magicWall ,
                Character.valueOf ( 'G' ) , "blockGlassColorless" ,
                Character.valueOf ( 'P' ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) );

        //lesser mana potion
        //GameRegistry.addShapedRecipe(new ItemStack(Item.potion, 1, ))

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.keystone" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.keystone , 1 ) , "GIG" ,
                "IVI" ,
                "GIG" ,
                Character.valueOf ( 'G' ) , "ingotGold" ,
                Character.valueOf ( 'I' ) , "ingotIron" ,
                Character.valueOf ( 'V' ) , "dustVinteum" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.itemOre" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) , new ItemStack ( BlockDefs.cerublossom ) ,
                new ItemStack ( BlockDefs.desertNova ) ,
                "dustVinteum" ,
                "arcaneAsh" );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaCake" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.manaCake , 3 , 0 ) , new ItemStack ( BlockDefs.cerublossom ) ,
                new ItemStack ( BlockDefs.desertNova ) ,
                new ItemStack ( Items.SUGAR ) ,
                "cropWheat" );

        BrewingRecipeRegistry.addRecipe ( PotionUtils.addPotionToItemStack ( new ItemStack ( Items.POTIONITEM ) , PotionTypes.AWKWARD ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_VINTEUM ) , new ItemStack ( ItemDefs.greaterManaPotion ) );
        BrewingRecipeRegistry.addRecipe ( PotionUtils.addPotionToItemStack ( new ItemStack ( Items.POTIONITEM ) , PotionTypes.AWKWARD ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ARCANEASH ) , new ItemStack ( ItemDefs.epicManaPotion ) );
        BrewingRecipeRegistry.addRecipe ( PotionUtils.addPotionToItemStack ( new ItemStack ( Items.POTIONITEM ) , PotionTypes.AWKWARD ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) , new ItemStack ( ItemDefs.legendaryManaPotion ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.lesserManaPotion" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.lesserManaPotion ) , Ingredient.fromStacks (
                new ItemStack ( Items.WHEAT_SEEDS ) ,
                new ItemStack ( Items.SUGAR ) ,
                new ItemStack ( Items.POTIONITEM , 1 , Short.MAX_VALUE )
        ) );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaCake" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.standardManaPotion ) , Ingredient.fromStacks (
                new ItemStack ( Items.GUNPOWDER ) ,
                new ItemStack ( ItemDefs.lesserManaPotion , 1 , Short.MAX_VALUE )
        ) );

//		GameRegistry.addShapedRecipe(new ShapelessOreRecipe(new ItemStack(ItemDefs.greaterManaPotion), new Object[]{
//				"dustVinteum",
//				new ItemStack(ItemDefs.standardManaPotion, 1, Short.MAX_VALUE)
//		}));
//
//		GameRegistry.addShapedRecipe(new ShapelessOreRecipe(new ItemStack(ItemDefs.epicManaPotion), new Object[]{
//				"arcaneAsh",
//				new ItemStack(ItemDefs.greaterManaPotion, 1, Short.MAX_VALUE)
//		}));
//
//		GameRegistry.addShapelessRecipe(new ItemStack(ItemDefs.legendaryManaPotion), new Object[]{
//				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM),
//				new ItemStack(ItemDefs.epicManaPotion, 1, Short.MAX_VALUE)
//		});

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.liquidEssenceBottle ) , "gemChimerite" ,
                new ItemStack ( BlockDefs.tarmaRoot ) ,
                UniversalBucket.getFilledBucket ( ForgeModContainer.getInstance ( ).universalBucket , BlockDefs.liquid_essence ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.lesserFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.lesserFocus ) , ItemDefs.lesserFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.standardFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.standardFocus ) , ItemDefs.standardFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.greaterFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.greaterFocus ) , ItemDefs.greaterFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.manaFocus ) , ItemDefs.manaFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.chargeFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.chargeFocus ) , ItemDefs.chargeFocus.getRecipeItems ( ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.playerFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.playerFocus ) , ItemDefs.playerFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.mobFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.mobFocus ) , ItemDefs.mobFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.itemFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.itemFocus ) , ItemDefs.itemFocus.getRecipeItems ( ) );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.creatureFocus" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.creatureFocus ) , ItemDefs.creatureFocus.getRecipeItems ( ) );

        //binding catalysts
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_AXE ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.GOLDEN_AXE );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_PICK ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.GOLDEN_PICKAXE );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_SHOVEL ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.GOLDEN_SHOVEL );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_SWORD ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_PURIFIED_VINTEUM ) ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.GOLDEN_SWORD );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_HOE ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.GOLDEN_HOE );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_BOW ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.BOW );
        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.bindingCatalyst" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.bindingCatalyst , 1 , ItemBindingCatalyst.META_SHIELD ) , "SVS" ,
                "SAS" ,
                "SVS" ,
                Character.valueOf ( 'V' ) , "dustVinteum" ,
                Character.valueOf ( 'S' ) , "slimeball" ,
                Character.valueOf ( 'A' ) , Items.SHIELD );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.itemKeystoneDoor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.itemKeystoneDoor , 1 , ItemKeystoneDoor.KEYSTONE_DOOR ) , "PWP" ,
                "RRR" ,
                "PWP" ,
                Character.valueOf ( 'P' ) , BlockDefs.witchwoodPlanks ,
                Character.valueOf ( 'R' ) , new ItemStack ( ItemDefs.blankRune ) ,
                Character.valueOf ( 'W' ) , BlockDefs.magicWall );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.itemKeystoneDoor" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.itemKeystoneDoor , 1 , ItemKeystoneDoor.SPELL_SEALED_DOOR ) , " G " ,
                "SKS" ,
                " L " ,
                Character.valueOf ( 'G' ) , ItemDefs.greaterFocus ,
                Character.valueOf ( 'S' ) , ItemDefs.standardFocus ,
                Character.valueOf ( 'L' ) , ItemDefs.lesserFocus ,
                Character.valueOf ( 'K' ) , new ItemStack ( ItemDefs.itemKeystoneDoor , 1 , ItemKeystoneDoor.KEYSTONE_DOOR ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.manaMartini" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.manaMartini ) , new ItemStack ( Blocks.ICE ) ,
                "cropPotato" ,
                new ItemStack ( Items.SUGAR ) ,
                "stickWood" ,
                new ItemStack ( ItemDefs.standardManaPotion ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 0 ) ,
                new ItemStack ( Items.BOOK ) ,
                new ItemStack ( Items.STRING ) ,
                new ItemStack ( Items.FEATHER ) ,
                "dyeBlack"
        );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 1 ) ,
                new ItemStack ( Blocks.CARPET , 1 , Short.MAX_VALUE ) ,
                new ItemStack ( Items.BOOK ) ,
                new ItemStack ( ItemDefs.chalk )
        );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.inscriptionUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.inscriptionUpgrade , 1 , 2 ) ,
                new ItemStack ( ItemDefs.wardingCandle ) ,
                new ItemStack ( Items.FLINT_AND_STEEL ) ,
                new ItemStack ( ItemDefs.itemOre , 1 , ItemOre.META_ANIMALFAT ) ,
                new ItemStack ( Items.GLASS_BOTTLE ) ,
                new ItemStack ( Items.BOOK )
        );

        GameRegistry.addShapelessRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.wordkbenchUpgrade" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.workbenchUpgrade ) , Ingredient.fromStacks ( new ItemStack ( BlockDefs.magiciansWorkbench ) , new ItemStack ( Blocks.CHEST ) , new ItemStack ( Blocks.CRAFTING_TABLE ) , new ItemStack ( Blocks.CRAFTING_TABLE ) , new ItemStack ( Items.GOLD_INGOT ) ) );

        GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.deficitCrystal" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , new ItemStack ( ItemDefs.deficitCrystal ) , new ItemStack ( Items.DIAMOND ) , "arcaneAsh" , new ItemStack ( Items.ENDER_EYE ) );
    }


    private static void addMetaSmeltingRecipe ( Block input , int meta , ItemStack output ) {
        ItemStack stack = new ItemStack ( input , 1 , meta );
        GameRegistry.addSmelting ( stack , output , 0 );
    }

    private static void createStorageBlockRecipe ( ResourceLocation name , ResourceLocation group , ItemStack storageBlock , ItemStack storageItem ) {
        GameRegistry.addShapedRecipe ( name , group , storageBlock , "III" ,
                "III" ,
                "III" ,
                Character.valueOf ( 'I' ) , new ItemStack ( storageItem.getItem ( ) , 1 , storageItem.getItemDamage ( ) ) );

        GameRegistry.addShapelessRecipe ( name , group , new ItemStack ( storageItem.getItem ( ) , 9 , storageItem.getItemDamage ( ) ) , Ingredient.fromStacks ( storageBlock ) );
    }

    private static void createTier2GemConverstionRecipies ( ItemStack stack , String dyeCode ) {
        if ( stack.getItemDamage ( ) != BlockCrystalMarker.META_LIKE_EXPORT ) {
            GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , stack , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_LIKE_EXPORT ) ,
                    dyeCode );
        }

        if ( stack.getItemDamage ( ) != BlockCrystalMarker.META_REGULATE_EXPORT ) {
            GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , stack , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_REGULATE_EXPORT ) ,
                    dyeCode );
        }

        if ( stack.getItemDamage ( ) != BlockCrystalMarker.META_SET_EXPORT ) {
            GameRegistry.addShapedRecipe ( new ResourceLocation ( ArsMagica2.MODID , "shaped.crystalWrench" ) , new ResourceLocation ( ArsMagica2.MODID , "shaped" ) , stack , new ItemStack ( BlockDefs.crystalMarker , 1 , BlockCrystalMarker.META_SET_EXPORT ) ,
                    dyeCode );
        }
    }
}
