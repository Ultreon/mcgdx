package io.github.ultreon.gdxminecraft.mixin;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.MinecraftApplication;
import io.github.ultreon.gdxminecraft.impl.MinecraftWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Window.class)
public class McWindowMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;"), remap = false)
    private GLCapabilities gdx_minecraft$injectInit() {
        GLCapabilities capabilities = GL.createCapabilities();
        GdxMinecraft.window = new MinecraftWindow((Window) (Object) this);
        GdxMinecraft.app.newWindow(GdxMinecraft.instance(), MinecraftApplication.createConfig(GdxMinecraft.instance(), Minecraft.getInstance()));
        return capabilities;
    }

    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void gdx_minecraft$injectSetIcon(PackResources packResources, IconSet iconSet, CallbackInfo ci) {
        ci.cancel();
    }
}
