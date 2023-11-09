package io.github.ultreon.gdxminecraft.api;

import com.badlogic.gdx.files.FileHandle;

public interface ModLoader {
    FileHandle getConfigDir();
    boolean isDevelopmentEnvironment();
    GameEnvironment getEnvironmentType();
}
