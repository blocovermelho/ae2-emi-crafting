package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

import java.util.List;

public class Ae2InscriberRecipe extends BasicEmiRecipe {
    protected EmiIngredient top;
    protected EmiIngredient middle;
    protected EmiIngredient bottom;
    public Ae2InscriberRecipe(InscriberRecipe source) {
        super(Ae2CategoryHolder.INSCRIBER, source.getId(), 97, 64);
        this.id = source.getId();

        this.top = EmiIngredient.of(source.getTopOptional());
        this.middle = EmiIngredient.of(source.getMiddleInput());
        this.bottom = EmiIngredient.of(source.getBottomOptional());

        if(!top.isEmpty() && source.getProcessType() == InscriberProcessType.INSCRIBE) {
            top.getEmiStacks().forEach(s -> s.setRemainder(s));
        }

        if (!bottom.isEmpty() && source.getProcessType() == InscriberProcessType.INSCRIBE) {
            bottom.getEmiStacks().forEach(s -> s.setRemainder(s));
        }

        this.inputs = List.of(top, middle, bottom);
        this.outputs = List.of(EmiStack.of(source.getResultItem()));
    }
    @Override
    public void addWidgets(WidgetHolder widgets) {
        Identifier background = AppEng.makeId("textures/guis/inscriber.png");

        widgets.addTexture(background, 0, 0, 97, 64, 44, 15);

        widgets.addAnimatedTexture(background, 91, 24, 6, 18, 135, 177,
                2000, false, true, false);

        widgets.addSlot(top, 0, 0)
                .drawBack(false);
        widgets.addSlot(middle, 18, 23)
                .drawBack(false);
        widgets.addSlot(bottom, 0, 46)
                .drawBack(false);
        widgets.addSlot(outputs.get(0), 68, 24)
                .drawBack(false);
    }
}
