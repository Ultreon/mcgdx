/*
 * Copyright (c) 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ultreon.mcgdx.mixin;

import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.impl.MinecraftApplication;
import dev.ultreon.mcgdx.impl.MinecraftInput;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onMove", at = @At("HEAD"))
    public void mcGDX$onMove(long l, double d, double e, CallbackInfo ci) {
        MinecraftApplication app = GdxMinecraft.app;
        if (app == null) return;
        MinecraftInput input = (MinecraftInput) app.getInput();
        input.cursorPosCallback.invoke(l, d, e);
    }

    @Inject(method = "onPress", at = @At("HEAD"))
    public void mcGDX$onPress(long l, int i, int j, int k, CallbackInfo ci) {
        MinecraftApplication app = GdxMinecraft.app;
        if (app == null) return;
        MinecraftInput input = (MinecraftInput) app.getInput();
        input.mouseButtonCallback.invoke(l, i, j, k);
    }

    @Inject(method = "onScroll", at = @At("HEAD"))
    public void mcGDX$onScroll(long l, double e, double d, CallbackInfo ci) {
        MinecraftApplication app = GdxMinecraft.app;
        if (app == null) return;
        MinecraftInput input = (MinecraftInput) app.getInput();
        input.scrollCallback.invoke(l, d, e);
    }
}
