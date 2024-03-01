package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.core.localization.ItemModText;
import appeng.integration.modules.jeirei.CraftingHelper;
import appeng.menu.me.items.CraftingTermMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.recipe.Recipe;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler;
import org.blocovermelho.ae2emicrafting.client.helper.rendering.Result;

import static org.blocovermelho.ae2emicrafting.client.helper.RecipeUtils.*;
import static org.blocovermelho.ae2emicrafting.client.recipes.generator.RecipeGenerator.createFakeRecipe;

public class Ae2CraftingHandler<T extends CraftingTermMenu> extends Ae2BaseRecipeHandler<T> {
    public Ae2CraftingHandler(Class<T> containerClass) {
        super(containerClass);
    }

    @Override
    protected Result transferRecipe(T menu, Recipe<?> recipe, EmiRecipe emiRecipe, boolean doTransfer) {
        boolean craftingRecipe = isCraftingRecipe(recipe, emiRecipe);
        if (!craftingRecipe) {
            return Result.createNotApplicable();
        }

        if (!fitsIn3x3Grid(recipe)) {
            return Result.createFailed(ItemModText.RECIPE_TOO_LARGE.text());
        }

        if (recipe == null) {
            recipe = createFakeRecipe(emiRecipe);
        }

        // Find missing ingredient
        var slotToIngredientMap = getGuiSlotToIngredientMap(recipe);
        var missingSlots = menu.findMissingIngredients(getGuiSlotToIngredientMap(recipe));

        if (missingSlots.missingSlots().size() == slotToIngredientMap.size()) {
            // All missing, can't do much...
            return Result.createFailed(ItemModText.NO_ITEMS.text(), missingSlots.missingSlots());
        }

        if (!doTransfer) {
            if (missingSlots.anyMissing() || missingSlots.anyCraftable()) {
                // Highlight the slots with missing ingredients
                return new Result.PartiallyCraftable(missingSlots);
            }
        } else {
            // Thank you RS for pioneering this amazing feature! :)
            boolean craftMissing = HandledScreen.hasControlDown();
            CraftingHelper.performTransfer(menu, recipe, craftMissing);
        }

        // No error
        return Result.createSuccessful();
    }
}
