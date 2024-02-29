package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.me.items.PatternEncodingTermMenu;
import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.screen.slot.Slot;
import org.blocovermelho.ae2emicrafting.client.helper.mapper.EmiStackConvertible;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ae2PatternTerminalDragHandler<C extends PatternEncodingTermMenu, T extends PatternEncodingTermScreen<C>> extends EmiDragDropHandler.SlotBased<T> {
    public Ae2PatternTerminalDragHandler() {
        super((f -> {
            C handler = f.getScreenHandler();

            List<Slot> slots = new ArrayList<>();
            slots.addAll(Arrays.stream(handler.getCraftingGridSlots()).toList());
            slots.addAll(Arrays.stream(handler.getProcessingInputSlots()).toList());
            slots.addAll(Arrays.stream(handler.getProcessingOutputSlots()).toList());
            slots.addAll(List.of(
                handler.getSmithingTableBaseSlot(),
                handler.getSmithingTableAdditionSlot(),
                handler.getSmithingTableTemplateSlot()
            ));
            return slots;
        }), (t, slot, emiIngredient) -> {
            List<EmiStack> emiStacks = emiIngredient.getEmiStacks();
            for (EmiStack emiStack : emiStacks) {
                GenericStack aeStack = EmiStackConvertible.AEGenericStack.into(emiStack);
                NetworkHandler.instance().sendToServer(new InventoryActionPacket(InventoryAction.SET_FILTER, slot.id, GenericStack.wrapInItemStack(aeStack)));
            }
        });
    }
}
