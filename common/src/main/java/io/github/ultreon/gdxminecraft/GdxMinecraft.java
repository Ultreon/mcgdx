package io.github.ultreon.gdxminecraft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.mojang.blaze3d.platform.Window;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import io.github.ultreon.gdxminecraft.impl.MinecraftApplication;
import io.github.ultreon.gdxminecraft.impl.MinecraftGdxLogger;
import io.github.ultreon.gdxminecraft.impl.MinecraftWindow;
import io.github.ultreon.gdxminecraft.mixin.Lwjgl3ApplicationAccessor;
import io.github.ultreon.gdxminecraft.mixin.Lwjgl3GraphicsAccessor;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GdxMinecraft implements ApplicationListener {
    public static final String MOD_ID = "gdx_minecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger("GDX-Minecraft");
    private static GdxMinecraft instance;
    private final Lwjgl3WindowListener windowListener = new UselessWindowListener();
    public static ModLoader loader;
    private final ApplicationLogger logger = new MinecraftGdxLogger();
    public static MinecraftApplication app;
    public static MinecraftWindow window;
    private static ShapeDrawer shapeRenderer;
    private static Batch batch;
    private static Thread gdxThread;
    private int width;
    private int height;
    private static Texture texture;
    private boolean posted;
    private static BitmapFont bitmapFont;

    public GdxMinecraft() {
        instance = this;
        try {
            Lwjgl3NativesLoader.load();
            GdxMinecraft.app = new MinecraftApplication(this, Minecraft.getInstance());
        } catch (Throwable e) {
            CrashReport libGDXCrash = new CrashReport("LibGDX failed to initialize", e);
            CrashReportCategory libGDX = libGDXCrash.addCategory("LibGDX");
            libGDX.setDetail("LibGDX Version", Version.VERSION);
            Minecraft.crash(libGDXCrash);
        }
    }

    public static GdxMinecraft instance() {
        return instance;
    }

    @ApiStatus.Internal
    public static void init() {
        new GdxMinecraft();
    }

    public static void initialize() {
        batch = new SpriteBatch();
        Pixmap whitePix = new Pixmap(1, 1, Pixmap.Format.RGB888);
        whitePix.drawPixel(0, 0, 0xffffffff);
        Texture white = new Texture(whitePix, true);
        shapeRenderer = new ShapeDrawer(batch, new TextureRegion(white));
        texture = new Texture(new Pixmap(Gdx.files.internal("test.png")), true);
        gdxThread = Thread.currentThread();
        bitmapFont = new BitmapFont(true);
    }

    @Override
    public void create() {
        Gdx.app.log("GDX-Minecraft", "LibGDX version: " + Version.VERSION);
        GdxMinecraft.initialize();
    }

    public void resize0(int width, int height) {
        LOGGER.info("Resizing to " + width + "x" + height);

        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho(0, width, height, 0, -1000000, 1000000));

        this.width = width;
        this.height = height;

        long windowHandle = window.getHandle();
        if (Configuration.GLFW_CHECK_THREAD0.get(true)) {
            this.renderWindow(windowHandle, width, height);
        } else {
            if (posted) return;
            posted = true;
            Gdx.app.postRunnable(() -> {
                posted = false;
                renderWindow(windowHandle, width, height);
            });
        }
    }

    @Override
    public void resize(int width, int height) {
        Matrix4 batchMat4 = batch.getProjectionMatrix().setToOrtho(0, width, height, 0, 0, 1000000);
        batch.setProjectionMatrix(batchMat4);
    }

    private void renderWindow(long windowHandle, int width, int height) {
        ((Lwjgl3GraphicsAccessor) app.getGraphics()).invokeRenderWindow(windowHandle, width, height);
    }

    @Override
    public void render() {
        Window window = GdxMinecraft.window.getWindow();
        int width = window.getWidth();
        int height = window.getHeight();
        if (width != this.width || height != this.height) {
            this.resize0(width, height);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public ApplicationLogger getApplicationLogger() {
        return logger;
    }

    public Lwjgl3WindowListener getWindowListener() {
        return this.windowListener;
    }

    @Deprecated
    public ApplicationListener getApplicationListener() {
        return this;
    }

    public ShapeDrawer getShapeDrawer() {
        return shapeRenderer;
    }

    public Batch getBatch() {
        return batch;
    }

    public boolean isOnGdxThread() {
        return Thread.currentThread() == gdxThread;
    }

    public Texture getTexture() {
        return texture;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    private static class UselessWindowListener implements Lwjgl3WindowListener {
        @Override
        public void created(Lwjgl3Window window) {

        }

        @Override
        public void iconified(boolean isIconified) {

        }

        @Override
        public void maximized(boolean isMaximized) {

        }

        @Override
        public void focusLost() {

        }

        @Override
        public void focusGained() {

        }

        @Override
        public boolean closeRequested() {
            return false;
        }

        @Override
        public void filesDropped(String[] files) {

        }

        @Override
        public void refreshRequested() {

        }
    }

}
