package org.blocovermelho.ae2emicrafting.client.helper;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.slot.FakeSlot;
import com.google.common.primitives.Ints;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemStack;
import org.blocovermelho.ae2emicrafting.client.helper.interfaces.DropTarget;

import java.util.ArrayList;
import java.util.List;

public final class DropTargets {
    private DropTargets() {}

    public static List<DropTarget> getTargets(AEBaseScreen<?> aeScreen) {
        List<DropTarget> targets = new ArrayList<>();

        for (var slot : aeScreen.getScreenHandler().slots) {
            if (slot.isEnabled() && slot instanceof FakeSlot fakeSlot) {
                var area = new Rect2i(aeScreen.getGuiLeft() + slot.x, aeScreen.getGuiTop() + slot.y, 16, 16);
                targets.add(new FakeSlotDropTarget(area, fakeSlot));
            }
        }

        return targets;
    }

    private record FakeSlotDropTarget(Rect2i area, FakeSlot slot) implements DropTarget {
        @Override
        public boolean canDrop(GenericStack stack) {
            return slot.canSetFilterTo(wrapFilterAsItem(stack));
        }

        @Override
        public boolean drop(GenericStack stack) {
            var itemStack = wrapFilterAsItem(stack);

            if (slot.canSetFilterTo(itemStack)) {
                NetworkHandler.instance().sendToServer(new InventoryActionPacket(InventoryAction.SET_FILTER,
                        slot.id, itemStack));
                return true;
            }

            return false;
        }

        private static ItemStack wrapFilterAsItem(GenericStack genericStack) {
            if (genericStack.what() instanceof AEItemKey itemKey) {
                return itemKey.toStack(Ints.saturatedCast(Math.max(1, genericStack.amount())));
            } else {
                return GenericStack.wrapInItemStack(genericStack.what(), Math.max(1, genericStack.amount()));
            }
        }
    }
}