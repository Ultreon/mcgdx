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

import com.badlogic.gdx.Gdx;
import com.mojang.blaze3d.platform.*;
import dev.ultreon.mcgdx.GdxMinecraft;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Window.class)
public abstract class McWindowMixin {
    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void gdx_minecraft$injectSetIcon(PackResources packResources, IconSet iconSet, CallbackInfo ci) {
        GdxMinecraft.init();
        GdxMinecraft.app.getGraphics().getWindow().setWin((Window) (Object) this);
        ci.cancel();
    }
}
