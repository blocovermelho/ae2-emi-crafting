package org.blocovermelho.ae2emicrafting.client.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseAe2Recipe implements EmiRecipe {
    protected Identifier id;
    protected List<EmiIngredient> input = Lists.newArrayList();
    protected List<EmiStack> output = Lists.newArrayList();
    protected List<EmiIngredient> catalysts = Lists.newArrayList();

    protected int width, height;
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
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }
}
