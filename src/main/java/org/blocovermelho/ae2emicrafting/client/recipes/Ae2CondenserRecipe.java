package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.api.config.CondenserOutput;
import appeng.api.implementations.items.IStorageComponent;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.core.localization.ButtonToolTips;
import com.google.common.base.Splitter;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static net.minecraft.text.Text.translatable;

public class Ae2CondenserRecipe extends BasicEmiRecipe {
    private final CondenserOutput type;
    private final EmiIngredient viableStorageComponents;
    private final EmiStack output;

    public Ae2CondenserRecipe(CondenserOutput source){
        super(Ae2CategoryHolder.CONDENSER, getRecipeId(source), 94, 48);
        this.type = source;
        this.output = EmiStack.of(getOutput(source));
        this.outputs.add(this.output);
        this.viableStorageComponents = EmiIngredient.of(getCandidates(source));
        this.catalysts.add(this.viableStorageComponents);

    }
    @Override
    public void addWidgets(WidgetHolder widgets) {

        var background = AppEng.makeId("textures/guis/condenser.png");
        widgets.addTexture(background, 0, 0, 94, 48, 50, 25);

        var statesLocation = AppEng.makeId("textures/guis/states.png");
        widgets.addTexture(statesLocation, 2, 28, 14, 14, 241, 81);
        widgets.addTexture(statesLocation, 78, 28, 16, 16, 240, 240);

        widgets.addAnimatedTexture(background, 70, 0, 6, 18, 178, 25,
                2000, false, true, false);

        if (type == CondenserOutput.MATTER_BALLS) {
            widgets.addTexture(statesLocation, 78, 28, 14, 14, 16, 112);
        } else if (type == CondenserOutput.SINGULARITY) {
            widgets.addTexture(statesLocation, 78, 28, 14, 14, 32, 112);
        }
        widgets.addTooltipText(getTooltip(type), 78, 28, 16, 16);

        widgets.addSlot(output, 54, 26).drawBack(false);
        widgets.addSlot(viableStorageComponents, 50, 0).drawBack(false);   }

    private static List<EmiStack> getCandidates(CondenserOutput kind) {
        return Stream.of(
                AEItems.CELL_COMPONENT_1K,
                AEItems.CELL_COMPONENT_4K,
                AEItems.CELL_COMPONENT_16K,
                AEItems.CELL_COMPONENT_64K,
                AEItems.CELL_COMPONENT_256K
        ).map(ItemDefinition::stack).filter(x -> isViableFor(x, kind)).map(EmiStack::of).toList();
    }
    private static boolean isViableFor(ItemStack stack, CondenserOutput kind) {
        IStorageComponent component = (IStorageComponent) stack.getItem();
        int storageBytes = component.getBytes(stack) * CondenserBlockEntity.BYTE_MULTIPLIER;
        return storageBytes >= kind.requiredPower;
    }

    private static Identifier getRecipeId(CondenserOutput type) {
        return AppEng.makeId(type.name().toLowerCase(Locale.ROOT));
    }
    private static ItemStack getOutput(CondenserOutput recipe) {
        return switch (recipe) {
            case MATTER_BALLS -> AEItems.MATTER_BALL.stack();
            case SINGULARITY -> AEItems.SINGULARITY.stack();
            default -> ItemStack.EMPTY;
        };
    }

    private List<Text> getTooltip(CondenserOutput type) {
        String key;
        switch (type) {
            case MATTER_BALLS:
                key = ButtonToolTips.MatterBalls.getTranslationKey();
                break;
            case SINGULARITY:
                key = ButtonToolTips.Singularity.getTranslationKey();
                break;
            default:
                return Collections.emptyList();

    }

    return Splitter.on("\n")
            .splitToList(Text.translatable(key, type.requiredPower).getString())
            .stream()
            .<Text>map(Text::literal)
            .toList();
    }
}
