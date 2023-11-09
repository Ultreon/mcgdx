package io.github.ultreon.gdxminecraft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.backends.lwjgl3.audio.Lwjgl3Audio;
import io.github.ultreon.gdxminecraft.api.ModLoader;
import io.github.ultreon.gdxminecraft.impl.MinecraftApplication;
import io.github.ultreon.gdxminecraft.impl.MinecraftGdxLogger;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;

public class GdxMinecraft implements ApplicationListener {
	public static final String MOD_ID = "gdx_minecraft";
	private final Lwjgl3WindowListener windowListener = new UselessWindowListener();
	public static ModLoader loader;
	private final ApplicationLogger logger = new MinecraftGdxLogger();
	public static MinecraftApplication app;
	public static Lwjgl3Input input;
	public static Lwjgl3Audio audio;

	public GdxMinecraft() {
		Gdx.gl = new Lwjgl3GL32();
		Gdx.gl20 = new Lwjgl3GL32();
		Gdx.gl30 = new Lwjgl3GL32();
		Gdx.gl31 = new Lwjgl3GL32();
		Gdx.gl32 = new Lwjgl3GL32();

		Gdx.app = app = new MinecraftApplication(this, Minecraft.getInstance());
		Gdx.input = input = app.createInput(app.getWindow());
		Gdx.audio = audio = app.createAudio(new Lwjgl3ApplicationConfiguration());
		Gdx.files = app.getFiles();
	}

	@ApiStatus.Internal
	public static void init() {
		new GdxMinecraft();
	}

	@Override
	public void create() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {

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
