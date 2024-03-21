package org.blocovermelho.ae2emicrafting.client;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AE2EmiCraftingPlugin implements EmiPlugin {
    public static final String MOD_ID = "ae2-emi-crafting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void register(EmiRegistry registry) {
        FabricLoader loader = FabricLoader.getInstance();
        LOGGER.info("AE2 Emi Crafting Support!");


        if (loader.isModLoaded("ae2")) {
            new Ae2EmiPlugin().register(registry);
            LOGGER.info("AE2 done.");
        }

        if (loader.isModLoaded("ae2wtlib")) {
            new Ae2WtEmiPlugin().register(registry);
            LOGGER.info("AE2WTLIB done.");
        }
    }
}
