package org.blocovermelho.ae2emicrafting.client.helper;

import appeng.api.stacks.GenericStack;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantic;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackHelper;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class InventoryUtils {
    public static <T extends AEBaseMenu> List<EmiStack> getStacks(HandledScreen<T> menu, SlotSemantic semantic) {
        return menu.getScreenHandler().getSlots(semantic).stream()
                .map(Slot::getStack)
                .map(EmiStack::of)
                .filter(Objects::nonNull)
                .toList();
    }

    public static <T extends MEStorageMenu> List<EmiStack> getExistingStacks(T menu) {
        Set<GridInventoryEntry> allEntries = menu.getClientRepo().getAllEntries();
        if (allEntries == null) {
            return List.of();
        }

        return allEntries.stream()
                .filter(it -> it.getWhat() != null)
                .filter(it -> it.getStoredAmount() > 0 || it.getRequestableAmount() > 0)
                .map(it -> new GenericStack(it.getWhat(), Math.max(it.getStoredAmount(), it.getRequestableAmount())))
                .map(EmiStackHelper::toEmiStack)
                .filter(Objects::nonNull)
                .toList();
    }
}
