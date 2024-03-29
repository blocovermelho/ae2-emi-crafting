package org.blocovermelho.ae2emicrafting.client;

import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wet.WETMenu;
import de.mari_023.ae2wtlib.wut.WUTHandler;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2CraftingHandler;
import org.blocovermelho.ae2emicrafting.client.handler.Ae2PatternTerminalHandler;

public class Ae2WtEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(WUTHandler.wirelessTerminals.get("crafting").universalTerminal()));
        registry.addRecipeHandler(WCTMenu.TYPE, new Ae2CraftingHandler<>(WCTMenu.class));
        registry.addRecipeHandler(WETMenu.TYPE, new Ae2PatternTerminalHandler<>(WETMenu.class));
    }
}
