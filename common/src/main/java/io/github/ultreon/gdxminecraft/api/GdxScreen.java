package io.github.ultreon.gdxminecraft.api;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import space.earlygrey.shapedrawer.ShapeDrawer;

@SuppressWarnings("unused")
public class GdxScreen extends Screen {
    private final GdxMinecraft gdx;
    private GuiGraphics mcGfx;
    private ShapeDrawer shapes;
    private Batch batch;
    private Exception failure;
    protected BitmapFont font;

    protected GdxScreen(Component component) {
        super(component);

        this.gdx = GdxMinecraft.instance();
        this.font = this.gdx.getBitmapFont();
    }

    @Override
    @ApiStatus.Internal
    public final void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (!this.gdx.isOnGdxThread()) {
            guiGraphics.fillGradient(0, 0, this.width, this.height, 0xff000000, 0xff200000);
            guiGraphics.drawCenteredString(super.font, "§lRender Failure\n§r§oInvalid Thread", width / 2, height / 2, 0xffffffff);
            return;
        }

        if (this.minecraft == null || this.minecraft.level == null) {
            guiGraphics.fillGradient(0, 0, this.width, this.height, 0xff000000, 0xff200000);
            guiGraphics.drawCenteredString(super.font, "§lRender Failure", width / 2, height / 2 - 11, 0xffffffff);
            guiGraphics.drawCenteredString(super.font, "§oCan't view LibGDX screen outside level rendering.", width / 2, height / 2 + 1, 0xffffffff);
            return;
        }

        if (this.failure != null) {
            guiGraphics.fillGradient(0, 0, this.width, this.height, 0xff000000, 0xff200000);
            guiGraphics.drawCenteredString(super.font, "§lRender Failure", width / 2, height / 2 - 11, 0xffffffff);
            guiGraphics.drawCenteredString(super.font, "§o" + failure.getMessage(), width / 2, height / 2 + 1, 0xffffffff);
            return;
        }

        this.mcGfx = guiGraphics;

        this.mcGfx.pose().pushPose();
        this.mcGfx.pose().translate(0, 0, 1000000);

        ScreenUtils.clear(0, 0, 0, 0);

        GlStateManager._disableDepthTest();
        GlStateManager._disableCull();
        GlStateManager._disableBlend();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        for (int tex = GL20.GL_TEXTURE0; tex <= GL20.GL_TEXTURE31; tex++) {
            RenderSystem.setShaderTexture(tex, GL20.GL_NONE);
        }

        this.shapes = gdx.getShapeDrawer();
        this.shapes.getBatch().flush();
        this.shapes.setPixelSize(2.0f);

        this.batch = gdx.getBatch();

        try {
            this.render(this.shapes, this.batch, i, j, f);
        } catch (Exception e) {
            this.failure = e;
            GdxMinecraft.LOGGER.error("Failed to render LibGDX screen.", e);
        }

        this.mcGfx.pose().popPose();

        GlStateManager._enableBlend();
        GlStateManager._enableCull();
        GlStateManager._enableDepthTest();
    }

    @Override
    public final void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
    }

    @Override
    public final void renderDirtBackground(GuiGraphics guiGraphics) {
        super.renderDirtBackground(guiGraphics);
    }

    @Override
    public final void renderTransparentBackground(GuiGraphics guiGraphics) {
        super.renderTransparentBackground(guiGraphics);
    }

    public void render(ShapeDrawer shapeDrawer, Batch batch, int i, int j, float f) {
        super.render(getMcGfx(), i, j, f); // Minecraft GUI component support.
    }

    GuiGraphics getMcGfx() {
        return mcGfx;
    }
}
