package io.github.ultreon.gdxminecraft.impl.forge;

import com.badlogic.gdx.files.FileHandle;
import io.github.ultreon.gdxminecraft.api.GameEnvironment;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeModLoader implements ModLoader {
    @Override
    public FileHandle getConfigDir() {
        return new FileHandle(FMLPaths.CONFIGDIR.get().toFile());
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public GameEnvironment getEnvironmentType() {
        Dist dist = FMLEnvironment.dist;
        if (dist == null) return GameEnvironment.UNKNOWN;

        return switch (dist) {
            case CLIENT -> GameEnvironment.CLIENT;
            case DEDICATED_SERVER -> GameEnvironment.SERVER;
        };
    }
}
