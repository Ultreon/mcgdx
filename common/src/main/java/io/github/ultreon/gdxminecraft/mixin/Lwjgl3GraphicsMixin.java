package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.utils.Disposable;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(Lwjgl3Graphics.class)
public abstract class Lwjgl3GraphicsMixin implements Disposable {
    @Shadow @Final
    Lwjgl3Window window;
    @Shadow
    IntBuffer tmpBuffer;
    @Shadow
    IntBuffer tmpBuffer2;
    @Shadow private volatile int backBufferWidth;
    @Shadow private volatile int backBufferHeight;
    @Shadow private volatile int logicalWidth;
    @Shadow private volatile int logicalHeight;
    @Shadow private Graphics.BufferFormat bufferFormat;

    @Shadow private int fps;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetFramebufferSizeCallback(JLorg/lwjgl/glfw/GLFWFramebufferSizeCallbackI;)Lorg/lwjgl/glfw/GLFWFramebufferSizeCallback;"), remap = false)
    private GLFWFramebufferSizeCallback gdx_minecraft$injectInit(long window, GLFWFramebufferSizeCallbackI cbfun) {
        return new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                // Do nothing lmao
            }
        };
    }

    /**
     * @author XyperCode
     * @reason 
     */
    @Overwrite(remap = false)
    void updateFramebufferInfo() {
        GLFW.glfwGetFramebufferSize(window.getWindowHandle(), tmpBuffer, tmpBuffer2);
        this.backBufferWidth = tmpBuffer.get(0);
        this.backBufferHeight = tmpBuffer2.get(0);
        GLFW.glfwGetWindowSize(window.getWindowHandle(), tmpBuffer, tmpBuffer2);
        logicalWidth = tmpBuffer.get(0);
        logicalHeight = tmpBuffer2.get(0);
        bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 16, 16, false);
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the FPS, so we just get the fps from the Minecraft instance.
     */
    @Overwrite(remap = false)
    public int getFramesPerSecond() {
        return Minecraft.getInstance().getFps();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the width from Minecraft.
     */
    @Overwrite(remap = false)
    public int getWidth() {
        return Minecraft.getInstance().getWindow().getWidth();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the height from Minecraft.
     */
    @Overwrite(remap = false)
    public int getHeight() {
        return Minecraft.getInstance().getWindow().getHeight();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the width from Minecraft.
     */
    @Overwrite(remap = false)
    public int getBackBufferWidth() {
        return Minecraft.getInstance().getWindow().getWidth();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the height from Minecraft.
     */
    @Overwrite(remap = false)
    public int getBackBufferHeight() {
        return Minecraft.getInstance().getWindow().getHeight();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the scaled width from Minecraft.
     */
    @Overwrite(remap = false)
    public int getLogicalWidth() {
        return Minecraft.getInstance().getWindow().getWidth();
    }

    /**
     * @author XyperCode
     * @reason Minecraft manages the application, so we just get the scaled height from Minecraft.
     */
    @Overwrite(remap = false)
    public int getLogicalHeight() {
        return Minecraft.getInstance().getWindow().getHeight();
    }
}
