package am2.api.recipes;

import am2.api.affinity.Affinity;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemCore;
import am2.common.items.ItemOre;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class RecipesEssenceRefiner extends RecipesArsMagica{
	private static final RecipesEssenceRefiner essenceExtractorRecipesBase = new RecipesEssenceRefiner();

	public static final RecipesEssenceRefiner essenceRefinement(){
		return essenceExtractorRecipesBase;
	}

	private RecipesEssenceRefiner(){
		RecipeList = new HashMap<Integer, RecipeArsMagica>();
		InitRecipes();
	}

	private void InitRecipes(){
		//arcane essence
		AddRecipe(new ItemStack[]{
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.ARCANE.getID()));
		//earth essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Blocks.DIRT),
						new ItemStack(Blocks.STONE),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Blocks.STONE),
						new ItemStack(Blocks.OBSIDIAN)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.EARTH.getID()));
		//air essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.FEATHER),
						new ItemStack(BlockDefs.tarmaRoot),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(BlockDefs.tarmaRoot),
						new ItemStack(Items.FEATHER)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.AIR.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(BlockDefs.tarmaRoot),
						new ItemStack(Items.FEATHER),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.FEATHER),
						new ItemStack(BlockDefs.tarmaRoot)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.AIR.getID()));
		//fire essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.COAL),
						new ItemStack(Items.BLAZE_POWDER),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.BLAZE_POWDER),
						new ItemStack(Items.COAL)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.FIRE.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.BLAZE_POWDER),
						new ItemStack(Items.COAL),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.COAL),
						new ItemStack(Items.BLAZE_POWDER)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.FIRE.getID()));
		//water essence
		AddRecipe(new ItemStack[]{
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(Items.WATER_BUCKET),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.WATER_BUCKET),
						new ItemStack(BlockDefs.wakebloom)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()));

		AddRecipe(new ItemStack[]{
						new ItemStack(Items.WATER_BUCKET),
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(Items.WATER_BUCKET)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()));

		AddRecipe(new ItemStack[]{
						new ItemStack(Items.POTIONITEM, 1, 0),
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(Items.POTIONITEM, 1, 0)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()));

		AddRecipe(new ItemStack[]{
						new ItemStack(BlockDefs.wakebloom),
						new ItemStack(Items.POTIONITEM, 1, 0),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.POTIONITEM, 1, 0),
						new ItemStack(BlockDefs.wakebloom)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()));
		//ice essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Blocks.SNOW),
						new ItemStack(Blocks.ICE),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Blocks.ICE),
						new ItemStack(Blocks.SNOW)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.ICE.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(Blocks.ICE),
						new ItemStack(Blocks.SNOW),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Blocks.SNOW),
						new ItemStack(Blocks.ICE)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.ICE.getID()));
		//lightning essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.REDSTONE),
						new ItemStack(Items.GLOWSTONE_DUST),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.GLOWSTONE_DUST),
						new ItemStack(Items.REDSTONE),
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.LIGHTNING.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.GLOWSTONE_DUST),
						new ItemStack(Items.REDSTONE),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.REDSTONE),
						new ItemStack(Items.GLOWSTONE_DUST),
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.LIGHTNING.getID()));
		//plant essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Blocks.LEAVES, 1, -1),
						new ItemStack(Blocks.WATERLILY),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Blocks.CACTUS),
						new ItemStack(Blocks.VINE)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.NATURE.getID()));
		//life essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.EGG),
						new ItemStack(Items.GOLDEN_APPLE, 1, 0),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.GOLDEN_APPLE, 1, 0),
						new ItemStack(Items.EGG)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.LIFE.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.GOLDEN_APPLE),
						new ItemStack(Items.EGG, 1, 0),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.EGG, 1, 0),
						new ItemStack(Items.GOLDEN_APPLE)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.LIFE.getID()));
		//ender essence
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.ENDER_PEARL),
						new ItemStack(Items.ENDER_EYE),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.ENDER_EYE),
						new ItemStack(Items.ENDER_PEARL)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()));
		AddRecipe(new ItemStack[]{
						new ItemStack(Items.ENDER_EYE),
						new ItemStack(Items.ENDER_PEARL),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(Items.ENDER_PEARL),
						new ItemStack(Items.ENDER_EYE)
				},
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()));

		//base essence core
		AddRecipe(new ItemStack[]{
						new ItemStack(ItemDefs.essence, 1, Affinity.AIR.getID()),
						new ItemStack(ItemDefs.essence, 1, Affinity.WATER.getID()),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.essence, 1, Affinity.FIRE.getID()),
						new ItemStack(ItemDefs.essence, 1, Affinity.EARTH.getID())
				},
				new ItemStack(ItemDefs.core, 1, ItemCore.META_BASE_CORE));
		//high essence core
		AddRecipe(new ItemStack[]{
						new ItemStack(ItemDefs.essence, 1, Affinity.LIGHTNING.getID()),
						new ItemStack(ItemDefs.essence, 1, Affinity.ICE.getID()),
						new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
						new ItemStack(ItemDefs.essence, 1, Affinity.NATURE.getID()),
						new ItemStack(ItemDefs.essence, 1, Affinity.ARCANE.getID())
				},
				new ItemStack(ItemDefs.core, 1, ItemCore.META_HIGH_CORE));
		//pure essence
		AddRecipe(new ItemStack[]{
						new ItemStack(ItemDefs.core, 1, ItemCore.META_HIGH_CORE),
						new ItemStack(ItemDefs.essence, 1, Affinity.LIFE.getID()),
						new ItemStack(Items.DIAMOND),
						new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
						new ItemStack(ItemDefs.core, 1, ItemCore.META_BASE_CORE)
				},
				new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE));

		AddRecipe(new ItemStack[]{
						new ItemStack(ItemDefs.core, 1, ItemCore.META_HIGH_CORE),
						new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
						new ItemStack(Items.DIAMOND),
						new ItemStack(ItemDefs.essence, 1, Affinity.LIFE.getID()),
						new ItemStack(ItemDefs.core, 1, ItemCore.META_BASE_CORE)
				},
				new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE));

		//deficit crystal
		AddRecipe(new ItemStack[]{
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
				new ItemStack(Items.MAGMA_CREAM),
				new ItemStack(Items.EMERALD),
				new ItemStack(Blocks.OBSIDIAN),
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
		}, new ItemStack(ItemDefs.deficitCrystal));

		AddRecipe(new ItemStack[]{
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
				new ItemStack(Blocks.OBSIDIAN),
				new ItemStack(Items.EMERALD),
				new ItemStack(Items.MAGMA_CREAM),
				new ItemStack(ItemDefs.essence, 1, Affinity.ENDER.getID()),
		}, new ItemStack(ItemDefs.deficitCrystal));
	}
}
