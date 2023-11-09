package io.github.ultreon.gdxminecraft.forge;

import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.forge.ForgeModLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(GdxMinecraft.MOD_ID)
public class ForgeGdxLauncher {
    public ForgeGdxLauncher() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;

        GdxMinecraft.init();
        GdxMinecraft.loader = new ForgeModLoader();
    }
}