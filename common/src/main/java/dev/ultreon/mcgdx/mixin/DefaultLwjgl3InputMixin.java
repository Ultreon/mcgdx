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

import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import com.badlogic.gdx.utils.Disposable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DefaultLwjgl3Input.class)
public abstract class DefaultLwjgl3InputMixin implements Disposable {
    @Shadow public abstract void resetPollingStates();

    /**
     * @author XyperCode
     * @reason Minecraft handles LWJGL3 input events by default.
     */
    @Overwrite(remap = false)
    public void windowHandleChanged(long windowHandle) {
        resetPollingStates();
    }
}
