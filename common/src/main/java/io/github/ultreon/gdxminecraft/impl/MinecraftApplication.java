package io.github.ultreon.gdxminecraft.impl;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import io.github.ultreon.gdxminecraft.mixin.accessors.Lwjgl3ApplicationAccessor;
import io.github.ultreon.gdxminecraft.mixin.accessors.Lwjgl3WindowAccessor;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class MinecraftApplication extends Lwjgl3Application {
    private final GdxMinecraft gdxMc;
    private ApplicationLogger applicationLogger;
    private final Minecraft minecraft;
    private final Array<LifecycleListener> lifecycleListeners = new Array<>();

    public MinecraftApplication(GdxMinecraft gdxMc, Minecraft minecraft) {
        super(gdxMc, createConfig(gdxMc, minecraft));
        this.gdxMc = gdxMc;
        this.applicationLogger = gdxMc.getApplicationLogger();
        this.minecraft = minecraft;
    }

    @NotNull
    public static Lwjgl3ApplicationConfiguration createConfig(GdxMinecraft gdxMc, Minecraft minecraft) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL32, 3, 2);
        config.setWindowListener(gdxMc.getWindowListener());
        return config;
    }

    @Override
    protected void cleanup() {
        if (Minecraft.getInstance().isRunning()) {
            return;
        }
        super.cleanup();
    }

    @Override
    protected void loop() {

    }

    @Override
    public void log(String tag, String message) {
        applicationLogger.log(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        applicationLogger.log(tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        applicationLogger.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        applicationLogger.error(tag, message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        applicationLogger.debug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        applicationLogger.debug(tag, message, exception);
    }

    @Override
    public void setLogLevel(int logLevel) {
        // Do nothing, log level is managed by Slf4j / Log4j.
    }

    @Override
    public int getLogLevel() {
        return Application.LOG_DEBUG;
    }

    @Override
    public Preferences getPreferences(String name) {
        ModLoader loader = GdxMinecraft.loader;
        FileHandle libgdx = loader.getConfigDir().child("libgdx");
        if (!libgdx.exists()) {
            libgdx.mkdirs();
        }
        return new Lwjgl3Preferences(Gdx.files.local("libgdx_" + name + ".txt"));
    }

    @Override
    public void postRunnable(Runnable runnable) {
        this.minecraft.submit(runnable);
    }

    @Override
    public void exit() {
        this.minecraft.stop();
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        this.lifecycleListeners.add(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        this.lifecycleListeners.removeValue(listener, true);
    }

    @Override
    public ApplicationLogger getApplicationLogger() {
        return applicationLogger;
    }

    @Override
    public void setApplicationLogger(ApplicationLogger applicationLogger) {
        this.applicationLogger = applicationLogger;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public GdxMinecraft getGdxMc() {
        return gdxMc;
    }

    public void update() {
        Lwjgl3Window gdxWindow = GdxMinecraft.window.getGdxWindow();
        ((Lwjgl3ApplicationAccessor)this).setCurrentWindow(gdxWindow);
        ((Lwjgl3WindowAccessor) gdxWindow).invokeUpdate();
    }
}
