package org.blocovermelho.ae2emicrafting.client.recipes.category;

import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.localization.LocalizationEnum;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.blocovermelho.ae2emicrafting.client.recipes.locale.TranslationKeys;

import java.util.List;
import java.util.function.Function;

public class Ae2CategoryHolder {
    public static Ae2RecipeCategory WORLD_INTERACTION = into(
            TransformRecipe.TYPE_ID,
            EmiStack.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED),
            TranslationKeys.CATEGORY_TRANSFORM
    );
    public static Ae2RecipeCategory INSCRIBER = into(
            InscriberRecipe.TYPE_ID,
            EmiStack.of(AEBlocks.INSCRIBER),
            TranslationKeys.CATEGORY_INSCRIBER
    );
    public static Ae2RecipeCategory CHARGER = into(
            ChargerRecipe.TYPE_ID,
            EmiStack.of(AEBlocks.CHARGER),
            TranslationKeys.CATEGORY_CHARGER
    );
    public static Ae2RecipeCategory ATTUNEMENT = unsafeInto(
            "attunement",
            EmiStack.of(AEParts.ME_P2P_TUNNEL),
            TranslationKeys.CATEGORY_ATTUNEMENT
    );
    public static Ae2RecipeCategory CONDENSER = unsafeInto(
            "condenser",
            EmiStack.of(AEBlocks.CONDENSER),
            TranslationKeys.CATEGORY_CONDENSER
    );
    private static Ae2RecipeCategory into(Identifier identifier, EmiRenderable icon, LocalizationEnum name) {
        return new Ae2RecipeCategory(identifier, icon, name);
    }

    private static Ae2RecipeCategory unsafeInto(String key, EmiRenderable icon, LocalizationEnum name) {
        return into(new Identifier(AppEng.MOD_ID, key), icon, name);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> void addAll(EmiRegistry registry, RecipeType type, Function<T, EmiRecipe> constructor) {
        for (T recipe : (List<T>) registry.getRecipeManager().listAllOfType(type)) {
            registry.addRecipe(constructor.apply(recipe));
        }
    }
}
