package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Ae2InscriberRecipe implements EmiRecipe {
    protected Identifier id;

    protected List<EmiIngredient> input;
    protected EmiIngredient top;
    protected EmiIngredient middle;
    protected EmiIngredient bottom;

    protected final List<EmiIngredient> catalysts;
    protected List<EmiStack> output;
    protected int width, height;
    public Ae2InscriberRecipe(InscriberRecipe source) {
        this.id = source.getId();

        this.top = EmiIngredient.of(source.getTopOptional());
        this.middle = EmiIngredient.of(source.getMiddleInput());
        this.bottom = EmiIngredient.of(source.getBottomOptional());

        List<Identifier> validCatalysts = List.of(
                AEItems.LOGIC_PROCESSOR_PRESS.id(),
                AEItems.CALCULATION_PROCESSOR_PRESS.id(),
                AEItems.ENGINEERING_PROCESSOR_PRESS.id(),
                AEItems.SILICON_PRESS.id()
        );

        EmiStack catalystStack = top.getEmiStacks().get(0);
        if(!top.isEmpty() && validCatalysts.contains(catalystStack.getId())) {
            this.catalysts = List.of(catalystStack);
            this.input = List.of(middle, bottom);
        } else {
            this.catalysts = List.of();
            this.input = List.of(top, middle, bottom);
        }

        this.width = 97;
        this.height = 64;

        this.output = List.of(EmiStack.of(source.getResultItem()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Ae2Categories.INSCRIBER;
    }
    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AppEng.makeId("textures/guis/inscriber.png"), 0, 0, 97, 64, 44, 15);
        widgets.addSlot(top, 0, 0).drawBack(false);
        widgets.addSlot(middle, 18, 23).drawBack(false);
        widgets.addSlot(bottom, 0, 46).drawBack(false);
        widgets.addSlot(EmiIngredient.of(output), 68, 24).drawBack(false).recipeContext(this);
    }
}
