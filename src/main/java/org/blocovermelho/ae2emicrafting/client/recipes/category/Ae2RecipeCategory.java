package org.blocovermelho.ae2emicrafting.client.recipes.category;

import appeng.core.localization.LocalizationEnum;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Ae2RecipeCategory extends EmiRecipeCategory {
    private final Text name;
    public Ae2RecipeCategory(Identifier id, EmiRenderable icon, LocalizationEnum locale) {
        super(id, icon);
        this.name = locale.text();
    }

    @Override
    public Text getName() {
        return name;
    }
}
