package org.blocovermelho.ae2emicrafting.client.helper.interfaces;

import appeng.api.stacks.GenericStack;
import net.minecraft.client.util.math.Rect2i;

public interface DropTarget {
    Rect2i area();

    boolean canDrop(GenericStack stack);

    boolean drop(GenericStack stack);
}