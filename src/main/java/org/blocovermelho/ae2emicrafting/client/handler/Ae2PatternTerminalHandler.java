package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.SlotSemantics;
import appeng.menu.me.items.PatternEncodingTermMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ae2PatternTerminalHandler<T extends PatternEncodingTermMenu> implements StandardRecipeHandler<T> {
    List<Slot> CreativeInputSource = Registries.ITEM.stream().map((s) -> {
        ItemStack is = new ItemStack(s);
        is.setCount(64);

        return new Slot(new SimpleInventory(is), 0, 0, 0);
    }).toList();

    //TODO: Handle Fluids
//   List<Slot> CreativeFluidInputSource = Registries.FLUID.stream().map((s) -> {
//
//   })

    @Override
    public List<Slot> getInputSources(T handler) {
        return CreativeInputSource;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        return handler.getSlots(SlotSemantics.CRAFTING_GRID);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        // EncodingHelper.isSupportedCraftingRecipe();

        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING ||
        recipe.getCategory() == VanillaEmiRecipeCategories.SMITHING ||
        recipe.getCategory() == VanillaEmiRecipeCategories.STONECUTTING;

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

        List<List<GenericStack>> items = recipe.getInputs().stream().map(Ae2PatternTerminalHandler::intoGenericStack).toList();

        EncodingHelper.encodeCraftingRecipe(menu, nm_recipe.get(), items, (x) -> true);

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
