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

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.math.GridPoint2;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.io.PrintStream;
import java.nio.IntBuffer;

public class MinecraftApplicationConfiguration {
    public static PrintStream errorStream = System.err;
    public boolean vSyncEnabled;
    public int foregroundFPS;
    public HdpiMode hdpiMode = HdpiMode.Pixels;
    public int audioDeviceSimultaneousSources = 256;
    public int audioDeviceBufferCount = 256;
    public int audioDeviceBufferSize = 4096;
    public int maxNetThreads = 8;
    private boolean decorated;
    private boolean resizable;

    static MinecraftGraphics.MinecraftMonitor toMinecraftMonitor(long glfwMonitor) {
        IntBuffer tmp = BufferUtils.createIntBuffer(1);
        IntBuffer tmp2 = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetMonitorPos(glfwMonitor, tmp, tmp2);
        int virtualX = tmp.get(0);
        int virtualY = tmp2.get(0);
        String name = GLFW.glfwGetMonitorName(glfwMonitor);
        return new MinecraftGraphics.MinecraftMonitor(glfwMonitor, virtualX, virtualY, name);
    }

    /**
     * @return the currently active {@link Graphics.DisplayMode} of the primary monitor
     */
    public static Graphics.DisplayMode getDisplayMode() {
        MinecraftApplication.initializeGlfw();
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        return new MinecraftGraphics.MinecraftDisplayMode(GLFW.glfwGetPrimaryMonitor(), videoMode.width(), videoMode.height(),
                videoMode.refreshRate(), videoMode.redBits() + videoMode.greenBits() + videoMode.blueBits());
    }

    /**
     * @return the currently active {@link Graphics.DisplayMode} of the given monitor
     */
    public static Graphics.DisplayMode getDisplayMode(Graphics.Monitor monitor) {
        MinecraftApplication.initializeGlfw();
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(((MinecraftGraphics.MinecraftMonitor) monitor).monitorHandle);
        return new MinecraftGraphics.MinecraftDisplayMode(((MinecraftGraphics.MinecraftMonitor) monitor).monitorHandle, videoMode.width(), videoMode.height(),
                videoMode.refreshRate(), videoMode.redBits() + videoMode.greenBits() + videoMode.blueBits());
    }

    /**
     * @return the available {@link Graphics.DisplayMode}s of the primary monitor
     */
    public static Graphics.DisplayMode[] getDisplayModes() {
        MinecraftApplication.initializeGlfw();
        GLFWVidMode.Buffer videoModes = GLFW.glfwGetVideoModes(GLFW.glfwGetPrimaryMonitor());
        Graphics.DisplayMode[] result = new Graphics.DisplayMode[videoModes.limit()];
        for (int i = 0; i < result.length; i++) {
            GLFWVidMode videoMode = videoModes.get(i);
            result[i] = new MinecraftGraphics.MinecraftDisplayMode(GLFW.glfwGetPrimaryMonitor(), videoMode.width(), videoMode.height(),
                    videoMode.refreshRate(), videoMode.redBits() + videoMode.greenBits() + videoMode.blueBits());
        }
        return result;
    }

    /**
     * @return the available {@link Graphics.DisplayMode}s of the given {@link Graphics.Monitor}
     */
    public static Graphics.DisplayMode[] getDisplayModes(Graphics.Monitor monitor) {
        MinecraftApplication.initializeGlfw();
        GLFWVidMode.Buffer videoModes = GLFW.glfwGetVideoModes(((MinecraftGraphics.MinecraftMonitor) monitor).monitorHandle);
        Graphics.DisplayMode[] result = new Graphics.DisplayMode[videoModes.limit()];
        for (int i = 0; i < result.length; i++) {
            GLFWVidMode videoMode = videoModes.get(i);
            result[i] = new MinecraftGraphics.MinecraftDisplayMode(((MinecraftGraphics.MinecraftMonitor) monitor).monitorHandle, videoMode.width(),
                    videoMode.height(), videoMode.refreshRate(), videoMode.redBits() + videoMode.greenBits() + videoMode.blueBits());
        }
        return result;
    }

    /**
     * @return the primary {@link Graphics.Monitor}
     */
    public static Graphics.Monitor getPrimaryMonitor() {
        MinecraftApplication.initializeGlfw();
        return toMinecraftMonitor(GLFW.glfwGetPrimaryMonitor());
    }

    /**
     * @return the connected {@link Graphics.Monitor}s
     */
    public static Graphics.Monitor[] getMonitors() {
        MinecraftApplication.initializeGlfw();
        PointerBuffer glfwMonitors = GLFW.glfwGetMonitors();
        Graphics.Monitor[] monitors = new Graphics.Monitor[glfwMonitors.limit()];
        for (int i = 0; i < glfwMonitors.limit(); i++) {
            monitors[i] = toMinecraftMonitor(glfwMonitors.get(i));
        }
        return monitors;
    }

    static GridPoint2 calculateCenteredWindowPosition(MinecraftGraphics.MinecraftMonitor monitor, int newWidth, int newHeight) {
        IntBuffer tmp = BufferUtils.createIntBuffer(1);
        IntBuffer tmp2 = BufferUtils.createIntBuffer(1);
        IntBuffer tmp3 = BufferUtils.createIntBuffer(1);
        IntBuffer tmp4 = BufferUtils.createIntBuffer(1);

        Graphics.DisplayMode displayMode = getDisplayMode(monitor);

        GLFW.glfwGetMonitorWorkarea(monitor.monitorHandle, tmp, tmp2, tmp3, tmp4);
        int workareaWidth = tmp3.get(0);
        int workareaHeight = tmp4.get(0);

        int minX, minY, maxX, maxY;

        // If the new width is greater than the working area, we have to ignore stuff like the taskbar for centering and use the
        // whole monitor's size
        if (newWidth > workareaWidth) {
            minX = monitor.virtualX;
            maxX = displayMode.width;
        } else {
            minX = tmp.get(0);
            maxX = workareaWidth;
        }
        // The same is true for height
        if (newHeight > workareaHeight) {
            minY = monitor.virtualY;
            maxY = displayMode.height;
        } else {
            minY = tmp2.get(0);
            maxY = workareaHeight;
        }

        return new GridPoint2(Math.max(minX, minX + (maxX - newWidth) / 2), Math.max(minY, minY + (maxY - newHeight) / 2));
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isResizable() {
        return resizable;
    }
}
