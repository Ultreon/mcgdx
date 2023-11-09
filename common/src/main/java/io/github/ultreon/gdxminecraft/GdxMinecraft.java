package io.github.ultreon.gdxminecraft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mojang.blaze3d.platform.Window;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import io.github.ultreon.gdxminecraft.impl.MinecraftApplication;
import io.github.ultreon.gdxminecraft.impl.MinecraftGdxLogger;
import io.github.ultreon.gdxminecraft.impl.MinecraftWindow;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GdxMinecraft implements ApplicationListener {
	public static final String MOD_ID = "gdx_minecraft";
	public static final Logger LOGGER = LoggerFactory.getLogger("GDX-Minecraft");
	private static GdxMinecraft instance;
	private final Lwjgl3WindowListener windowListener = new UselessWindowListener();
	public static ModLoader loader;
	private final ApplicationLogger logger = new MinecraftGdxLogger();
	public static MinecraftApplication app;
	public static MinecraftWindow window;
	private ShapeRenderer shapeRenderer;
	private Batch batch;
	private Thread gdxThread;
	private int width;
	private int height;

	public GdxMinecraft() {
		instance = this;
		Lwjgl3NativesLoader.load();
	}

	public static GdxMinecraft instance() {
		return instance;
	}

	@ApiStatus.Internal
	public static void init() {
		new GdxMinecraft();
	}

	@Override
	public void create() {
		Gdx.app.log("GDX-Minecraft", "LibGDX version: " + Version.VERSION);

		this.shapeRenderer = new ShapeRenderer();
		this.batch = new SpriteBatch();
		this.gdxThread = Thread.currentThread();
	}

	@Override
	public void resize(int width, int height) {
		LOGGER.info("Resizing to " + width + "x" + height);

		this.shapeRenderer.getProjectionMatrix().setToOrtho(0, width, height, 0, 0, 1000000);
		this.batch.getProjectionMatrix().setToOrtho(0, width, height, 0, 0, 1000000);
		this.shapeRenderer.updateMatrices();
		this.width = width;
		this.height = height;
	}

	@Override
	public void render() {
		Window window = GdxMinecraft.window.getWindow();
		int width = window.getWidth();
		int height = window.getHeight();
		if (width != this.width || height != this.height) {
			this.resize(width, height);
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
		this.shapeRenderer.dispose();
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

	public ShapeRenderer getShapeDrawer() {
		return shapeRenderer;
	}

	public Batch getBatch() {
		return batch;
	}

	public boolean isOnGdxThread() {
		return Thread.currentThread() == this.gdxThread;
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
