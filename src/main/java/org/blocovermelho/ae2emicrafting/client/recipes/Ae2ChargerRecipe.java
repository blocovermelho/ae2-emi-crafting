package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.blockentity.misc.ChargerBlockEntity;
import appeng.blockentity.misc.CrankBlockEntity;
import appeng.core.definitions.AEBlocks;
import appeng.core.localization.ItemModText;
import appeng.recipes.handlers.ChargerRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.MutableText;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

import java.util.List;

public class Ae2ChargerRecipe extends BasicEmiRecipe {
    private final EmiIngredient ingredient;
    private final EmiStack result;
    public Ae2ChargerRecipe(ChargerRecipe source) {
        super(Ae2CategoryHolder.CHARGER, source.getId(), 130,50);
        this.ingredient = EmiIngredient.of(source.getIngredient());
        this.result = EmiStack.of(source.getResultItem());
    }
    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(ingredient, 30, 7);
        widgets.addSlot(result, 80, 7);
        widgets.addSlot(EmiStack.of(AEBlocks.CRANK), 2, 29)
                .drawBack(false);

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 52, 8);

        var turns = (ChargerBlockEntity.POWER_MAXIMUM_AMOUNT + CrankBlockEntity.POWER_PER_CRANK_TURN - 1)
                / CrankBlockEntity.POWER_PER_CRANK_TURN;
        widgets.addText(
                ItemModText.CHARGER_REQUIRED_POWER.text(turns, ChargerBlockEntity.POWER_MAXIMUM_AMOUNT),
                20, 35,
                0x7E7E7E,
                false);
    }
}
