package org.blocovermelho.ae2emicrafting.client.widget.slot;

import appeng.api.client.AEKeyRendering;
import appeng.api.stacks.AEFluidKey;
import appeng.core.localization.ItemModText;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EntropySlot extends SlotWidget {
    private final boolean consumed;
    private final boolean flowing;
    public EntropySlot(EmiIngredient stack, boolean consumed, boolean flowing, int x, int y) {
        super(stack, x, y);
        this.consumed = consumed;
        this.flowing = flowing;
    }

    @Override
    public List<TooltipComponent> getTooltip(int mouseX, int mouseY) {
        var tooltip = super.getTooltip(mouseX, mouseY);

        var fluid = ((EmiStack) stack).getKeyOfType(Fluid.class);
        if (fluid != null) {
            // We use our own tooltip composition here since it's hard to customize client tooltip components
            tooltip.clear();

            var fluidTooltip = AEKeyRendering.getTooltip(AEFluidKey.of(fluid));
            // Prepend "Flowing" to the first line if we're dealing with a non-source block
            if (!fluidTooltip.isEmpty() && flowing) {
                fluidTooltip.set(
                        0,
                        ItemModText.FLOWING_FLUID_NAME.text(fluidTooltip.get(0)));
            }

            fluidTooltip.stream()
                    .map(Text::asOrderedText)
                    .map(TooltipComponent::of)
                    .forEach(tooltip::add);
            addSlotTooltip(tooltip);
        }

        if (consumed) {
            var text = ItemModText.CONSUMED.text().formatted(Formatting.RED, Formatting.BOLD);
            tooltip.add(TooltipComponent.of(text.asOrderedText()));
        }

        return tooltip;
    }

    @Override
    public void drawOverlay(DrawContext draw, int mouseX, int mouseY, float delta) {
        if (consumed) {
            var bounds = getBounds();
            draw.fill(bounds.x(), bounds.y(), bounds.x() + bounds.width(), bounds.y() + bounds.height(), 0xAAFA0000);
        }
        super.drawOverlay(draw, mouseX, mouseY, delta);
    }
}
