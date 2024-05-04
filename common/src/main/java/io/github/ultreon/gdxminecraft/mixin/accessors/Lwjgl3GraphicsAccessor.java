package io.github.ultreon.gdxminecraft.mixin.accessors;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Lwjgl3Graphics.class)
public interface Lwjgl3GraphicsAccessor {
    @Invoker
    void invokeRenderWindow(long windowHandle, final int width, final int height);
}
