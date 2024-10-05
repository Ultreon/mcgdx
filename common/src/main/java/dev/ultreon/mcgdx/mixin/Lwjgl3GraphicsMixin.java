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

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(Lwjgl3Graphics.class)
public abstract class Lwjgl3GraphicsMixin implements Disposable, Graphics {
    @Shadow @Final
    Lwjgl3Window window;
    @Shadow
    IntBuffer tmpBuffer;
    @Shadow
    IntBuffer tmpBuffer2;
    @Shadow private volatile int backBufferWidth;
    @Shadow private volatile int backBufferHeight;
    @Shadow private volatile int logicalWidth;
    @Shadow private volatile int logicalHeight;
    @Shadow private Graphics.BufferFormat bufferFormat;

    @Shadow private int fps;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetFramebufferSizeCallback(JLorg/lwjgl/glfw/GLFWFramebufferSizeCallbackI;)Lorg/lwjgl/glfw/GLFWFramebufferSizeCallback;"), remap = false)
    private GLFWFramebufferSizeCallback gdx_minecraft$injectInit(long window, GLFWFramebufferSizeCallbackI cbfun) {
        return new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                // Do nothing lmao
            }
        };
    }

    /**
     * @author XyperCode
     * @reason 
     */
    @Overwrite(remap = false)
    void updateFramebufferInfo() {
        GLFW.glfwGetFramebufferSize(window.getWindowHandle(), tmpBuffer, tmpBuffer2);
        this.backBufferWidth = tmpBuffer.get(0);
        this.backBufferHeight = tmpBuffer2.get(0);
        GLFW.glfwGetWindowSize(window.getWindowHandle(), tmpBuffer, tmpBuffer2);
        logicalWidth = tmpBuffer.get(0);
        logicalHeight = tmpBuffer2.get(0);
        bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 16, 16, false);
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the FPS, so we just get the fps from the Minecraft instance.
     */
    @Overwrite(remap = false)
    public int getFramesPerSecond() {
        return Minecraft.getInstance().getFps();
    }
}
