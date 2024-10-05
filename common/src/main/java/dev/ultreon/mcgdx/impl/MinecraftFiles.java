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

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import dev.ultreon.mcgdx.GdxMinecraft;

import java.io.File;

public class MinecraftFiles implements Files {
    @Override
    public FileHandle getFileHandle(String path, FileType type) {
        return switch (type) {
            case Internal -> internal(path);
            case Classpath -> classpath(path);
            case Absolute -> absolute(path);
            case External -> external(path);
            case Local -> local(path);
        };
    }

    @Override
    public FileHandle classpath(String path) {
        return new Lwjgl3FileHandle(path, FileType.Classpath);
    }

    @Override
    public FileHandle internal(String path) {
        return new MinecraftAssetHandle(path);
    }

    @Override
    public FileHandle external(String path) {
        return new FileHandle(new File(".", path));
    }

    @Override
    public FileHandle absolute(String path) {
        GdxMinecraft.LOGGER.warn("Absolute paths are unsupported behavior!");
        return new FileHandle(new File(path));
    }

    @Override
    public FileHandle local(String path) {
        return new FileHandle(new File(".", path));
    }

    @Override
    public String getExternalStoragePath() {
        throw new GdxRuntimeException("External Storage unavailable!");
    }

    @Override
    public boolean isExternalStorageAvailable() {
        return false;
    }

    @Override
    public String getLocalStoragePath() {
        throw new GdxRuntimeException("External Storage unavailable!");
    }

    @Override
    public boolean isLocalStorageAvailable() {
        return false;
    }
}
