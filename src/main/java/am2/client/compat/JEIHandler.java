package am2.client.compat;

import am2.api.recipes.RecipeArsMagica;
import am2.api.recipes.RecipesEssenceRefiner;
import am2.client.compat.jei.EssenceRefinerRecipeCategory;
import am2.client.compat.jei.EssenceRefinerRecipeHandler;
import am2.client.compat.jei.EssenceRefinerRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class JEIHandler implements IModPlugin{

	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(RecipeArsMagica.class, EssenceRefinerRecipeWrapper::new, EssenceRefinerRecipeCategory.ID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new EssenceRefinerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}
}
