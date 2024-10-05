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

import java.lang.reflect.Method;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import com.badlogic.gdx.graphics.glutils.GLVersion;

import com.badlogic.gdx.utils.*;
import dev.ultreon.mcgdx.GdxMinecraft;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Callback;

public class MinecraftApplication implements MinecraftApplicationBase {
    private final MinecraftApplicationConfiguration config = new MinecraftApplicationConfiguration();
    final Array<MinecraftWindow> windows = new Array<>();
    private volatile MinecraftWindow currentWindow;
    private MinecraftAudio audio;
    private final Files files = new Lwjgl3Files();
    private final Net net;
    private final ObjectMap<String, Preferences> preferences = new ObjectMap<>();
    private final MinecraftClipboard clipboard;
    private int logLevel = LOG_INFO;
    private ApplicationLogger applicationLogger;
    private volatile boolean running = true;
    private final Queue<Runnable> runnables = new Queue<>();
    private final Array<Runnable> executedRunnables = new Array<>();
    private final Array<LifecycleListener> lifecycleListeners = new Array<>();
    private static GLFWErrorCallback errorCallback;
    private static GLVersion glVersion;
    private static Callback glDebugCallback;

    static void initializeGlfw() {
        if (errorCallback == null) {
            Lwjgl3NativesLoader.load();
            errorCallback = GLFWErrorCallback.createPrint(MinecraftApplicationConfiguration.errorStream);
            GLFW.glfwSetErrorCallback(errorCallback);
            if (SharedLibraryLoader.isMac)
                GLFW.glfwInitHint(GLFW.GLFW_ANGLE_PLATFORM_TYPE, GLFW.GLFW_ANGLE_PLATFORM_TYPE_METAL);
            GLFW.glfwInitHint(GLFW.GLFW_JOYSTICK_HAT_BUTTONS, GLFW.GLFW_FALSE);
            if (!GLFW.glfwInit()) {
                throw new GdxRuntimeException("Unable to initialize GLFW");
            }
        }
    }

    public MinecraftApplication(ApplicationListener listener, MinecraftApplicationConfiguration minecraftApplicationConfiguration, Minecraft instance) {
        this.net = new MinecraftNet(minecraftApplicationConfiguration);
        this.clipboard = new MinecraftClipboard();

        currentWindow = new MinecraftWindow(listener, minecraftApplicationConfiguration, this);

        GdxMinecraft.window = currentWindow;

        this.applicationLogger = new MinecraftGdxLogger();

        MinecraftApplication app = this;
        Gdx.app = app;
        Gdx.files = app.files;

        Gdx.gl = app.getGraphics().gl20;
        Gdx.gl20 = app.getGraphics().gl20;
        Gdx.gl30 = app.getGraphics().getGL30();
        Gdx.gl31 = app.getGraphics().getGL31();
        Gdx.gl32 = app.getGraphics().getGL32();

        Gdx.graphics = app.getGraphics();

        Gdx.audio = app.audio;
        Gdx.net = app.net;

        Gdx.input = app.getInput();
    }

    public void create() {
        getApplicationListener().create();
    }

    public void update() {
        currentWindow.update();

        Runnable func;
        while (!runnables.isEmpty()) {
            func = runnables.removeFirst();
            func.run();
        }
    }

    protected void cleanupWindows() {
        synchronized (lifecycleListeners) {
            for (LifecycleListener lifecycleListener : lifecycleListeners) {
                lifecycleListener.pause();
                lifecycleListener.dispose();
            }
        }
        for (MinecraftWindow window : windows) {
            window.dispose();
        }
        windows.clear();
    }

    protected void cleanup() {
        GdxMinecraft.LOGGER.warn("cleanup() called, this is unsupported behavior!");
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return currentWindow.getListener();
    }

    @Override
    public MinecraftGraphics getGraphics() {
        return currentWindow.getGraphics();
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Input getInput() {
        return currentWindow.getInput();
    }

    @Override
    public Files getFiles() {
        return files;
    }

    @Override
    public Net getNet() {
        return net;
    }

    @Override
    public void debug(String tag, String message) {
        if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message, exception);
    }

    @Override
    public void log(String tag, String message) {
        if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message, exception);
    }

    @Override
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public void setApplicationLogger(ApplicationLogger applicationLogger) {
        this.applicationLogger = applicationLogger;
    }

    @Override
    public ApplicationLogger getApplicationLogger() {
        return applicationLogger;
    }

    @Override
    public ApplicationType getType() {
        return ApplicationType.Desktop;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public long getJavaHeap() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @Override
    public long getNativeHeap() {
        return getJavaHeap();
    }

    @Override
    public Preferences getPreferences(String name) {
        if (preferences.containsKey(name)) {
            return preferences.get(name);
        } else {
            return null;
        }
    }

    @Override
    public Clipboard getClipboard() {
        return clipboard;
    }

    @Override
    public void postRunnable(Runnable runnable) {
        synchronized (runnables) {
            runnables.addLast(runnable);
        }
    }

    @Override
    public void exit() {
        running = false;
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.add(listener);
        }
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.removeValue(listener, true);
        }
    }

    @Override
    public MinecraftAudio createAudio(MinecraftApplicationConfiguration config) {
        return new OpenALMinecraftAudio(config.audioDeviceSimultaneousSources, config.audioDeviceBufferCount, config.audioDeviceBufferSize);
    }

    protected Files createFiles() {
        return new Lwjgl3Files();
    }

    private static void initiateGL(boolean useGLES20) {
        if (!useGLES20) {
            String versionString = GL11.glGetString(GL11.GL_VERSION);
            String vendorString = GL11.glGetString(GL11.GL_VENDOR);
            String rendererString = GL11.glGetString(GL11.GL_RENDERER);
            glVersion = new GLVersion(Application.ApplicationType.Desktop, versionString, vendorString, rendererString);
        } else {
            try {
                Class<?> gles = Class.forName("org.lwjgl.opengles.GLES20");
                Method getString = gles.getMethod("glGetString", int.class);
                String versionString = (String) getString.invoke(gles, GL11.GL_VERSION);
                String vendorString = (String) getString.invoke(gles, GL11.GL_VENDOR);
                String rendererString = (String) getString.invoke(gles, GL11.GL_RENDERER);
                glVersion = new GLVersion(Application.ApplicationType.Desktop, versionString, vendorString, rendererString);
            } catch (Throwable e) {
                throw new GdxRuntimeException("Couldn't get GLES version string.", e);
            }
        }
    }

    private static boolean supportsFBO() {
        // FBO is in core since OpenGL 3.0, see https://www.opengl.org/wiki/Framebuffer_Object
        return glVersion.isVersionEqualToOrHigher(3, 0) || GLFW.glfwExtensionSupported("GL_EXT_framebuffer_object") || GLFW.glfwExtensionSupported("GL_ARB_framebuffer_object");
    }
}
