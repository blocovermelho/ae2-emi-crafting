package org.blocovermelho.ae2emicrafting.client.recipes.generator;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;

import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_HEIGHT;
import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_WIDTH;

public class RecipeGenerator {
    public static Recipe<?> createFakeRecipe(EmiRecipe display) {
        var ingredients = DefaultedList.ofSize(CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT,
                Ingredient.EMPTY);

        for (int i = 0; i < Math.min(display.getInputs().size(), ingredients.size()); i++) {
            var ingredient = Ingredient.ofStacks(display.getInputs().get(i).getEmiStacks().stream()
                    .map(EmiStack::getItemStack)
                    .filter(is -> !is.isEmpty()));
            ingredients.set(i, ingredient);
        }

        return new ShapedRecipe(display.getId(), "", CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT,ingredients, ItemStack.EMPTY);
    }
}
