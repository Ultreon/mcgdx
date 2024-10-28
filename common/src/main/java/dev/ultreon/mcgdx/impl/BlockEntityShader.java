package dev.ultreon.mcgdx.impl;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sun.jna.platform.win32.GL;
import dev.ultreon.mcgdx.mixin.accessors.LightTextureAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.NotNull;

import static dev.ultreon.mcgdx.impl.MinecraftRendererContext.packedLight;

public class BlockEntityShader extends DefaultShader {
    private final int u_mcLight;
    private final int u_mcLightTexture;

    public BlockEntityShader(final Renderable renderable) {
        this(renderable, new Config());
    }

    public BlockEntityShader(final Renderable renderable, final Config config) {
        this(renderable, config, createPrefix(renderable, config));
    }

    public BlockEntityShader(final Renderable renderable, final Config config, final String prefix) {
        this(renderable, config, prefix, config.vertexShader != null ? config.vertexShader : getDefaultVertexShader(),
                config.fragmentShader != null ? config.fragmentShader : getDefaultFragmentShader());
    }

    public BlockEntityShader(final Renderable renderable, final Config config, final String prefix, final String vertexShader,
                             final String fragmentShader) {
        this(renderable, config, new ShaderProgram(prefix + vertexShader, prefix + fragmentShader));
    }

    public BlockEntityShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
        super(renderable, config, shaderProgram);

        LightTexture lightTexture = Minecraft.getInstance().gameRenderer.lightTexture();
        GLTexture glTexture = getGlTexture((LightTextureAccessor) lightTexture);

        this.u_mcLight = register(new Uniform("u_mcLight"), new LocalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                shader.set(inputID, (float) LightTexture.block(packedLight), (float) LightTexture.sky(packedLight));
            }
        });

        this.u_mcLightTexture = register(new Uniform("u_mcLightTexture"), new GlobalSetter() {
            @Override
            public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
                shader.set(inputID, glTexture);
            }
        });
    }

    private static @NotNull GLTexture getGlTexture(LightTextureAccessor lightTexture) {
        LightTextureAccessor accessor = lightTexture;
        DynamicTexture dynamicTexture = accessor.getLightTexture();

        int id = dynamicTexture.getId();
        // Unreloadable
        return new GLTexture(GL20.GL_TEXTURE_2D, id) {
            @Override
            public int getWidth() {
                return 16;
            }

            @Override
            public int getHeight() {
                return 16;
            }

            @Override
            public int getDepth() {
                return 32;
            }

            @Override
            public boolean isManaged() {
                return false;
            }

            @Override
            protected void reload() {
                // Unreloadable
            }
        };
    }

    public static String createPrefix(final Renderable renderable, final Config config) {
        return "#version 150\n" + DefaultShader.createPrefix(renderable, config);
    }
}
