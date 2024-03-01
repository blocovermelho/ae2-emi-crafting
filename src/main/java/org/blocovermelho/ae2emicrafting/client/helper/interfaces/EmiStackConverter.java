package org.blocovermelho.ae2emicrafting.client.helper.interfaces;

import appeng.api.stacks.GenericStack;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.Nullable;

public interface EmiStackConverter {
    Class<?> getKeyType();

    @Nullable
    EmiStack toEmiStack(GenericStack stack);

    @Nullable
    GenericStack toGenericStack(EmiStack stack);
}