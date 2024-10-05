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
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    public void mcGDX$keyPress(long l, int i, int j, int k, int m, CallbackInfo ci) {
        MinecraftApplication app = GdxMinecraft.app;
        if (app == null) return;
        MinecraftInput input = (MinecraftInput) app.getInput();
        input.keyCallback.invoke(l, i, j, k, m);
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    public void mcGDX$charTyped(long l, int i, int j, CallbackInfo ci) {
        MinecraftApplication app = GdxMinecraft.app;
        if (app == null) return;
        MinecraftInput input = (MinecraftInput) app.getInput();
        input.charCallback.invoke(l, i);
    }
}
