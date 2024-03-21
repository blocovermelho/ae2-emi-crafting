package org.blocovermelho.ae2emicrafting.client.helper.rendering;

import appeng.api.stacks.AEKey;
import appeng.integration.modules.jeirei.TransferHelper;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.CraftingTermMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static org.blocovermelho.ae2emicrafting.client.helper.rendering.Rendering.*;
import static appeng.integration.modules.jeirei.TransferHelper.BLUE_SLOT_HIGHLIGHT_COLOR;

public sealed abstract class Result {
    /**
     * @return null doesn't override the default tooltip.
     */
    @Nullable
    public List<Text> getTooltip(EmiRecipe recipe, EmiCraftContext<?> context) {
        return null;
    }

    public abstract boolean canCraft();

    public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                       MatrixStack matrices) {
    }

    public static final class Success extends Result {
        @Override
        public boolean canCraft() {
            return true;
        }
    }

    /**
     * There are missing ingredients, but at least one is present.
     */
    public static final class PartiallyCraftable extends Result {
        private final CraftingTermMenu.MissingIngredientSlots missingSlots;

        public PartiallyCraftable(CraftingTermMenu.MissingIngredientSlots missingSlots) {
            this.missingSlots = missingSlots;
        }

        @Override
        public boolean canCraft() {
            return true;
        }

        @Override
        public List<Text> getTooltip(EmiRecipe recipe, EmiCraftContext<?> context) {
            // EMI caches this tooltip, we cannot dynamically react to control being held here
            return TransferHelper.createCraftingTooltip(missingSlots, false);
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           MatrixStack matrixStack) {
            renderMissingAndCraftableSlotOverlays(widgets, matrixStack, missingSlots.missingSlots(),
                    missingSlots.craftableSlots());
        }
    }

    /**
     * Indicates that some of the slots can already be crafted by the auto-crafting system.
     */
    public static final class EncodeWithCraftables extends Result {
        private final Set<AEKey> craftableKeys;

        /**
         * @param craftableKeys All keys that the current system can auto-craft.
         */
        public EncodeWithCraftables(Set<AEKey> craftableKeys) {
            this.craftableKeys = craftableKeys;
        }

        @Override
        public boolean canCraft() {
            return true;
        }

        @Override
        public List<Text> getTooltip(EmiRecipe emiRecipe, EmiCraftContext<?> context) {
            var anyCraftable = emiRecipe.getInputs().stream()
                    .anyMatch(ing -> isCraftable(craftableKeys, ing));
            if (anyCraftable) {
                return TransferHelper.createEncodingTooltip(true);
            }
            return null;
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           MatrixStack matrixStack) {
            for (var widget : widgets) {
                if (widget instanceof SlotWidget slot && isInputSlot(slot)) {
                    if (isCraftable(craftableKeys, slot.getStack())) {
                        matrixStack.push();
                        matrixStack.translate(0, 0, 400);

                        var bounds = getInnerBounds(slot);

                        DrawableHelper.fill(
                                matrixStack,
                                bounds.x(),
                                bounds.y(),
                                bounds.right(),
                                bounds.bottom(),
                                BLUE_SLOT_HIGHLIGHT_COLOR);

                        matrixStack.pop();
                    }
                }
            }
        }

        private static boolean isCraftable(Set<AEKey> craftableKeys, EmiIngredient ingredient) {
            return ingredient.getEmiStacks().stream().anyMatch(emiIngredient -> {
                var stack = EmiStackHelper.toGenericStack(emiIngredient);
                return stack != null && craftableKeys.contains(stack.what());
            });
        }
    }

    static final class NotApplicable extends Result {
        @Override
        public boolean canCraft() {
            return false;
        }
    }

    static final class Error extends Result {
        private final Text message;
        private final Set<Integer> missingSlots;

        public Error(Text message, Set<Integer> missingSlots) {
            this.message = message;
            this.missingSlots = missingSlots;
        }

        public Text getMessage() {
            return message;
        }

        @Override
        public boolean canCraft() {
            return false;
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           MatrixStack matrixStack) {
            renderMissingAndCraftableSlotOverlays(widgets, matrixStack, missingSlots, Set.of());
        }

        @Override
        public List<Text> getTooltip(EmiRecipe emiRecipe, EmiCraftContext<?> context) {
            return List.of(getMessage());
        }
    }

    public static NotApplicable createNotApplicable() {
        return new NotApplicable();
    }

    public static Success createSuccessful() {
        return new Success();
    }

    public static Error createFailed(Text text) {
        return new Error(text, Set.of());
    }

    public static Error createFailed(Text text, Set<Integer> missingSlots) {
        return new Error(text, missingSlots);
    }
}
