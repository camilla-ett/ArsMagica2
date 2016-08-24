package am2.utils;

import java.util.ArrayList;
import java.util.List;

import am2.LogHelper;
import am2.api.SpellRegistry;
import am2.api.event.SpellRecipeItemsEvent;
import am2.api.spell.AbstractSpellPart;
import am2.defs.ItemDefs;
import am2.power.PowerTypes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeUtils {
//	public static ItemStack parsePotionMeta(String potionDefinition){
//		String[] potionSections = potionDefinition.split("&");
//
//		int potionMeta = 0;
//		ItemStack potion = new ItemStack(Items.POTIONITEM);
//
//		for (String s : potionSections){
//			s = s.trim();
//
//			if (s.contains("+")) continue;
//
//			boolean bitSet = true;
//			for (char c : s.toCharArray()){
//				if (c == '!'){
//					bitSet = false;
//					continue;
//				}
//				if (Character.isDigit(c)){
//					int value = Character.getNumericValue(c);
//					Potion.REGISTRY.getObjectById(value);
//					if (bitSet){
//						potionMeta = setBit(potionMeta, value);
//					}else{
//						potionMeta = clearBit(potionMeta, value);
//					}
//					bitSet = true;
//					continue;
//				}
//			}
//
//		}
//
//		return potionMeta;
//	}
	
	public static IRecipe getRecipeFor(ItemStack item){

		if (item == null || item.getItem() == null) return null;

		try{
			List<IRecipe> list = CraftingManager.getInstance().getRecipeList();
			ArrayList<IRecipe> possibleRecipes = new ArrayList<>();
			for (IRecipe recipe : list){
				ItemStack output = ((IRecipe)recipe).getRecipeOutput();
				if (output == null) continue;
				if (output.getItem() == item.getItem() && (output.getItemDamage() == Short.MAX_VALUE || output.getItemDamage() == item.getItemDamage())){
					possibleRecipes.add(recipe);
				}
			}

			if (possibleRecipes.size() > 0){
				for (Object recipe : possibleRecipes){
					if (((IRecipe)recipe).getRecipeOutput().getItemDamage() == item.getItemDamage()){
						return (IRecipe)recipe;
					}
				}
				return (IRecipe)possibleRecipes.get(0);
			}
		}catch (Throwable t){

		}

		return null;
	}

	public static int[] ParseEssenceIDs(String s){
		if (s.toLowerCase().equals("e:*")){
			int[] all = new int[PowerTypes.all().size()];
			int count = 0;
			for (PowerTypes type : PowerTypes.all()){
				all[count++] = type.ID();
			}
			return all;
		}
		s = s.toLowerCase().replace("e:", "");
		String[] split = s.split("\\|");
		int[] ids = new int[split.length];
		for (int i = 0; i < split.length; ++i){
			try{
				ids[i] = Integer.parseInt(split[i]);
			}catch (NumberFormatException nex){
				LogHelper.warn("Invalid power type ID while parsing value " + s);
				ids[i] = 0;
			}
		}
		return ids;
	}
	
	public static ArrayList<ItemStack> getConvRecipe(AbstractSpellPart part) {
		ArrayList<ItemStack> list = new ArrayList<>();
		if (part == null){
			LogHelper.error("Unable to write recipe to book.  Recipe part is null!");
			return list;
		}
		
		Object[] recipeItems = part.getRecipe();
		SpellRecipeItemsEvent event = new SpellRecipeItemsEvent(SpellRegistry.getSkillFromPart(part).getID(), recipeItems);
		MinecraftForge.EVENT_BUS.post(event);
		recipeItems = event.recipeItems;

		if (recipeItems == null){
			LogHelper.error("Unable to write recipe to book.  Recipe items are null for part " + SpellRegistry.getSkillFromPart(part).getName() + "!");
			return list;
		}
		for (int i = 0; i < recipeItems.length; ++i){
			Object o = recipeItems[i];
			String materialkey = "";
			int qty = 1;
			ItemStack recipeStack = null;
			if (o instanceof ItemStack){
				materialkey = ((ItemStack)o).getDisplayName();
				recipeStack = (ItemStack)o;
			}else if (o instanceof Item){
				recipeStack = new ItemStack((Item)o);
				materialkey = ((Item)o).getItemStackDisplayName(new ItemStack((Item)o));
			}else if (o instanceof Block){
				recipeStack = new ItemStack((Block)o);
				materialkey = ((Block)o).getLocalizedName();
			}else if (o instanceof String){
				if (((String)o).startsWith("E:")){
					int[] ids = RecipeUtils.ParseEssenceIDs((String)o);
					materialkey = "Essence (";
					for (int powerID : ids){
						PowerTypes type = PowerTypes.getByID(powerID);
						materialkey += type.name() + "/";
					}

					if (materialkey.equals("Essence (")){
						++i;
						continue;
					}

					o = recipeItems[++i];
					if (materialkey.startsWith("Essence (")){
						materialkey = materialkey.substring(0, materialkey.lastIndexOf("/")) + ")";
						qty = (Integer)o;
						int flag = 0;
						for (int f : ids){
							flag |= f;
						}

						recipeStack = new ItemStack(ItemDefs.etherium, qty, flag);
					}

				}else{
					List<ItemStack> ores = OreDictionary.getOres((String)o);
					recipeStack = ores.size() > 0 ? ores.get(1) : null;
					materialkey = (String)o;
				}
			}
			list.add(recipeStack);
		}
		return list;
	}

	public static Object[] getRecipeItems(Object recipe){
		if (recipe instanceof ShapedRecipes){
			return getShapedRecipeItems((ShapedRecipes)recipe);
		}else if (recipe instanceof ShapelessRecipes){
			return getShapelessRecipeItems((ShapelessRecipes)recipe);
		}else if (recipe instanceof ShapedOreRecipe){
			return getShapedOreRecipeItems((ShapedOreRecipe)recipe);
		}else if (recipe instanceof ShapelessOreRecipe){
			return getShapelessOreRecipeItems((ShapelessOreRecipe)recipe);
		}
		return new Object[0];
	}
	
	private static Object[] getShapedRecipeItems(ShapedRecipes recipe){
		return recipe.recipeItems;
	}

	private static Object[] getShapelessRecipeItems(ShapelessRecipes recipe){
		return recipe.recipeItems.toArray();
	}

	private static Object[] getShapedOreRecipeItems(ShapedOreRecipe recipe){
		Object[] components = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, "input");
		return components;
	}

	private static Object[] getShapelessOreRecipeItems(ShapelessOreRecipe recipe){
		ArrayList<Object> components = ReflectionHelper.getPrivateValue(ShapelessOreRecipe.class, recipe, "input");
		return components.toArray();
	}
}