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
import io.github.ultreon.gdxminecraft.api.GdxScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GdxTestScreen extends GdxScreen {
    public static final Texture TEXTURE = new Texture(Gdx.files.internal("test.png"));
    public static final Mesh MESH = createFullScreenQuad();
    public static final ShaderProgram SHADER = new ShaderProgram(DefaultShader.getDefaultVertexShader(), DefaultShader.getDefaultFragmentShader());
    public static final Renderable RENDERABLE = new Renderable();

    public GdxTestScreen(Screen screen) {
        super(Component.literal("Gdx TEST"));
    }

    @Override
    public void render(ShapeRenderer shapeDrawer, Batch batch, int i, int j, float f) {
//        shapeDrawer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeDrawer.setColor(1, 0, 0, 1);
//        shapeDrawer.rect(-50, -50, width + 100, height + 100);
//        shapeDrawer.end();
//        Gdx.gl.glFlush();
        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        batch.begin();
        batch.draw(TEXTURE, 0, 0, width, height);
        batch.end();

//        TEXTURE.bind();
//        MESH.render(SHADER, GL20.GL_TRIANGLES);
    }
    public static Mesh createFullScreenQuad() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(1)), GL20.GL_TRIANGLES);
        builder.rect(new Vector3(0, 0, 0), new Vector3(200, 0, 0), new Vector3(200, 200, 0), new Vector3(0, 200, 0), new Vector3(0, 0, 0));
        return builder.end();
    }
}
