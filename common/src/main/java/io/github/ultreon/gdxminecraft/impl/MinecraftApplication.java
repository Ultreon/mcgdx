package io.github.ultreon.gdxminecraft.impl;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.backends.lwjgl3.audio.Lwjgl3Audio;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALLwjgl3Audio;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import io.github.ultreon.gdxminecraft.mixin.Lwjgl3WindowAccessor;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

public class MinecraftApplication implements Lwjgl3ApplicationBase {
    private final GdxMinecraft gdxMc;
    private final Lwjgl3Input input;
    private final Lwjgl3Window window;
    private final Lwjgl3ApplicationConfiguration config;
    private ApplicationLogger applicationLogger;
    private final Minecraft minecraft;
    private final Audio audio;
    private final Files files;
    private final Graphics graphics;
    private final Net net;
    private final Clipboard clipboard;
    private final Array<LifecycleListener> lifecycleListeners = new Array<>();

    public MinecraftApplication(GdxMinecraft gdxMc, Minecraft minecraft) {
        super();
        this.gdxMc = gdxMc;
        this.applicationLogger = gdxMc.getApplicationLogger();
        this.minecraft = minecraft;

        this.config = new Lwjgl3ApplicationConfiguration();
        this.window = Lwjgl3WindowAccessor.invokeInit(gdxMc, config, this);

        Gdx.input = input = this.createInput(window);
        Gdx.audio = audio = this.createAudio(null);
        Gdx.files = files = this.createFiles();
        Gdx.graphics = graphics = this.createGraphics();
        Gdx.net = net = this.createNet();
        clipboard = this.createClipboard();
    }

    private Clipboard createClipboard() {
        return new Lwjgl3Clipboard();
    }

    private Net createNet() {
        return new Lwjgl3Net(this.config);
    }

    private Graphics createGraphics() {
        return new Lwjgl3Graphics(this.window);
    }

    private Files createFiles() {
        return new Lwjgl3Files();
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return gdxMc;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Input getInput() {
        return input;
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
        ModLoader loader = GdxMinecraft.loader;
        FileHandle libgdx = loader.getConfigDir().child("libgdx");
        if (!libgdx.exists()) {
            libgdx.mkdirs();
        }
        return new Lwjgl3Preferences(Gdx.files.local("libgdx_" + name + ".txt"));
    }

    @Override
    public Clipboard getClipboard() {
        return clipboard;
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

    @Override
    public Lwjgl3Audio createAudio(Lwjgl3ApplicationConfiguration config) {
        return new OpenALLwjgl3Audio();
    }

    @Override
    public Lwjgl3Input createInput(Lwjgl3Window window) {
        return new DefaultLwjgl3Input(window);
    }

    public Lwjgl3Window getWindow() {
        return window;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }
}
