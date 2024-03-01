package org.blocovermelho.ae2emicrafting.client.helper.mapper;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.blocovermelho.ae2emicrafting.client.helper.interfaces.EmiStackConverter;
import org.jetbrains.annotations.Nullable;

public class EmiItemStackConverter implements EmiStackConverter {
    @Override
    public Class<?> getKeyType() {
        return Item.class;
    }

    @Override
    public @Nullable EmiStack toEmiStack(GenericStack stack) {
        if (stack.what() instanceof AEFluidKey fluidKey) {
            return EmiStack.of(fluidKey.getFluid(), fluidKey.copyTag(), stack.amount());
        }

        return null;
    }

    @Override
    public @Nullable GenericStack toGenericStack(EmiStack stack) {
        var item = stack.getKeyOfType(Item.class);

        if (item != null && item != Items.AIR) {
            var itemKey = AEItemKey.of(item, stack.getNbt());
            return new GenericStack(itemKey, stack.getAmount());
        }

        return null;
    }
}
