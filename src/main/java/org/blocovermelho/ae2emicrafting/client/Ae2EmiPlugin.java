package org.blocovermelho.ae2emicrafting.client;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.core.definitions.AEParts;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2PatternTerminalDragHandler;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2PatternTerminalHandler;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2RecipeHandler;
import org.blocovermelho.ae2emicrafting.client.recipes.Ae2Categories;
import org.blocovermelho.ae2emicrafting.client.recipes.Ae2ChargerRecipe;
import org.blocovermelho.ae2emicrafting.client.recipes.Ae2InscriberRecipe;
import org.blocovermelho.ae2emicrafting.client.recipes.ItemTransformationRecipe;

public class Ae2EmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addRecipeHandler(CraftingTermMenu.TYPE, new Ae2RecipeHandler<>());
        registry.addDragDropHandler(PatternEncodingTermScreen.class, new Ae2PatternTerminalDragHandler<>());
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>());

        registry.addGenericExclusionArea(((screen, consumer) -> {
            if (screen instanceof AEBaseScreen<? extends AEBaseMenu> baseScreen) {
                baseScreen
                        .getExclusionZones()
                        .stream()
                        .map(ez -> new Bounds(ez.getX(), ez.getY(), ez.getWidth(), ez.getHeight()))
                        .forEach(consumer);
            }
        }));

        registry.addCategory(Ae2Categories.WORLD_INTERACTION);
        Ae2Categories.addAll(registry, TransformRecipe.TYPE, ItemTransformationRecipe::new);

        registry.addCategory(Ae2Categories.INSCRIBER);
        Ae2Categories.addAll(registry, InscriberRecipe.TYPE, Ae2InscriberRecipe::new);

        registry.addCategory(Ae2Categories.CHARGER);
        Ae2Categories.addAll(registry, ChargerRecipe.TYPE, Ae2ChargerRecipe::new);
    }
}
