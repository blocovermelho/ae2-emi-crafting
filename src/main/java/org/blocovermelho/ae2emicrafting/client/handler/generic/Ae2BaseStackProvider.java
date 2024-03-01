package org.blocovermelho.ae2emicrafting.client.handler.generic;

import appeng.client.gui.AEBaseScreen;
import dev.emi.emi.api.EmiStackProvider;
import dev.emi.emi.api.stack.EmiStackInteraction;
import net.minecraft.client.gui.screen.Screen;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackHelper;

public class Ae2BaseStackProvider implements EmiStackProvider<Screen> {
    @Override
    public EmiStackInteraction getStackAt(Screen screen, int x, int y) {
        if (screen instanceof AEBaseScreen<?> aeScreen) {
            var stack = aeScreen.getStackUnderMouse(x, y);
            if (stack != null) {
                var emiStack = EmiStackHelper.toEmiStack(stack.stack());
                if (emiStack != null) {
                    return new EmiStackInteraction(emiStack);
                }
            }
        }
        return EmiStackInteraction.EMPTY;
    }
}
