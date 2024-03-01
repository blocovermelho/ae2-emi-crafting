package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.api.config.CondenserOutput;
import appeng.api.implementations.items.IStorageComponent;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;

import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.text.Text.translatable;

public class Ae2CondenserRecipe extends VirtualAe2Recipe {
    private static final int PADDING = 7;

    private List<EmiStack> viableStorageComponents = Lists.newArrayList();
    private final CondenserOutput recipe;

    public Ae2CondenserRecipe(CondenserOutput source){
        this.recipe =  source;
        this.prefix = "condenser";
        this.width = 94 + 2 * PADDING;
        this.height = 48 + 2 * PADDING;

        ItemStack stack = switch (source) {
            case TRASH -> ItemStack.EMPTY;
            case MATTER_BALLS -> AEItems.MATTER_BALL.stack();
            case SINGULARITY -> AEItems.SINGULARITY.stack();
        };

        this.output = List.of(EmiStack.of(stack));
        this.viableStorageComponents = getCandidates(source);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Ae2CategoryHolder.CONDENSER;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AppEng.makeId("textures/guis/condenser.png"), PADDING, PADDING, 94, 48, 50, 25);
        widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 2, PADDING + 28, 14, 14, 241, 81);
        widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 16, 16, 240, 240);
        if (recipe == CondenserOutput.MATTER_BALLS) {
            widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 14, 14, 16, 112)
                    .tooltip((mouseX, mouseY) -> List.of(TooltipComponent.of(translatable("gui.tooltips.ae2.MatterBalls", recipe.requiredPower).asOrderedText())));
        } else if (recipe == CondenserOutput.SINGULARITY) {
            widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 14, 14, 32, 112)
                    .tooltip((mouseX, mouseY) -> List.of(TooltipComponent.of(translatable("gui.tooltips.ae2.Singularity", recipe.requiredPower).asOrderedText())));
        }
        widgets.addSlot(output.get(0), PADDING + 54, PADDING + 26).drawBack(false);
        widgets.addSlot(EmiIngredient.of(viableStorageComponents, viableStorageComponents.size()), PADDING + 50, PADDING).drawBack(false);
    }

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
}
