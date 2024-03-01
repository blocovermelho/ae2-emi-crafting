package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.AELog;
import appeng.core.AppEng;
import appeng.core.localization.ItemModText;
import appeng.items.tools.powered.EntropyManipulatorItem;
import appeng.recipes.entropy.EntropyRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import org.blocovermelho.ae2emicrafting.client.recipes.category.Ae2CategoryHolder;
import org.blocovermelho.ae2emicrafting.client.widget.slot.EntropySlot;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Ae2EntropyManipulatorRecipe extends BasicEmiRecipe {
    private static final int BODY_TEXT_COLOR = 0x7E7E7E;
    private final EmiStack inputBlockIngredient;
    private final boolean inputFluidFlowing;
    private final EmiStack outputBlockIngredient;
    private final boolean outputFluidFlowing;
    private final List<EmiStack> additionalDrops;
    private final boolean inputConsumed;
    private final EntropyRecipe recipe;

    public Ae2EntropyManipulatorRecipe(EntropyRecipe source) {
        super(Ae2CategoryHolder.ENTROPY, source.getId(),  130, 50);

        // In-World Block/Fluid input
        var iB = source.getInputBlock();
        var iF = source.getInputFluid();
        inputBlockIngredient = createIngredient(iB, iF);
        inputFluidFlowing = iF != null && isFlowing(iF);

        // In-World Block/Fluid output
        var oB = source.getOutputBlock();
        var oF = source.getOutputFluid();
        outputBlockIngredient = createIngredient(oB, oF);
        outputFluidFlowing = oF != null && isFlowing(oF);


        inputConsumed = oB != null && oB.getDefaultState().isAir()
                && (oF == null || oF == Fluids.EMPTY);
        if (!inputConsumed) {
            inputBlockIngredient.setRemainder(inputBlockIngredient);
        }

        // Additional item drops
        additionalDrops = source.getDrops()
                .stream()
                .map(EmiStack::of)
                .toList();

        inputs.add(inputBlockIngredient);
        outputs.add(outputBlockIngredient);
        outputs.addAll(additionalDrops);

        recipe = source;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var centerX = width / 2;
        var labelText = switch (recipe.getMode()) {
            case HEAT -> ItemModText.ENTROPY_MANIPULATOR_HEAT.text(EntropyManipulatorItem.ENERGY_PER_USE);
            case COOL -> ItemModText.ENTROPY_MANIPULATOR_COOL.text(EntropyManipulatorItem.ENERGY_PER_USE);
        };
        var interaction = switch (recipe.getMode()) {
            case HEAT -> ItemModText.RIGHT_CLICK.text();
            case COOL -> ItemModText.SHIFT_RIGHT_CLICK.text();
        };

        var modeLabel = widgets.addText(labelText, centerX + 4, 2, BODY_TEXT_COLOR, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        var modeLabelX = modeLabel.getBounds().x();
        switch (recipe.getMode()) {
            case HEAT -> widgets.addTexture(AppEng.makeId("textures/guis/jei.png"), modeLabelX - 9, 3, 6, 6, 0, 68);
            case COOL -> widgets.addTexture(AppEng.makeId("textures/guis/jei.png"), modeLabelX - 9, 3, 6, 6, 6, 68);
        }

        widgets.addTexture(EmiTexture.EMPTY_ARROW, centerX - 12, 14);
        widgets.addText(interaction, centerX, 38, BODY_TEXT_COLOR, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        widgets.add(new EntropySlot(inputBlockIngredient, false, inputFluidFlowing, width / 2 - 35, 14));

        int x = centerX + 20;

        // In-World Block or Fluid output
        if (inputConsumed) {
            widgets.add(new EntropySlot(inputBlockIngredient, true, outputFluidFlowing, x - 1, 14));
            x += 18;
        } else if (!outputBlockIngredient.isEmpty()) {
            widgets.add(new EntropySlot(outputBlockIngredient, false, outputFluidFlowing, x - 1, 14)
                    .recipeContext(this));
            x += 18;
        }

        for (var drop : additionalDrops) {
            widgets.addSlot(drop, x - 1, 14).recipeContext(this);
            x += 18;
        }
    }

    private static boolean isFlowing(@NotNull Fluid fluid) {
        return fluid != Fluids.EMPTY && !fluid.isStill(fluid.getDefaultState());
    }
    private static EmiStack createIngredient(Block block, Fluid fluid) {
        if (fluid != null) {
            // We need to tell the player that they need to use the manipulator on the *flowing* variant
            // anyway, so this if-block would be needed in any case.
            if (!fluid.isStill(fluid.getDefaultState())) {
                if (fluid instanceof FlowableFluid flowingFluid) {
                    return EmiStack.of(flowingFluid.getStill());
                } else {
                    // Don't really know how to get the source :-(
                    AELog.warn("Don't know how to get the source fluid for %s", fluid);
                    return EmiStack.of(fluid);
                }
            } else {
                return EmiStack.of(fluid);
            }
        } else if (block != null) {
            return EmiStack.of(block.asItem().getDefaultStack());
        } else {
            return EmiStack.EMPTY;
        }
    }
}
