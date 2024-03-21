package org.blocovermelho.ae2emicrafting.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ActionResult;
import org.blocovermelho.ae2emicrafting.client.config.MainConfig;

public class Ae2EmiMod implements ClientModInitializer {
    public static MainConfig cfg;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(MainConfig.class, GsonConfigSerializer::new);
        ConfigHolder<MainConfig> configHolder = AutoConfig.getConfigHolder(MainConfig.class);
        cfg = configHolder.get();

        configHolder.registerLoadListener((manager, data) -> {
            cfg = data;
            return ActionResult.SUCCESS;
        });

        configHolder.registerSaveListener((manager, data) -> {
            cfg = data;
            return ActionResult.SUCCESS;
        });
    }
}
