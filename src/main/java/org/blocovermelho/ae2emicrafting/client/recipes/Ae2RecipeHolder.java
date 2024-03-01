package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.api.features.P2PTunnelAttunementInternal;
import appeng.core.localization.ItemModText;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.blocovermelho.ae2emicrafting.client.recipes.generator.FacadeGenerator;

import java.util.Optional;
import java.util.function.Consumer;

public class Ae2RecipeHolder {
    public static void registerP2PAttunements(Consumer<EmiRecipe> recipeConsumer) {
        var all = EmiApi.getIndexStacks();
        for (var entry : P2PTunnelAttunementInternal.getApiTunnels()) {
            var inputs = all.stream().filter(stack -> entry.stackPredicate().test(stack.getItemStack()))
                    .toList();
            if (inputs.isEmpty()) {
                continue;
            }
            recipeConsumer.accept(
                    new Ae2AttunementRecipe(
                            EmiIngredient.of(inputs),
                            EmiStack.of(entry.tunnelType()),
                            ItemModText.P2P_API_ATTUNEMENT.text().append("\n").append(entry.description())));
        }

        for (var entry : P2PTunnelAttunementInternal.getTagTunnels().entrySet()) {
            var ingredient = EmiIngredient.of(entry.getKey());
            if (ingredient.isEmpty()) {
                continue;
            }
            recipeConsumer.accept(
                    new Ae2AttunementRecipe(
                            ingredient,
                            EmiStack.of(entry.getValue()),
                            ItemModText.P2P_TAG_ATTUNEMENT.text()));
        }
    }

    public static void registerFacades(Consumer<EmiRecipe> recipeConsumer) {
        EmiApi.getIndexStacks().stream()
                .map(FacadeGenerator::of)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(recipeConsumer);
    }
}
