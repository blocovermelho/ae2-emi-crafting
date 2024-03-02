package org.blocovermelho.ae2emicrafting.client.helper;

import appeng.integration.modules.jeirei.EncodingHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;

import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_HEIGHT;
import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_WIDTH;

public class RecipeUtils {
    public static boolean isCraftingRecipe(Recipe<?> recipe, EmiRecipe emiRecipe) {
        return EncodingHelper.isSupportedCraftingRecipe(recipe)
                || emiRecipe.getCategory().equals(VanillaEmiRecipeCategories.CRAFTING);
    }

    public static boolean  fitsIn3x3Grid(Recipe<?> recipe) {
        if (recipe != null) {
            return recipe.fits(CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT);
        } else {
            return true;
        }
    }

    public static Map<Integer, Ingredient> getGuiSlotToIngredientMap(Recipe<?> recipe) {
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        int width;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            width = shapedRecipe.getWidth();
        } else {
            width = 3;
        }

        HashMap<Integer, Ingredient> result = new HashMap(ingredients.size());

        for(int i = 0; i < ingredients.size(); ++i) {
            int guiSlot = i / width * 3 + i % width;
            Ingredient ingredient = (Ingredient)ingredients.get(i);
            if (!ingredient.isEmpty()) {
                result.put(guiSlot, ingredient);
            }
        }

        return result;
    }
}
