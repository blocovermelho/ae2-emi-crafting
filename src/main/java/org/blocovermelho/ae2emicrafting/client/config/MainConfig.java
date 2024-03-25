package org.blocovermelho.ae2emicrafting.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ae2-emi-crafting")
public class MainConfig implements ConfigData {
    @ConfigEntry.Category("emi")
    @ConfigEntry.Gui.Tooltip()
    public boolean bomsync = false;

    @ConfigEntry.Category("experiments")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean alwayssync = false;
}
