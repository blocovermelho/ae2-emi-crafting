package org.blocovermelho.ae2emicrafting.client.handler.generic;

import appeng.client.gui.AEBaseScreen;
import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.blocovermelho.ae2emicrafting.client.helper.DropTargets;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackHelper;

import java.util.Objects;
import java.util.stream.Collectors;

public class Ae2PatternTerminalDragHandler implements EmiDragDropHandler<Screen> {
    @Override
    public boolean dropStack(Screen screen, EmiIngredient emiIngredient, int x, int y) {
        if (!(screen instanceof AEBaseScreen<?> aeScreen)) {
            return false;
        }

        var targets = DropTargets.getTargets(aeScreen);

        for (var target : targets) {
            if (target.area().contains(x, y)) {
                for (var emiStack : emiIngredient.getEmiStacks()) {
                    var filter = EmiStackHelper.toGenericStack(emiStack);

                    if (filter != null && target.drop(filter)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void render(Screen screen, EmiIngredient dragged, DrawContext draw, int mouseX, int mouseY, float delta) {
        if (!(screen instanceof AEBaseScreen<?>aeScreen)) {
            return;
        }

        var potentialStacks = dragged.getEmiStacks().stream()
                .map(EmiStackHelper::toGenericStack)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var targets = DropTargets.getTargets(aeScreen);

        for (var target : targets) {
            if (potentialStacks.stream().noneMatch(target::canDrop)) {
                continue;
            }

            var area = target.area();

            draw.fill(
                    area.getX(),
                    area.getY(),
                    area.getX() + area.getWidth(),
                    area.getY() + area.getHeight(),
                    0x8822BB33);
        }
    }
}
