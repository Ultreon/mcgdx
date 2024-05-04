package io.github.ultreon.gdxminecraft.mixin.accessors;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Lwjgl3Application.class)
public interface Lwjgl3ApplicationAccessor {
    @Accessor
    void setCurrentWindow(Lwjgl3Window window);
}
