package dev.ultreon.mcgdx.mixin;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.ultreon.mcgdx.GdxMinecraft;
import org.spongepowered.asm.mixin.Mixin;

import static dev.ultreon.mcgdx.GdxMinecraft.toFrag150;
import static dev.ultreon.mcgdx.GdxMinecraft.toVert150;

@Mixin(ShaderProgram.class)
public abstract class ShaderProgramMixin {
    @WrapMethod(method = "compileShaders", remap = false)
    private void compileShaders(String vertexShader, String fragmentShader, Operation<Void> original) {
        GdxMinecraft.LOGGER.warn("Mixin injecting!");
        original.call(toVert150(vertexShader), toFrag150(fragmentShader));
    }
}
