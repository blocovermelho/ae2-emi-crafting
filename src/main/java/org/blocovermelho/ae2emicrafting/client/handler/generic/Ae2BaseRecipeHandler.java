package org.blocovermelho.ae2emicrafting.client.handler.generic;

import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.CraftingTermMenu;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.blocovermelho.ae2emicrafting.client.Ae2EmiMod;
import org.blocovermelho.ae2emicrafting.client.helper.InventoryUtils;
import org.blocovermelho.ae2emicrafting.client.helper.rendering.Result;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Ae2BaseRecipeHandler<T extends AEBaseMenu> implements EmiRecipeHandler<T> {
    public static final int CRAFTING_GRID_WIDTH = 3;
    public static final int CRAFTING_GRID_HEIGHT = 3;

    private final Class<T> containerClass;
    protected Ae2BaseRecipeHandler(Class<T> containerClass) {
        this.containerClass = containerClass;
    }

    @Override
    public EmiPlayerInventory getInventory(HandledScreen<T> screen) {

        if (!Ae2EmiMod.cfg.bomsync) {
            return new EmiPlayerInventory(List.of());
        }

        T handler = screen.getScreenHandler();
        if (handler instanceof MEStorageMenu menu) {
            DefaultedList<EmiStack> allStack = DefaultedList.of();

            List<EmiStack> meSystem = InventoryUtils.getExistingStacks(menu);
            allStack.addAll(meSystem);

            List<EmiStack> hotbar = InventoryUtils.getStacks(screen, SlotSemantics.PLAYER_HOTBAR);
            allStack.addAll(hotbar);

            List<EmiStack> inventory = InventoryUtils.getStacks(screen, SlotSemantics.PLAYER_INVENTORY);
            allStack.addAll(inventory);

            if (menu instanceof CraftingTermMenu) {
                List<EmiStack> craft = InventoryUtils.getStacks(screen, SlotSemantics.CRAFTING_GRID);
                allStack.addAll(craft);
            }

            return new EmiPlayerInventory(allStack);
        } else {
            return EmiPlayerInventory.of(handler.getPlayer());
        }
    }

    protected abstract Result transferRecipe(T menu,
                                             @Nullable Recipe<?> recipe,
                                             EmiRecipe emiRecipe,
                                             boolean doTransfer);

    protected final Result transferRecipe(EmiRecipe emiRecipe, EmiCraftContext<T> context, boolean doTransfer) {
        if (!containerClass.isInstance(context.getScreenHandler())) {
            return Result.createNotApplicable();
        }

        var recipe = context.getScreenHandler()
                .getPlayer()
                .getWorld()
                .getRecipeManager()
                .get(emiRecipe.getId())
                .orElse(null);


        T menu = containerClass.cast(context.getScreenHandler());

        var result = transferRecipe(menu, recipe, emiRecipe, doTransfer);
        if (result instanceof Result.Success && doTransfer) {
            MinecraftClient.getInstance().setScreen(context.getScreen());
        }
        return result;
    }


    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return true;
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<T> context) {
        if (Ae2EmiMod.cfg.alwayssync || context.getType() == EmiCraftContext.Type.FILL_BUTTON) {
            return transferRecipe(recipe, context, false).canCraft();
        } else {
            return context.getInventory().canCraft(recipe);
        }
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        return transferRecipe(recipe, context, true).canCraft();
    }


    @Override
    public List<TooltipComponent> getTooltip(EmiRecipe recipe, EmiCraftContext<T> context) {
        var tooltip = transferRecipe(recipe, context, false).getTooltip(recipe, context);
        if (tooltip != null) {
            return tooltip.stream()
                    .map(Text::asOrderedText)
                    .map(TooltipComponent::of)
                    .toList();
        } else {
            return EmiRecipeHandler.super.getTooltip(recipe, context);
        }
    }

    @Override
    public void render(EmiRecipe recipe, EmiCraftContext<T> context, List<Widget> widgets, DrawContext draw) {
        if (context.getType() == EmiCraftContext.Type.FILL_BUTTON) {
            transferRecipe(recipe, context, false).render(recipe, context, widgets, draw);
        } else {
            EmiRecipeHandler.super.render(recipe, context,  widgets,  draw);
        }
    }
}
