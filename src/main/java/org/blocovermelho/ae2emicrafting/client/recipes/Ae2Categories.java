package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class Ae2Categories {
    public static EmiRecipeCategory WORLD_INTERACTION = into(TransformRecipe.TYPE_ID, EmiStack.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED));
    public static EmiRecipeCategory INSCRIBER = into(InscriberRecipe.TYPE_ID, EmiStack.of(AEBlocks.INSCRIBER));
    public static EmiRecipeCategory CHARGER = into(ChargerRecipe.TYPE_ID, EmiStack.of(AEBlocks.CHARGER));
    public static EmiRecipeCategory ATTUNEMENT = unsafeInto("attunement", EmiStack.of(AEParts.ME_P2P_TUNNEL));
    public static EmiRecipeCategory CONDENSER = unsafeInto("condenser", EmiStack.of(AEBlocks.CONDENSER));
    private static EmiRecipeCategory into(Identifier identifier, EmiRenderable icon) {
        return new EmiRecipeCategory(identifier, icon);
    }

    private static EmiRecipeCategory unsafeInto(String key, EmiRenderable icon) {
        return into(new Identifier(AppEng.MOD_ID, key), icon);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> void addAll(EmiRegistry registry, RecipeType type, Function<T, EmiRecipe> constructor) {
        for (T recipe : (List<T>) registry.getRecipeManager().listAllOfType(type)) {
            registry.addRecipe(constructor.apply(recipe));
        }
    }
}
