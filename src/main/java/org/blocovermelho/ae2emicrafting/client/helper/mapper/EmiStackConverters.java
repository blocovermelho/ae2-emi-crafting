package org.blocovermelho.ae2emicrafting.client.helper.mapper;

import com.google.common.collect.ImmutableList;
import org.blocovermelho.ae2emicrafting.client.helper.interfaces.EmiStackConverter;

import java.util.List;

public final class EmiStackConverters {
    private static List<EmiStackConverter> converters = ImmutableList.of();

    private EmiStackConverters() {}

    public static synchronized boolean register(EmiStackConverter converter) {
        for (var existingConverter : converters) {
            if (existingConverter.getKeyType() == converter.getKeyType()) {
                return false;
            }
        }

        converters = ImmutableList.<EmiStackConverter>builder()
                .addAll(converters)
                .add(converter)
                .build();

        return true;
    }

    public static synchronized List<EmiStackConverter> getConverters() {
        return converters;
    }
}
