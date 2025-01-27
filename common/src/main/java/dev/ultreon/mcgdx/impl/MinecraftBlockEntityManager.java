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

package dev.ultreon.mcgdx.impl;

import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.api.BlockEntityManager;
import dev.ultreon.mcgdx.api.Gdx3DRenderSource;
import dev.ultreon.mcgdx.api.McGdx;
import dev.ultreon.mcgdx.api.NamespaceID;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;

public class MinecraftBlockEntityManager implements BlockEntityManager {
    @Override
    public void register(NamespaceID id, Gdx3DRenderable source) {
        GdxMinecraft.loader.register(new ResourceLocation(id.domain(), id.namespace()), source);
    }
}
