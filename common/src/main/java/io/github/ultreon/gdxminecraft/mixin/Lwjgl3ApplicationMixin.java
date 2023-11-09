package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.utils.Disposable;
import io.github.ultreon.gdxminecraft.GdxMinecraft;
import io.github.ultreon.gdxminecraft.impl.MinecraftGdxLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Lwjgl3Application.class)
public abstract class Lwjgl3ApplicationMixin implements Disposable {
    @Shadow public abstract void setApplicationLogger(ApplicationLogger applicationLogger);

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;initializeGlfw()V"))
    private void gdx_minecraft$redirectInit$initializeGLFW(ApplicationListener listener, Lwjgl3ApplicationConfiguration config) {

    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;setApplicationLogger(Lcom/badlogic/gdx/ApplicationLogger;)V"))
    private void gdx_minecraft$redirectInit$setAppLogger(Lwjgl3Application instance, ApplicationLogger logger) {
        this.setApplicationLogger(new MinecraftGdxLogger());
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;cleanup()V"))
    private void gdx_minecraft$redirectInit$cleanup(Lwjgl3Application instance) {

    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;cleanupWindows()V"))
    private void gdx_minecraft$redirectInit$cleanupWindows(Lwjgl3Application instance) {

    }

    /**
     * @author XyperCode
     * @reason Minecraft initializes the window already. Need to make it wrapped instead.
     */
    @Overwrite(remap = false)
    void createWindow(Lwjgl3Window window, Lwjgl3ApplicationConfiguration config, long sharedContext) {
//        long windowHandle = createGlfwWindow(config, sharedContext);
        ((Lwjgl3WindowAccessor)window).invokeCreate(GdxMinecraft.window.getHandle());
//        window.setVisible(config.initialVisible);

//        for (int i = 0; i < 2; i++) {
//            GL11.glClearColor(config.initialBackgroundColor.r, config.initialBackgroundColor.g, config.initialBackgroundColor.b,
//                    config.initialBackgroundColor.a);
//            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//            GLFW.glfwSwapBuffers(windowHandle);
//        }
    }

    /**
     * @author XyperCode
     * @reason Minecraft already initializes the GLFW window.
     */
    @Overwrite(remap = false)
    static long createGlfwWindow(Lwjgl3ApplicationConfiguration config, long sharedContextWindow) {
        throw new UnsupportedOperationException("Minecraft already initializes the GLFW window.");
    }
}
