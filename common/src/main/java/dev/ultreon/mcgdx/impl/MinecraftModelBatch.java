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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;

public class MinecraftModelBatch extends ModelBatch {
    private final boolean ownContext;
    private boolean ending;

    public MinecraftModelBatch(RenderContext context, ShaderProvider shaderProvider, RenderableSorter sorter) {
        super(context, shaderProvider, sorter);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(RenderContext context, ShaderProvider shaderProvider) {
        super(context, shaderProvider);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(RenderContext context, RenderableSorter sorter) {
        super(context, sorter);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(RenderContext context) {
        super(context);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(ShaderProvider shaderProvider, RenderableSorter sorter) {
        super(shaderProvider, sorter);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(RenderableSorter sorter) {
        super(sorter);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(ShaderProvider shaderProvider) {
        super(shaderProvider);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(FileHandle vertexShader, FileHandle fragmentShader) {
        super(vertexShader, fragmentShader);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        this.ownContext = (context == null);
    }

    public MinecraftModelBatch() {
        this.ownContext = (context == null);
    }

    @Override
    public void render(Renderable renderable) {
        Gdx.app.postRunnable(() -> super.render(renderable));
    }

    @Override
    public void render(RenderableProvider renderableProvider) {
        Gdx.app.postRunnable(() -> super.render(renderableProvider));
    }

    @Override
    public <T extends RenderableProvider> void render(Iterable<T> renderableProviders) {
        Gdx.app.postRunnable(() -> super.render(renderableProviders));
    }

    @Override
    public void render(RenderableProvider renderableProvider, Environment environment) {
        Gdx.app.postRunnable(() -> super.render(renderableProvider, environment));
    }

    @Override
    public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Environment environment) {
        Gdx.app.postRunnable(() -> super.render(renderableProviders, environment));
    }

    @Override
    public void render(RenderableProvider renderableProvider, Shader shader) {
        Gdx.app.postRunnable(() -> super.render(renderableProvider, shader));
    }

    @Override
    public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Shader shader) {
        Gdx.app.postRunnable(() -> super.render(renderableProviders, shader));
    }

    @Override
    public void render(RenderableProvider renderableProvider, Environment environment, Shader shader) {
        Gdx.app.postRunnable(() -> super.render(renderableProvider, environment, shader));
    }

    @Override
    public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Environment environment, Shader shader) {
        Gdx.app.postRunnable(() -> super.render(renderableProviders, environment, shader));
    }

    @Override
    public void begin(Camera cam) {
        Gdx.app.postRunnable(() -> {
            if (this.ending) {
                CrashReport report = new CrashReport("Can't begin the batch while ending it...", new Error("Sanity Check"));
                throw new ReportedException(report);
            }
            super.begin(cam);
        });
    }

    @Override
    public void flush() {
        Gdx.app.postRunnable(() -> {
            if (this.ending) {
                CrashReport report = new CrashReport("Attempted to flush while ending the batch...", new Error("Sanity Check"));
                throw new ReportedException(report);
            }
            super.flush();
        });
    }

    @Override
    public void end() {
        Gdx.app.postRunnable(() -> {
            super.flush();
            if (ownContext) context.end();
            camera = null;
        });
    }
}
