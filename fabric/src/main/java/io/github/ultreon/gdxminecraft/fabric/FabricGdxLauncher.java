package io.github.ultreon.gdxminecraft.fabric;

import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.fabric.FabricModLoader;
import net.fabricmc.api.ClientModInitializer;

public class FabricGdxLauncher implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GdxMinecraft.init();
        GdxMinecraft.loader = new FabricModLoader();
    }
}