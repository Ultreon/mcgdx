package io.github.ultreon.gdxminecraft.impl.fabric;

import com.badlogic.gdx.files.FileHandle;
import io.github.ultreon.gdxminecraft.api.GameEnvironment;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class FabricModLoader implements ModLoader {
    @Override
    public FileHandle getConfigDir() {
        return new FileHandle(FabricLoader.getInstance().getConfigDir().toFile());
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public GameEnvironment getEnvironmentType() {
        EnvType envType = FabricLoader.getInstance().getEnvironmentType();
        if (envType == null) return GameEnvironment.UNKNOWN;
        return switch (envType) {
            case CLIENT -> GameEnvironment.CLIENT;
            case SERVER -> GameEnvironment.SERVER;
        };
    }
}
