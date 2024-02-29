package org.blocovermelho.ae2emicrafting.client.recipes;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import java.util.List;

public class Ae2AttunementRecipe extends VirtualAe2Recipe {
    public Ae2AttunementRecipe(List<EmiIngredient> input, List<EmiStack> output) {
        this.prefix = "attunement";
        this.input = input;
        this.output = output;

        this.width = 150;
        this.height = 36;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Ae2Categories.ATTUNEMENT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, width / 2 - 41 + 27, height / 2 - 13 + 4);
        widgets.addSlot(EmiIngredient.of(input, input.size()), width / 2 - 41 + 4, height / 2 - 13 + 4);
        widgets.addSlot(output.get(0), width / 2 - 41 + 56, height / 2 - 13 + 4);
    }
}
