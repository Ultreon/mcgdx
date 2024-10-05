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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MinecraftAssetHandle extends FileHandle {
    private final String path;

    public MinecraftAssetHandle(String path) {
        super(path);
        this.path = path;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public OutputStream write(boolean append) {
        throw new GdxRuntimeException("Read-only file system!");
    }

    @Override
    public InputStream read() {
        ResourceLocation resourceLocation = ResourceLocation.tryParse(path);
        if (resourceLocation == null) {
            throw new GdxRuntimeException("Invalid resource ID: " + path);
        }
        try {
            return Minecraft.getInstance().getResourceManager().open(resourceLocation);
        } catch (IOException e) {
            throw new GdxRuntimeException("I/O Error Occurred", e);
        }
    }
}
