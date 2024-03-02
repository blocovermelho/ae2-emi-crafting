package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.api.features.P2PTunnelAttunementInternal;
import appeng.core.AEConfig;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.core.localization.GuiText;
import appeng.core.localization.ItemModText;
import appeng.core.localization.LocalizationEnum;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.text.Text;
import org.blocovermelho.ae2emicrafting.client.recipes.generator.FacadeGenerator;

import java.util.Arrays;
import java.util.List;
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

    public static void registerDescriptions(EmiRegistry registry) {
        addDescription(registry, AEItems.CERTUS_QUARTZ_CRYSTAL, GuiText.CertusQuartzObtain);

        if (AEConfig.instance().isSpawnPressesInMeteoritesEnabled()) {
            addDescription(registry, AEItems.LOGIC_PROCESSOR_PRESS, GuiText.inWorldCraftingPresses);
            addDescription(registry, AEItems.CALCULATION_PROCESSOR_PRESS,
                    GuiText.inWorldCraftingPresses);
            addDescription(registry, AEItems.ENGINEERING_PROCESSOR_PRESS,
                    GuiText.inWorldCraftingPresses);
            addDescription(registry, AEItems.SILICON_PRESS, GuiText.inWorldCraftingPresses);
        }

        addDescription(registry, AEBlocks.CRANK, ItemModText.CRANK_DESCRIPTION);
    }

    private static void addDescription(EmiRegistry registry, ItemDefinition<?> item, LocalizationEnum... lines) {
        var info = new EmiInfoRecipe(
                List.of(EmiStack.of(item)),
                Arrays.stream(lines).<Text>map(LocalizationEnum::text).toList(),
                null);
        registry.addRecipe(info);

    }
}
