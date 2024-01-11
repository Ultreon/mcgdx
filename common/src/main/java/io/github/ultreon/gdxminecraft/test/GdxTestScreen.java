package io.github.ultreon.gdxminecraft.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.api.GdxScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GdxTestScreen extends GdxScreen {
    public final Texture texture;
    public static final Mesh MESH = createFullScreenQuad();
    public static final ShaderProgram SHADER = new ShaderProgram(DefaultShader.getDefaultVertexShader(), DefaultShader.getDefaultFragmentShader());
    public static final Renderable RENDERABLE = new Renderable();

    public GdxTestScreen(Screen screen) {
        super(Component.literal("Gdx TEST"));

        this.texture = GdxMinecraft.instance().getTexture();
    }

    @Override
    public void render(ShapeDrawer shapeDrawer, Batch batch, int i, int j, float f) {
        super.render(shapeDrawer, batch, i, j, f);

        // Always call begin() at the beginning of a batch.
        batch.begin();

        // Draw a rectangle using the shape drawer.
        shapeDrawer.setColor(1, 1, 3, 8);
        shapeDrawer.filledCircle(0, 0, 250, Color.SCARLET);

        // Draw hello world text using font.
        this.font.draw(batch, "Hello World!", 10, 10);

        // Always call end() at the end of a batch.
        batch.end();
    }
    public static Mesh createFullScreenQuad() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0)), GL20.GL_TRIANGLES);
        builder.rect(new Vector3(0, 0, 0), new Vector3(200, 0, 0), new Vector3(200, 200, 0), new Vector3(0, 200, 0), new Vector3(0, 0, 0));
        return builder.end();
    }
}
