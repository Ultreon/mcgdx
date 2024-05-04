package io.github.ultreon.gdxminecraft.mixin.accessors;

import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import org.lwjgl.glfw.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DefaultLwjgl3Input.class)
public interface DefaultLwjgl3InputAccessor {
    @Accessor
    GLFWKeyCallback getKeyCallback();

    @Accessor
    GLFWCharCallback getCharCallback();

    @Accessor
    GLFWCursorPosCallback getCursorPosCallback();

    @Accessor
    GLFWMouseButtonCallback getMouseButtonCallback();

    @Accessor
    GLFWScrollCallback getScrollCallback();
}
