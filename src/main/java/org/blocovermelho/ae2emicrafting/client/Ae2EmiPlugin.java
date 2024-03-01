package org.blocovermelho.ae2emicrafting.client;

import appeng.api.config.CondenserOutput;
import appeng.api.features.P2PTunnelAttunementInternal;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.blocovermelho.ae2emicrafting.client.handler.*;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseScreenExclusionZones;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseStackProvider;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2PatternTerminalDragHandler;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiFluidStackConverter;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiItemStackConverter;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackConverters;
import org.blocovermelho.ae2emicrafting.client.recipes.*;

import java.util.List;

public class Ae2EmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        EmiStackConverters.register(new EmiItemStackConverter());
        EmiStackConverters.register(new EmiFluidStackConverter());

        registry.addGenericExclusionArea(new Ae2BaseScreenExclusionZones());
//      registry.addGenericStackProvider(new Ae2BaseStackProvider());
        registry.addGenericDragDropHandler(new Ae2PatternTerminalDragHandler());

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEItems.WIRELESS_CRAFTING_TERMINAL.stack()));

        registry.addRecipeHandler(CraftingTermMenu.TYPE, new Ae2RecipeHandler<>());
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>());

        registry.addCategory(Ae2Categories.WORLD_INTERACTION);
        Ae2Categories.addAll(registry, TransformRecipe.TYPE, ItemTransformationRecipe::new);

        registry.addCategory(Ae2Categories.INSCRIBER);
        Ae2Categories.addAll(registry, InscriberRecipe.TYPE, Ae2InscriberRecipe::new);

        registry.addCategory(Ae2Categories.CHARGER);
        Ae2Categories.addAll(registry, ChargerRecipe.TYPE, Ae2ChargerRecipe::new);

        registry.addCategory(Ae2Categories.CONDENSER);
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.MATTER_BALLS));
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.SINGULARITY));

        registry.addCategory(Ae2Categories.ATTUNEMENT);
        for (var entry : P2PTunnelAttunementInternal.getTagTunnels().entrySet()) {
            registry.addRecipe(new Ae2AttunementRecipe(List.of(EmiIngredient.of(entry.getKey())),
                    List.of(EmiStack.of(entry.getValue()))));
        }
    }
}
