package io.github.ultreon.gdxminecraft.api;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
public class GdxScreen extends Screen {
    private final GdxMinecraft gdx;
    private GuiGraphics mcGfx;

    protected GdxScreen(Component component) {
        super(component);

        this.gdx = GdxMinecraft.instance();
    }

    @Override
    @ApiStatus.Internal
    public final void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (!this.gdx.isOnGdxThread()) {
            guiGraphics.fillGradient(0, 0, width, height, 0xff000000, 0xff200000);
            guiGraphics.drawCenteredString(this.font, "§lRender Failure: §r§oInvalid Thread", width / 2, height / 2, 0xffffffff);
            return;
        }

        this.mcGfx = guiGraphics;
        this.render(gdx.getShapeDrawer(), gdx.getBatch(), i, j, f);
    }

    @Override
    public final void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
    }

    @Override
    public final void renderDirtBackground(GuiGraphics guiGraphics) {
//        super.renderDirtBackground(guiGraphics);
    }

    @Override
    public final void renderTransparentBackground(GuiGraphics guiGraphics) {
        super.renderTransparentBackground(guiGraphics);
    }

    public void render(ShapeRenderer shapeDrawer, Batch batch, int i, int j, float f) {
        super.render(getMcGfx(), i, j, f); // Minecraft GUI component support.
    }

    GuiGraphics getMcGfx() {
        return mcGfx;
    }
}
