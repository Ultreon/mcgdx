package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationBase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3Window.class)
public abstract class Lwjgl3WindowMixin implements Disposable {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void gdx_minecraft$injectInit(ApplicationListener listener, Lwjgl3ApplicationConfiguration config, Lwjgl3ApplicationBase application, CallbackInfo ci) {
        ((Lwjgl3WindowAccessor) this).setConfig(null); // Gdx should not know the application configuration, as it's fully managed by Minecraft.
    }
    @Inject(method = "create", at = @At("HEAD"))
    private void gdx_minecraft$injectCreate(long windowHandle, CallbackInfo ci) {
        throw new UnsupportedOperationException("Minecraft does not support creating a window.");
    }
}
