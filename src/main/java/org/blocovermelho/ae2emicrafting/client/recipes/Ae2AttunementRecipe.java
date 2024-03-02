package org.blocovermelho.ae2emicrafting.client.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

import java.util.List;

public class Ae2AttunementRecipe extends BasicEmiRecipe {
    private final EmiIngredient input;
    private final EmiStack p2pTunnel;
    private final Text description;

    public Ae2AttunementRecipe(EmiIngredient input, EmiStack p2pTunnel, Text description) {
        super(Ae2CategoryHolder.ATTUNEMENT, null, 150, 36);
        this.input = input;
        this.p2pTunnel = p2pTunnel;
        this.description = description;

        this.inputs.add(input);
        this.outputs.add(p2pTunnel);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var originX = width / 2 - 41;
        var originY = height / 2 - 13;

        widgets.addSlot(input, originX + 3, originY + 4)
                .appendTooltip(description);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, originX + 27, originY + 4);
        widgets.addSlot(p2pTunnel, originX + 60, originY + 4);
    }
}
