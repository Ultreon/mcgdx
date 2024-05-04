package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.MinecraftGdxLogger;
import io.github.ultreon.gdxminecraft.mixin.accessors.Lwjgl3WindowAccessor;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Lwjgl3Application.class)
public abstract class Lwjgl3ApplicationMixin implements Disposable {
    @Unique
    private Lwjgl3Window gdx_minecraft$window;

    @Shadow public abstract void setApplicationLogger(ApplicationLogger applicationLogger);

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;initializeGlfw()V"))
    private void gdx_minecraft$redirectInit$initializeGLFW(ApplicationListener listener, Lwjgl3ApplicationConfiguration config) {
        GdxMinecraft.LOGGER.error("Cleanup called on Lwjgl3Application, this is unsupported behavior!");
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;setApplicationLogger(Lcom/badlogic/gdx/ApplicationLogger;)V"))
    private void gdx_minecraft$redirectInit$setAppLogger(Lwjgl3Application instance, ApplicationLogger logger) {
        this.setApplicationLogger(new MinecraftGdxLogger());
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;cleanup()V"))
    private void gdx_minecraft$redirectInit$cleanup(Lwjgl3Application instance) {
        GdxMinecraft.LOGGER.error("cleanup() called on Lwjgl3Application, this is unsupported behavior!");
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;cleanupWindows()V"))
    private void gdx_minecraft$redirectInit$cleanupWindows(Lwjgl3Application instance) {
        GdxMinecraft.LOGGER.error("cleanupWindows() called on Lwjgl3Application, this is unsupported behavior!");
    }

    /**
     * @author <a href="https://github.com/XyperCode">XyperCode</a>
     * @reason Minecraft initializes the window already. Need to make it wrapped instead.
     */
    @Overwrite(remap = false)
    void createWindow(Lwjgl3Window window, Lwjgl3ApplicationConfiguration config, long sharedContext) {
        if (GdxMinecraft.window == null) return;
        if (this.gdx_minecraft$window != null) throw new UnsupportedOperationException("Window already created!");

        this.gdx_minecraft$window = window;
        GdxMinecraft.window.setGdxWindow(window);
        GdxMinecraft.LOGGER.info("Created new window: " + window);
        ((Lwjgl3WindowAccessor)window).invokeCreate(GdxMinecraft.window.getHandle());

        for (int i = 0; i < 2; i++) {
            GL11.glClearColor(0, 0, 0, 1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GLFW.glfwSwapBuffers(GdxMinecraft.window.getHandle());
        }
    }

    /**
     * @author <a href="https://github.com/XyperCode">XyperCode</a>
     * @reason Minecraft already initializes the GLFW window, and creating multiple windows is unsupported behavior.
     */
    @Overwrite(remap = false)
    static long createGlfwWindow(Lwjgl3ApplicationConfiguration config, long sharedContextWindow) {
        throw new UnsupportedOperationException("Minecraft already initializes the GLFW window.");
    }
}
