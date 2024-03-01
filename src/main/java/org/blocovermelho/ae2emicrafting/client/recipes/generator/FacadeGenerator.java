package org.blocovermelho.ae2emicrafting.client.recipes.generator;

import appeng.core.definitions.AEParts;
import appeng.items.parts.FacadeItem;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class FacadeGenerator {
    private static final ItemStack cableAnchor;
    public static Optional<EmiRecipe> of(EmiStack emiStack) {
        var itemStack = emiStack.getItemStack();
        if (itemStack.isEmpty()) {
            return Optional.empty(); // We only have items
        }

        // Looking up how a certain facade is crafted
        if (itemStack.getItem() instanceof FacadeItem facadeItem) {
            ItemStack textureItem = facadeItem.getTextureItem(itemStack);
            return Optional.of(make(textureItem, itemStack.copy()));
        }

        return Optional.empty();
    }

    private static EmiRecipe make(ItemStack textureItem, ItemStack result) {
        var input = List.<EmiIngredient>of(
                EmiStack.EMPTY,
                EmiStack.of(cableAnchor),
                EmiStack.EMPTY,
                EmiStack.of(cableAnchor),
                EmiStack.of(textureItem),
                EmiStack.of(cableAnchor),
                EmiStack.EMPTY,
                EmiStack.of(cableAnchor),
                EmiStack.EMPTY);

        return new EmiCraftingRecipe(
                input,
                EmiStack.of(result, 4),
                null,
                false);
    }

    static {
        cableAnchor = AEParts.CABLE_ANCHOR.stack();
    }
}
