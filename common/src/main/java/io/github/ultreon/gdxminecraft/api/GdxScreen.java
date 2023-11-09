package io.github.ultreon.gdxminecraft.api;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import space.earlygrey.shapedrawer.ShapeDrawer;

@SuppressWarnings("unused")
public class GdxScreen extends Screen {
    private final GdxMinecraft gdx;
    private GuiGraphics mcGfx;
    private ShapeDrawer shapeRenderer;
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

        this.shapeRenderer = gdx.getShapeDrawer();
//        this.shapeRenderer.getBatch().flush();
//        Matrix4 matrix41 = this.shapeRenderer.getBatch().getProjectionMatrix().setToOrtho(0, this.width, this.height, 0, 0, 1000000);
//        this.shapeRenderer.getBatch().setProjectionMatrix(matrix41);
        this.shapeRenderer.setPixelSize(2.0f);

        this.batch = gdx.getBatch();
//        var matrix4 = this.batch.getProjectionMatrix().cpy().setToOrtho(0, this.width, this.height, 0, -1000000, 1000000).scale(0, 0, -1);
//        this.batch.setProjectionMatrix(matrix4);

        try {
            this.render(this.shapeRenderer, this.batch, i, j, f);
        } catch (Exception e) {
            this.failure = e;
            GdxMinecraft.LOGGER.error("Failed to render LibGDX screen.", e);
        }
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
