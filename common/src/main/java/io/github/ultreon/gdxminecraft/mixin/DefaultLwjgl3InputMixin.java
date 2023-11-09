package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationBase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import org.lwjgl.glfw.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultLwjgl3Input.class)
public abstract class DefaultLwjgl3InputMixin implements Disposable {
    @Shadow private GLFWKeyCallback keyCallback;
    @Shadow GLFWCharCallback charCallback;
    @Shadow private GLFWCursorPosCallback cursorPosCallback;
    @Shadow private GLFWMouseButtonCallback mouseButtonCallback;
    @Shadow private GLFWScrollCallback scrollCallback;

    @Inject(method = "windowHandleChanged", at = @At("RETURN"))
    private void gdx_minecraft$injectInit(long windowHandle, CallbackInfo ci) {

    }
}
