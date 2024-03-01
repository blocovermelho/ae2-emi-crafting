package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.core.localization.ItemModText;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.items.PatternEncodingTermMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.recipe.Recipe;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackHelper;
import org.blocovermelho.ae2emicrafting.client.helper.rendering.Result;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.blocovermelho.ae2emicrafting.client.helper.RecipeUtils.fitsIn3x3Grid;
import static org.blocovermelho.ae2emicrafting.client.helper.RecipeUtils.isCraftingRecipe;

public class Ae2PatternTerminalHandler<T extends PatternEncodingTermMenu> extends Ae2BaseRecipeHandler<T> {
    public Ae2PatternTerminalHandler(Class<T> containerClass) {
        super(containerClass);
    }

    @Override
    protected Result transferRecipe(T menu, @Nullable Recipe<?> recipe, EmiRecipe emiRecipe, boolean doTransfer) {

        var recipeId = recipe != null ? recipe.getId() : null;


        // Crafting recipe slots are not grouped, hence they must fit into the 3x3 grid.
        boolean craftingRecipe = isCraftingRecipe(recipe, emiRecipe);
        if (craftingRecipe && !fitsIn3x3Grid(recipe)) {
            return Result.createFailed(ItemModText.RECIPE_TOO_LARGE.text());
        }

        if (doTransfer) {
            if (craftingRecipe && recipeId != null) {
                EncodingHelper.encodeCraftingRecipe(menu,
                        recipe,
                        getGuiIngredientsForCrafting(emiRecipe),
                        stack -> true);
            } else {
                EncodingHelper.encodeProcessingRecipe(menu,
                        EmiStackHelper.ofInputs(emiRecipe),
                        EmiStackHelper.ofOutputs(emiRecipe));
            }
        } else {
            var repo = menu.getClientRepo();
            Set<AEKey> craftableKeys = repo != null ? repo.getAllEntries().stream()
                    .filter(GridInventoryEntry::isCraftable)
                    .map(GridInventoryEntry::getWhat)
                    .collect(Collectors.toSet()) : Set.of();

            return new Result.EncodeWithCraftables(craftableKeys);
        }

        return Result.createSuccessful();
    }

    /**
     * In case the recipe does not report inputs, we will use the inputs shown on the EMI GUI instead.
     */
    private List<List<GenericStack>> getGuiIngredientsForCrafting(EmiRecipe emiRecipe) {
        var result = new ArrayList<List<GenericStack>>(CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT);
        for (int i = 0; i < CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT; i++) {
            var stacks = new ArrayList<GenericStack>();

            if (i < emiRecipe.getInputs().size()) {
                for (var emiStack : emiRecipe.getInputs().get(i).getEmiStacks()) {
                    var genericStack = EmiStackHelper.toGenericStack(emiStack);
                    if (genericStack != null && genericStack.what() instanceof AEItemKey) {
                        stacks.add(genericStack);
                    }
                }
            }

            result.add(stacks);
        }
        return result;
    }
}