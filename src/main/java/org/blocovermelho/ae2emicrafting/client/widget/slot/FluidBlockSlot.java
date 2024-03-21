package org.blocovermelho.ae2emicrafting.client.widget.slot;

import appeng.integration.modules.jeirei.FluidBlockRendering;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;

import java.util.List;

public class FluidBlockSlot extends SlotWidget {
    private final List<Fluid> allFluids;
    public FluidBlockSlot(EmiIngredient stack, int x, int y) {
        super(stack, x, y);
        allFluids = stack.getEmiStacks()
                .stream()
                .map(s -> s.getKeyOfType(Fluid.class))
                .distinct()
                .toList();
    }

    @Override
    public void drawStack(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        Bounds bounds = getBounds();
        FluidBlockRendering.render(matrixStack, getCurrentFluid(), bounds.x(), bounds.y(), bounds.width(), bounds.height());
    }

    private Fluid getCurrentFluid() {
        if (allFluids.isEmpty()) {
            return Fluids.EMPTY;
        }
        int item = (int) (System.currentTimeMillis() / 1000 % allFluids.size());
        return allFluids.get(item);
    }

}
