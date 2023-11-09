package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationBase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Lwjgl3Window.class)
public interface Lwjgl3WindowAccessor {
    @Accessor
    Lwjgl3ApplicationConfiguration getConfig();

    @Mutable
    @Accessor
    void setConfig(Lwjgl3ApplicationConfiguration config);

    @Invoker("<init>")
    static Lwjgl3Window invokeInit(ApplicationListener listener, Lwjgl3ApplicationConfiguration config, Lwjgl3ApplicationBase application) {
        throw new AssertionError("Mixin failed to apply!");
    }
}
