package org.blocovermelho.ae2emicrafting.client.helper.mapper;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EmiStackConvertible {
    public class AEGenericStack {
        @Contract(pure = true)
        public static GenericStack into(@NotNull EmiStack source) {
            if (source.getKey() instanceof Fluid fl) {
                return new GenericStack(AEFluidKey.of(fl, source.getNbt()), source.getAmount() == 0 ? 81000L : source.getAmount());
            } else {
                return new GenericStack(AEItemKey.of(source.getItemStack().getItem(), source.getNbt()), source.getAmount());
            }
        }
    }
}
