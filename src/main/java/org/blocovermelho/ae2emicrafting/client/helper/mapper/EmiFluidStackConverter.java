package org.blocovermelho.ae2emicrafting.client.helper.mapper;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import org.blocovermelho.ae2emicrafting.client.helper.interfaces.EmiStackConverter;
import org.jetbrains.annotations.Nullable;

public class EmiFluidStackConverter implements EmiStackConverter {
    @Override
    public Class<?> getKeyType() {
        return Fluid.class;
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
        var fluid = stack.getKeyOfType(Fluid.class);

        if (fluid != null && fluid != Fluids.EMPTY) {
            var fluidKey = AEFluidKey.of(fluid, stack.getNbt());
            return new GenericStack(fluidKey, stack.getAmount());
        }

        return null;
    }
}
