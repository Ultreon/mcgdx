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

package dev.ultreon.mcgdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.mojang.blaze3d.platform.Window;
import dev.ultreon.mcgdx.api.Gdx3DRenderSource;
import dev.ultreon.mcgdx.api.McGdx;
import dev.ultreon.mcgdx.api.ModLoader;
import dev.ultreon.mcgdx.api.NamespaceID;
import dev.ultreon.mcgdx.impl.*;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.earlygrey.shapedrawer.ShapeDrawer;

@ApiStatus.Internal
@SuppressWarnings("GDXJavaStaticResource")
public class GdxMinecraft implements ApplicationListener {
    public static final String MOD_ID = "mcgdx";
    public static final Logger LOGGER = LoggerFactory.getLogger("GDX-Minecraft");

    public static final Vector3f pos = new Vector3f();
    public static final AxisAngle4f rotation = new AxisAngle4f();
    public static final Vector3 tmp = new Vector3();
    public static Color fogColor = new Color();
    public static final ColorAttribute FOG = ColorAttribute.createFog(fogColor);

    public static boolean disableCubemapUsage = false;

    private static GdxMinecraft instance;
    private static Model cube;
    private static ModelInstance cubeInstance;
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

    private GdxMinecraft() {
        instance = this;
        try {
            GdxNativesLoader.load();
            GdxMinecraft.app = new MinecraftApplication(this, new MinecraftApplicationConfiguration(), Minecraft.getInstance());
            McGdx.blockEntityManager = new MinecraftBlockEntityManager();

            McGdx.blockEntityManager.register(new NamespaceID("mcgdx", "example"), GdxMinecraft::renderExample);
        } catch (Throwable e) {
            CrashReport libGDXCrash = new CrashReport("LibGDX failed to initialize", e);
            CrashReportCategory libGDX = libGDXCrash.addCategory("LibGDX");
            libGDX.setDetail("LibGDX Version", Version.VERSION);
            Minecraft.crash(libGDXCrash);
        }
    }

//    public static void beginCubeFBO() {
//        cubemapFBO.begin();
//    }

//    public static void endCubeFBO() {
//        cubemapFBO.end();
//    }

    public static GdxMinecraft instance() {
        return instance;
    }

    public static String toVert150(String vert120) {
        vert120 = vert120.replace("\nattribute ", "\nin ");
        vert120 = vert120.replace(" attribute ", " in ");

        vert120 = vert120.replace("\nvarying ", "\nout ");
        vert120 = vert120.replace(" varying ", " out ");

        vert120 = vert120.replace("texture2D(", "texture(");

        return vert120;
    }

    public static String toFrag150(String frag120) {
        frag120 = frag120.replace("\nattribute ", "\nout ");
        frag120 = frag120.replace(" attribute ", " out ");

        frag120 = frag120.replace("\nvarying ", "\nin ");
        frag120 = frag120.replace(" varying ", " in ");

        if (frag120.contains("gl_FragColor")) {
            frag120 = frag120.replace("void main()",
                    "out vec4 fragColor; \nvoid main()");
            frag120 = frag120.replace("gl_FragColor", "fragColor");
        }

        frag120 = frag120.replace("texture2D(", "texture(");
        frag120 = frag120.replace("textureCube(", "texture(");

        return frag120;
    }

    public static final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                          + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                          + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                          + "uniform mat4 u_projTrans;\n" //
                          + "varying vec4 v_color;\n" //
                          + "varying vec2 v_texCoords;\n" //
                          + "\n" //
                          + "void main()\n" //
                          + "{\n" //
                          + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                          + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                          + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                          + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                          + "}\n";
    public static final String fragmentShader = "#ifdef GL_ES\n" //
                            + "#define LOWP lowp\n" //
                            + "precision mediump float;\n" //
                            + "#else\n" //
                            + "#define LOWP \n" //
                            + "#endif\n" //
                            + "varying LOWP vec4 v_color;\n" //
                            + "varying vec2 v_texCoords;\n" //
                            + "uniform sampler2D u_texture;\n" //
                            + "void main()\n"//
                            + "{\n" //
                            + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                            + "}";

    public static void initialize() {
        batch = new SpriteBatch(1000, new ShaderProgram(toVert150(toVert150(vertexShader)), toFrag150(fragmentShader)));
        Pixmap whitePix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePix.drawPixel(0, 0, 0xffffffff);
        Texture white = new Texture(whitePix, true);
        shapeRenderer = new ShapeDrawer(batch, new TextureRegion(white));
        texture = new Texture(new Pixmap(Gdx.files.internal("test.png")), true);
        gdxThread = Thread.currentThread();
        bitmapFont = new BitmapFont(true);

        Material material = new Material("box_id");
        material.set(ColorAttribute.createDiffuse(new Color(1, 1, 1, 1)));
        ModelBuilder modelBuilder = new ModelBuilder();
        cube = modelBuilder.createSphere(1, 1, 1, 50, 50, GL20.GL_TRIANGLES, material, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(0.5f, 0.5f, 0.5f);
    }

    public static void init() {
        if (instance == null) {
            instance = new GdxMinecraft();
        }
    }

    private static void renderExample(Gdx3DRenderSource<?> source) {
        ModelBatch batch1 = source.getBatch();
        batch1.render(cubeInstance, source.getEnvironment());
    }

    @Override
    public void create() {
        Gdx.app.log("GDX-Minecraft", "LibGDX version: " + Version.VERSION);
        GdxMinecraft.initialize();
    }

    public void resize0(int width, int height) {
        if (batch == null) return;
        Gdx.app.log("GDX-Minecraft", "Resizing to " + width + "x" + height);

        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho(0, width, height, 0, -1000000, 1000000));

        this.width = width;
        this.height = height;

//        if (cubemapFBO != null) cubemapFBO.dispose();
//        cubemapFBO = new FrameBufferCubemap(Pixmap.Format.RGBA8888, width, height, true);
        long windowHandle = window.getWindowHandle();
        if (Boolean.TRUE.equals(Configuration.GLFW_CHECK_THREAD0.get(true))) {
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
        app.getGraphics();
    }

    @Override
    public void render() {
        if (GdxMinecraft.app == null) return;
        if (GdxMinecraft.window == null) {
            LOGGER.warn("Window not initialized yet! This is probably not a good sign!");
            return;
        }
        Window window = GdxMinecraft.window.getWindow();
        int width = window.getWidth();
        int height = window.getHeight();
        if (width != this.width || height != this.height) {
            this.resize0(width, height);

            this.width = width;
            this.height = height;
        }
    }

    @Override
    public void pause() {
        // Nothing
    }

    @Override
    public void resume() {
        // Nothing
    }

    @Override
    public void dispose() {
        batch.dispose();
        cube.dispose();
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
