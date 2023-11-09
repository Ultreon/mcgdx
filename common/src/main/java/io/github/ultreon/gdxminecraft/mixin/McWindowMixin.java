package io.github.ultreon.gdxminecraft.mixin;

import com.mojang.blaze3d.platform.*;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.MinecraftApplication;
import io.github.ultreon.gdxminecraft.impl.MinecraftWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Window.class)
public class McWindowMixin {

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void gdx_minecraft$injectInit(WindowEventHandler windowEventHandler, ScreenManager screenManager, DisplayData displayData, String string, String string2, CallbackInfo ci) {
        GdxMinecraft.window = new MinecraftWindow((Window) (Object) this);
        GdxMinecraft.app = new MinecraftApplication(GdxMinecraft.instance(), Minecraft.getInstance());
    }

    @Inject(method = "setIcon", at = @At("HEAD"), remap = false, cancellable = true)
    private void gdx_minecraft$injectSetIcon(PackResources packResources, IconSet iconSet, CallbackInfo ci) {
        ci.cancel();
    }
}
