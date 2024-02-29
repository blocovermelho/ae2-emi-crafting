package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.definitions.AEBlocks;
import appeng.core.localization.ItemModText;
import appeng.recipes.handlers.ChargerRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.MutableText;

import java.util.List;

public class Ae2ChargerRecipe extends BaseAe2Recipe {
    public Ae2ChargerRecipe(ChargerRecipe source) {
        this.id = source.getId();

        this.input = source.getIngredients()
                .stream()
                .filter(x -> !x.isEmpty())
                .map(EmiIngredient::of)
                .toList();

        this.output = List.of(EmiStack.of(source.getResultItem()));

        this.width = 130;
        this.height = 50;
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return Ae2Categories.CHARGER;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input.get(0), 31, 8);
        widgets.addSlot(output.get(0), 81, 8).recipeContext(this);

        widgets.addSlot(EmiStack.of(AEBlocks.CRANK), 3, 30).drawBack(false);

        widgets.addFillingArrow(52, 8, 1);

        int turns = 10;
        MutableText text = ItemModText.CHARGER_REQUIRED_POWER.text(turns, 1600);

        widgets.addText(text, 20, 35, 0x7E7E7E, false);
    }
}
