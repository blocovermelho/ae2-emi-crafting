package org.blocovermelho.ae2emicrafting.client;

import appeng.client.gui.AEBaseScreen;
import appeng.core.definitions.AEParts;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.CraftingTermMenu;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2RecipeHandler;

public class Ae2EmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addRecipeHandler(CraftingTermMenu.TYPE, new Ae2RecipeHandler<>());

        registry.addGenericExclusionArea(((screen, consumer) -> {
            if (screen instanceof AEBaseScreen<? extends AEBaseMenu> baseScreen) {
                baseScreen
                        .getExclusionZones()
                        .stream()
                        .map(ez -> new Bounds(ez.getX(), ez.getY(), ez.getWidth(), ez.getHeight()))
                        .forEach(consumer);
            }
        }));
    }
}
