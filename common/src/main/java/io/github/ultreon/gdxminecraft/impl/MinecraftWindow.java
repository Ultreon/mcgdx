package io.github.ultreon.gdxminecraft.impl;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.mojang.blaze3d.platform.Window;

public class MinecraftWindow {
    private final Window window;
    private Lwjgl3Window gdxWindow;

    public MinecraftWindow(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public Lwjgl3Window getGdxWindow() {
        return gdxWindow;
    }

    public void setGdxWindow(Lwjgl3Window gdxWindow) {
        this.gdxWindow = gdxWindow;
    }

    public long getHandle() {
        return this.window.getWindow();
    }

    public int getRed() {
        return 24;
    }
}
