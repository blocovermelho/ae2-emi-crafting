package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.SlotSemantics;
import appeng.menu.me.items.PatternEncodingTermMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Ae2PatternTerminalHandler<T extends PatternEncodingTermMenu> implements StandardRecipeHandler<T> {
    @Override
    public List<Slot> getInputSources(T handler) {
        return List.of();
    }

    @Override
    public boolean canCraft (EmiRecipe recipe, EmiCraftContext<T> context) {
        return true;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        return handler.getSlots(SlotSemantics.CRAFTING_GRID);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return true;
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        T menu = context.getScreenHandler();
        Identifier recipeId = recipe.getId();
        Optional<? extends Recipe<?>> nm_recipe = menu.getPlayer().getWorld().getRecipeManager().get(recipeId);

        // So call me Maybe<T>
        if(nm_recipe.isEmpty()) {
            return false;
        }
        // AE2 Handled Recipes
        List<RecipeSerializer<?>> acceptedSerializers = List.of(
                // Crafting
                RecipeSerializer.SHAPED,
                RecipeSerializer.SHAPELESS,

                RecipeSerializer.STONECUTTING,
                RecipeSerializer.SMITHING_TRANSFORM
        );

        List<List<GenericStack>> items = recipe.getInputs().stream().map(Ae2PatternTerminalHandler::intoGenericStack).toList();

        if (acceptedSerializers.contains(nm_recipe.get().getSerializer())) {
            EncodingHelper.encodeCraftingRecipe(menu, nm_recipe.get(), items, (x) -> true);
        } else {
            // Convert the recipe to a "Processing" recipe.
            List<GenericStack> outputs = recipe.getOutputs().stream()
                    .map(Ae2PatternTerminalHandler::intoGenericStack)
                    .flatMap(Collection::stream)
                    .toList();

            EncodingHelper.encodeProcessingRecipe(menu, items, outputs);
        }

        MinecraftClient.getInstance().setScreen(context.getScreen());

        return true;
    }

    private static List<GenericStack> intoGenericStack(EmiIngredient ingredient) {
        if (ingredient.isEmpty()) {
            return new ArrayList<>();
        }

        return ingredient.getEmiStacks().stream().map((s) ->
            GenericStack.fromItemStack(s.getItemStack())
        ).toList();
    }
}
