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

package dev.ultreon.mcgdx.forge;

import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.impl.forge.ForgeModLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(GdxMinecraft.MOD_ID)
public class ForgeGdxLauncher {
    public ForgeGdxLauncher() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;

        GdxMinecraft.setup();
        GdxMinecraft.loader = new ForgeModLoader();
    }
}