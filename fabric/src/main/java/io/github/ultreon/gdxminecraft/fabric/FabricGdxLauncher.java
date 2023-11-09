package io.github.ultreon.gdxminecraft.fabric;

import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.fabric.FabricModLoader;
import net.fabricmc.api.ModInitializer;

public class FabricGdxLauncher implements ModInitializer {
    @Override
    public void onInitialize() {
        GdxMinecraft.init();
        GdxMinecraft.loader = new FabricModLoader();
    }
}