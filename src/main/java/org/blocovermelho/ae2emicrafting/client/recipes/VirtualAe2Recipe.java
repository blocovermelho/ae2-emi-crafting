package org.blocovermelho.ae2emicrafting.client.recipes;

import appeng.core.AppEng;
import net.minecraft.util.Identifier;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public abstract class VirtualAe2Recipe extends BaseAe2Recipe {
    private static final PrimitiveIterator.OfInt ids = IntStream.iterate(0, n -> n + 1).iterator();
    String prefix;
    @Override
    public Identifier getId () {
        return new Identifier(String.format(
                "emi:%s/%s/virt-%d",
                AppEng.MOD_ID,
                prefix,
                ids.nextInt()));
    }
}
