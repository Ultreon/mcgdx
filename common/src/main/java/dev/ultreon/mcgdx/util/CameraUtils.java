package dev.ultreon.mcgdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.impl.GdxBlockEntityRenderer;
import dev.ultreon.mcgdx.mixin.accessors.GameRendererAccessor;
import net.minecraft.client.Minecraft;

public class CameraUtils {
    public static void setupCamera(Camera camera, float f, PoseStack poseStack) {
        PoseStack.Pose pose = poseStack.last();
        pose.pose().getTranslation(GdxMinecraft.pos);

        if (camera instanceof PerspectiveCamera perspectiveCamera) {
            perspectiveCamera.fieldOfView = (float) ((GameRendererAccessor) Minecraft.getInstance().gameRenderer).invokeGetFov(Minecraft.getInstance().gameRenderer.getMainCamera(), f, true);
            camera.viewportWidth = Gdx.graphics.getWidth();
            camera.viewportHeight = Gdx.graphics.getHeight();

            GdxMinecraft.FOG.color.set(GdxMinecraft.fogColor);

            GdxBlockEntityRenderer.environment.set(GdxMinecraft.FOG);

            camera.near = 0.05f;
            camera.far = Minecraft.getInstance().gameRenderer.getDepthFar();

            camera.position.set(-GdxMinecraft.pos.x, -GdxMinecraft.pos.y, -GdxMinecraft.pos.z);
            camera.direction.set(0, 0, -1);
            camera.up.set(0, 1, 0);

            pose.pose().getRotation(GdxMinecraft.rotation);
            camera.rotateAround(Vector3.Zero, GdxMinecraft.tmp.set(GdxMinecraft.rotation.x, GdxMinecraft.rotation.y, GdxMinecraft.rotation.z), GdxMinecraft.rotation.angle);

            float aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
            camera.projection.setToProjection(Math.abs(camera.near), Math.abs(camera.far), perspectiveCamera.fieldOfView, aspect);
            camera.view.setToTranslation(GdxMinecraft.pos.x, GdxMinecraft.pos.y, GdxMinecraft.pos.z).rotateRad(GdxMinecraft.rotation.x, GdxMinecraft.rotation.y, GdxMinecraft.rotation.z, GdxMinecraft.rotation.angle);
            camera.combined.set(camera.projection);
            Matrix4.mul(camera.combined.val, camera.view.val);

            camera.invProjectionView.set(camera.combined);
            Matrix4.inv(camera.invProjectionView.val);
            camera.frustum.update(camera.invProjectionView);
        }
        if (camera instanceof OrthographicCamera orthographicCamera) {
            camera.viewportWidth = Gdx.graphics.getWidth();
            camera.viewportHeight = Gdx.graphics.getHeight();

            GdxMinecraft.FOG.color.set(GdxMinecraft.fogColor);

            GdxBlockEntityRenderer.environment.set(GdxMinecraft.FOG);

            camera.near = 0f;
            camera.far = 15000;

            camera.position.set(-GdxMinecraft.pos.x, -GdxMinecraft.pos.y, -GdxMinecraft.pos.z);
            camera.direction.set(0, 0, -1);
            camera.up.set(0, 1, 0);

            pose.pose().getRotation(GdxMinecraft.rotation);
            camera.rotateAround(Vector3.Zero, GdxMinecraft.tmp.set(GdxMinecraft.rotation.x, GdxMinecraft.rotation.y, GdxMinecraft.rotation.z), GdxMinecraft.rotation.angle);

            float aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
            camera.projection.setToOrtho(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), Math.abs(camera.near), Math.abs(camera.far));
            camera.view.setToTranslation(GdxMinecraft.pos.x, GdxMinecraft.pos.y, GdxMinecraft.pos.z).rotateRad(GdxMinecraft.rotation.x, GdxMinecraft.rotation.y, GdxMinecraft.rotation.z, GdxMinecraft.rotation.angle);
            camera.combined.set(camera.projection);
            Matrix4.mul(camera.combined.val, camera.view.val);

            camera.invProjectionView.set(camera.combined);
            Matrix4.inv(camera.invProjectionView.val);
            camera.frustum.update(camera.invProjectionView);
        }
    }
}
