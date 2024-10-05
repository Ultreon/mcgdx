/*
 * Copyright (c) 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ultreon.mcgdx.mixin;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
//    @Unique
//    private final PerspectiveCamera mcgdx$cam = new PerspectiveCamera();
//    @Unique
//    private final Vector3f mcgdx$pos = new Vector3f();
//    @Unique
//    private final AxisAngle4f mcgdx$rotation = new AxisAngle4f();
//    @Unique
//    private final Vector3 mcgdx$tmp = new Vector3();

    @WrapMethod(method = "renderLevel")
    public void mcgdx$wrapLevelRenderForWorldCubeMap(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Operation<Void> original) {
//        FrameBufferCubemap frameBuffer = GdxMinecraft.cubemapFBO;
//
//        GdxMinecraft.disableCubemapUsage = true;
//        frameBuffer.begin();
//        PoseStack.Pose pose = poseStack.last();
//        pose.pose().getTranslation(mcgdx$pos);
//
//        mcgdx$cam.fieldOfView = (float) ((GameRendererAccessor) Minecraft.getInstance().gameRenderer).invokeGetFov(camera, f, true);
//        mcgdx$cam.viewportWidth = Gdx.graphics.getWidth();
//        mcgdx$cam.viewportHeight = Gdx.graphics.getHeight();
//
//        mcgdx$cam.near = 0.05f;
//        mcgdx$cam.far = Minecraft.getInstance().gameRenderer.getDepthFar();
//
//        mcgdx$cam.position.set(-mcgdx$pos.x, -mcgdx$pos.y, -mcgdx$pos.z);
//        mcgdx$cam.direction.set(0, 0, -1);
//        mcgdx$cam.up.set(0, 1, 0);
//
//        pose.pose().getRotation(mcgdx$rotation);
//        mcgdx$cam.rotateAround(Vector3.Zero, mcgdx$tmp.set(mcgdx$rotation.x, mcgdx$rotation.y, mcgdx$rotation.z), mcgdx$rotation.angle);
//
//        float aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
//        mcgdx$cam.projection.setToProjection(Math.abs(mcgdx$cam.near), Math.abs(mcgdx$cam.far), mcgdx$cam.fieldOfView, aspect);
//        mcgdx$cam.view.setToTranslation(mcgdx$pos.x, mcgdx$pos.y, mcgdx$pos.z).rotateRad(mcgdx$rotation.x, mcgdx$rotation.y, mcgdx$rotation.z, mcgdx$rotation.angle);
//        mcgdx$cam.combined.set(mcgdx$cam.projection);
//        Matrix4.mul(mcgdx$cam.combined.val, mcgdx$cam.view.val);
//
//        mcgdx$cam.invProjectionView.set(mcgdx$cam.combined);
//        Matrix4.inv(mcgdx$cam.invProjectionView.val);
//        mcgdx$cam.frustum.update(mcgdx$cam.invProjectionView);
//
//        while (frameBuffer.nextSide()) {
//            frameBuffer.getSide().getUp(mcgdx$cam.up);
//            frameBuffer.getSide().getDirection(mcgdx$cam.direction);
//            mcgdx$cam.update();
//            Gdx.gl.glClearColor(0, 0, 0, 1);
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//
//            original.call(poseStack, f, l, bl, camera, gameRenderer, lightTexture, matrix4f);
//        }
//        frameBuffer.end();
//        GdxMinecraft.disableCubemapUsage = false;
        original.call(poseStack, f, l, bl, camera, gameRenderer, lightTexture, matrix4f);

//        GdxMinecraft.cubeMap = frameBuffer.getColorBufferTexture();
    }
}
