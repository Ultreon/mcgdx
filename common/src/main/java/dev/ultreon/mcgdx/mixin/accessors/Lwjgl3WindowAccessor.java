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

package dev.ultreon.mcgdx.mixin.accessors;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationBase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Lwjgl3Window.class)
public interface Lwjgl3WindowAccessor {
    @Accessor
    Lwjgl3ApplicationConfiguration getConfig();

    @Mutable
    @Accessor
    void setConfig(Lwjgl3ApplicationConfiguration config);

    @Invoker("<init>")
    static Lwjgl3Window invokeInit(ApplicationListener listener, Lwjgl3ApplicationConfiguration config, Lwjgl3ApplicationBase application) {
        throw new AssertionError("Mixin failed to apply!");
    }

    @Invoker
    void invokeCreate(long windowHandle);

    @Invoker
    boolean invokeUpdate();
}
