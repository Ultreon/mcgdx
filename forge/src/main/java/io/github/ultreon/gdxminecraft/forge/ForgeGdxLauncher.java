package io.github.ultreon.gdxminecraft.forge;

import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.forge.ForgeModLoader;
import net.minecraftforge.fml.common.Mod;

@Mod(GdxMinecraft.MOD_ID)
public class ForgeGdxLauncher {
    public ForgeGdxLauncher() {
        GdxMinecraft.init();
        GdxMinecraft.loader = new ForgeModLoader();
    }
}