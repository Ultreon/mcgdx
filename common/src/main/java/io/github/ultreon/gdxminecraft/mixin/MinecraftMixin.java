package io.github.ultreon.gdxminecraft.mixin;

import com.mojang.blaze3d.platform.Window;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable
    public ClientLevel level;
    @Shadow @Nullable
    public Screen screen;
    @Shadow @Nullable
    private Overlay overlay;
    @Shadow @Final
    private Window window;

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/SingleTickProfiler;createTickProfiler(Ljava/lang/String;)Lnet/minecraft/util/profiling/SingleTickProfiler;"))
    private void createTickProfiler(CallbackInfo ci) {
        GdxMinecraft.app.update();
    }

    /**
     * @author XyperCode
     * @reason Minecraft breaks with LibGDX if the framerate limit is set.
     */
    @Overwrite
    private int getFramerateLimit() {
        return Integer.MAX_VALUE;
    }
}
