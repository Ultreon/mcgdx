package io.github.ultreon.gdxminecraft.mixin;

import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import com.badlogic.gdx.utils.Disposable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DefaultLwjgl3Input.class)
public abstract class DefaultLwjgl3InputMixin implements Disposable {
    @Shadow public abstract void resetPollingStates();

    /**
     * @author XyperCode
     * @reason Minecraft handles LWJGL3 input events by default.
     */
    @Overwrite(remap = false)
    public void windowHandleChanged(long windowHandle) {
        resetPollingStates();
    }
}
