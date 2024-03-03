package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.menu.me.common.MEStorageMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler;
import org.blocovermelho.ae2emicrafting.client.helper.rendering.Result;
import org.jetbrains.annotations.Nullable;

public class Ae2MeTerminalHandler<T extends MEStorageMenu> extends Ae2BaseRecipeHandler<T> {
    public Ae2MeTerminalHandler(Class<T> containerClass) {
        super(containerClass);
    }

    @Override
    protected Result transferRecipe(T menu, @Nullable Recipe<?> recipe, EmiRecipe emiRecipe, boolean doTransfer) {
        return Result.createFailed(Text.translatable("ae2-emi-crafting.error.crafting.storage_terminal"));
    }
}
