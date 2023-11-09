package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.InputEventQueue;
import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import org.lwjgl.glfw.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
