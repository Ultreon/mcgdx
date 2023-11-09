package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.utils.Disposable;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Lwjgl3Window.class)
public abstract class Lwjgl3WindowMixin implements Disposable {
    @Shadow
    private long windowHandle;

    @Shadow
    private Lwjgl3Input input;

    @Shadow
    private Lwjgl3Graphics graphics;

    @Shadow
    @Final
    Lwjgl3ApplicationBase application;

    @Shadow
    Lwjgl3WindowListener windowListener;

    @Inject(method = "create", at = @At("HEAD"), remap = false, cancellable = true)
    private void gdx_minecraft$injectCreate(long windowHandle, CallbackInfo ci) {
        ci.cancel();
        this.windowHandle = windowHandle;
        this.input = application.createInput((Lwjgl3Window) (Object) this);
        this.graphics = new Lwjgl3Graphics((Lwjgl3Window) (Object) this);

        if (windowListener != null) {
            windowListener.created((Lwjgl3Window) (Object) this);
        }
    }
    @Inject(method = "update", at = @At("HEAD"), remap = false)
    private void gdx_minecraft$injectUpdate(CallbackInfoReturnable<Boolean> cir) {
        Gdx.graphics = graphics;
        Gdx.gl32 = graphics.getGL32();
        Gdx.gl31 = Gdx.gl32 != null ? Gdx.gl32 : graphics.getGL31();
        Gdx.gl30 = Gdx.gl31 != null ? Gdx.gl31 : graphics.getGL30();
        Gdx.gl20 = Gdx.gl30 != null ? Gdx.gl30 : graphics.getGL20();
        Gdx.gl = Gdx.gl20;
        Gdx.input = input;
    }
}
