package net.coderbot.iris;

import static net.coderbot.iris.postprocess.PostProcessUniformsRest.SHADOW_TEX_0;

import com.mojang.blaze3d.platform.GlStateManager;
import net.coderbot.iris.gl.framebuffer.GlFramebuffer;
import net.coderbot.iris.mixin.InvokerCamera;
import net.coderbot.iris.pipeline.ShaderPipeline;
import net.coderbot.iris.uniforms.CapturedRenderingState;
import net.coderbot.iris.uniforms.CelestialUniforms;
import org.lwjgl.opengl.GL30;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBlendState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;

public class ShadowProject {
	public static Camera camera2 = new Camera();
	public static Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
	float x = CelestialUniforms.getShadowLightPosition().getX();
	float y = CelestialUniforms.getShadowLightPosition().getY();
	float z = CelestialUniforms.getShadowLightPosition().getZ();
	public static Camera getCamera() {
		return camera2;
	}
	public static void setupSecondCamera() {
		float x = CelestialUniforms.getShadowLightPosition().getX();
		float y = CelestialUniforms.getShadowLightPosition().getY();
		float z = CelestialUniforms.getShadowLightPosition().getZ();
		//camera2.setPosi(new Vec3d(x, y, z));
		((InvokerCamera) camera2).posSet(new Vec3d(x, y, z));
		//((InvokerCamera) camera).posSet(new Vec3d(x, y, z));

		//((InvokerCamera) camera).posSet(new Vec3d(camera.getPos().x, camera.getPos().y + 20, camera.getPos().z));
		//Iris.logger.warn("camera pos: " + camera.getPos());
		//ShaderPipeline.shadowframe.bind();
		//ShaderPipeline.shadowframe.addDepthAttachment(SHADOW_TEX_0); this causes crash when reloading???
		MinecraftClient client = MinecraftClient.getInstance();
		WorldRenderer worldRenderer = new WorldRenderer(client, client.getBufferBuilders());

		//worldRenderer.
		//camera2.
		//getCamera().setPos(new Vec3d(1, 1, 1));
	}
}
