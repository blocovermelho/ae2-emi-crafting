package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.definitions.AEBlocks;
import appeng.core.localization.ItemModText;
import appeng.recipes.transform.TransformCircumstance;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTransformationRecipe extends BaseAe2Recipe {
    protected TransformRecipe recipe;

    public ItemTransformationRecipe(TransformRecipe source) {
        this.recipe = source;

        this.id = source.getId();

        this.input = source.getIngredients()
                .stream()
                .filter(x -> !x.isEmpty())
                .map(EmiIngredient::of)
                .toList();

        this.output = List.of(EmiStack.of(source.getResultItem()));

        this.width = 150;
        this.height = 72;
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return Ae2Categories.WORLD_INTERACTION;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        TransformCircumstance circumstance = recipe.circumstance;
        ItemModText circText = circumstance.isExplosion() ? ItemModText.EXPLOSION : ItemModText.SUBMERGE_IN;

        int y = 10;
        int col1 = 10;

        int size = input.size();
        if (size < 3) {
            y += 9 * (3 - size);
        }

        int yOffset = 28;
        final int col2 = col1 + 25;

        for (EmiIngredient i : input) {
            var slot = widgets.addSlot(i, col1, y);
            y += slot.getBounds().height();
        }

        var arrow1 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col2, yOffset);
        final int col3 = col2 + arrow1.getBounds().width() + 6;

        widgets.addText(circText.text(), col3, yOffset-15, 8289918, false);

        if (circumstance.isFluid()) {
            widgets.addSlot(EmiIngredient.of(List.of(EmiStack.of(Fluids.WATER))), col3, yOffset);
        } else {
            widgets.addSlot(EmiIngredient.of(List.of(EmiStack.of(Items.TNT), EmiStack.of(AEBlocks.TINY_TNT))), col3, yOffset);
        }

        final int col4 = col3 + 16 + 5;
        var arrow2 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col4, yOffset);

        final int col5 = arrow2.getBounds().x() + arrow2.getBounds().width() + 10;
        widgets.addSlot(output.get(0), col5, yOffset);

    }
}
