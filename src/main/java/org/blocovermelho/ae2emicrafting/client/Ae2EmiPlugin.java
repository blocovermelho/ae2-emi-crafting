package org.blocovermelho.ae2emicrafting.client;

import appeng.api.config.CondenserOutput;
import appeng.core.AEConfig;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.me.items.WirelessCraftingTermMenu;
import appeng.recipes.entropy.EntropyRecipe;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2CraftingHandler;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2MeTerminalHandler;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2PatternTerminalHandler;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseScreenExclusionZones;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseDragHandler;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiFluidStackConverter;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiItemStackConverter;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackConverters;
import org.blocovermelho.ae2emicrafting.client.recipes.*;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

public class Ae2EmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        EmiStackConverters.register(new EmiItemStackConverter());
        EmiStackConverters.register(new EmiFluidStackConverter());

        registry.addGenericExclusionArea(new Ae2BaseScreenExclusionZones());
//      registry.addGenericStackProvider(new Ae2BaseStackProvider());
        registry.addGenericDragDropHandler(new Ae2BaseDragHandler());

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEItems.WIRELESS_CRAFTING_TERMINAL.stack()));

        registry.addRecipeHandler(CraftingTermMenu.TYPE, new Ae2CraftingHandler<>(CraftingTermMenu.class));
        registry.addRecipeHandler(WirelessCraftingTermMenu.TYPE, new Ae2CraftingHandler<>(WirelessCraftingTermMenu.class));
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>(PatternEncodingTermMenu.class));
        // Workaround: Seeing Items from ME Terminal on Synthetic Favourites without using the GenericStackProvider.
        // Reasoning: For whatever reason that is broken on fluids, even though it shouldn't.
        registry.addRecipeHandler(MEStorageMenu.TYPE, new Ae2MeTerminalHandler<>(MEStorageMenu.class));

        registry.addCategory(Ae2CategoryHolder.WORLD_INTERACTION);
        Ae2CategoryHolder.addAll(registry, TransformRecipe.TYPE, Ae2TransformRecipe::new);

        registry.addCategory(Ae2CategoryHolder.INSCRIBER);
        registry.addWorkstation(Ae2CategoryHolder.INSCRIBER, EmiStack.of(AEBlocks.INSCRIBER));
        Ae2CategoryHolder.addAll(registry, InscriberRecipe.TYPE, Ae2InscriberRecipe::new);

        registry.addCategory(Ae2CategoryHolder.CHARGER);
        registry.addWorkstation(Ae2CategoryHolder.CHARGER, EmiStack.of(AEBlocks.CHARGER));
        registry.addWorkstation(Ae2CategoryHolder.CHARGER, EmiStack.of(AEBlocks.CRANK));
        Ae2CategoryHolder.addAll(registry, ChargerRecipe.TYPE, Ae2ChargerRecipe::new);

        registry.addCategory(Ae2CategoryHolder.CONDENSER);
        registry.addWorkstation(Ae2CategoryHolder.CONDENSER, EmiStack.of(AEBlocks.CONDENSER));
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.MATTER_BALLS));
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.SINGULARITY));

        registry.addCategory(Ae2CategoryHolder.ENTROPY);
        registry.addWorkstation(Ae2CategoryHolder.ENTROPY, EmiStack.of(AEItems.ENTROPY_MANIPULATOR));
        Ae2CategoryHolder.addAll(registry, EntropyRecipe.TYPE, Ae2EntropyManipulatorRecipe::new);

        registry.addCategory(Ae2CategoryHolder.ATTUNEMENT);
        registry.addDeferredRecipes(Ae2RecipeHolder::registerP2PAttunements);

        if (AEConfig.instance().isEnableFacadeRecipesInJEI()) {
            registry.addDeferredRecipes(Ae2RecipeHolder::registerFacades);
        }

        Ae2RecipeHolder.registerDescriptions(registry);
    }
}
