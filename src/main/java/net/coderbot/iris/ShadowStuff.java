package net.coderbot.iris;

import static net.coderbot.iris.pipeline.ShaderPipeline.shadowframe;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.ARBFramebufferObject.glFramebufferTexture2D;
import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

import com.mojang.blaze3d.platform.GlStateManager;
import net.coderbot.iris.gl.framebuffer.GlFramebuffer;
import net.coderbot.iris.layer.GbufferProgram;
import net.coderbot.iris.layer.GbufferPrograms;
import net.coderbot.iris.mixin.InvokerWorldRenderer;
import net.coderbot.iris.rendertarget.RenderTargets;
import net.coderbot.iris.uniforms.CapturedRenderingState;
import net.coderbot.iris.uniforms.CelestialUniforms;
import net.coderbot.iris.uniforms.SystemTimeUniforms;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;

public class ShadowStuff {
	public static float halfShadow = 160.0F;
	public static int stuff = 0;
	static EmptyFrustum frustum = new EmptyFrustum();
	static int framecount = new SystemTimeUniforms.FrameCounter().getAsInt();
	public static void shadowStart(Camera camera, GameRenderer renderer, MatrixStack matrices, float tickDelta)
	{
		shadowframe = new GlFramebuffer();
		Iris.getPipeline().beginWorldRender();
		if(stuff == 0) {

			stuff = 1;
		}
		else
		{
			//Iris.logger.warn("pos" + CelestialUniforms.getShadowLightPosition());
			frustum.setPosition(camera.getPos().x, camera.getPos().y, camera.getPos().z + CelestialUniforms.getShadowLightPosition().getZ());
		}
		//camera.
		GlStateManager.disableCull();
		ClientWorld world = MinecraftClient.getInstance().world;
		glEnable(GL_DEPTH_TEST);
		shadowframe.addDepthAttachment(RenderTargets.getShadowTexture().getTextureId());
		//shadowframe.bindo();
		glClear(GL_DEPTH_BUFFER_BIT);
		shadowframe.bind();
		//MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, RenderTargets.getShadowTexture().getTextureId(), 0);
		GL32.glReadBuffer(GL11.GL_NONE);
		GL32.glDrawBuffer(GL11.GL_NONE);
		matrices.push();
		GbufferPrograms.push(GbufferProgram.SHADOW);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
		matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(world.getSkyAngle(tickDelta) * 360.0F));
		matrices.translate(0, 100, 0);
		CapturedRenderingState.INSTANCE.setShadowModelView(matrices.peek().getModel());
		matrices.pop();
		//GL31.glActiveTexture(4);
		//GL31.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, 0, 0, 4096, 4096, 0);
		//GL31.glActiveTexture(0);
		//ShadowProject.setupSecondCamera();

		//Matrix4f projectshadow = gameRenderer.getBasicProjectionMatrix(camera2, tickDelta, true);
		GL32.glDepthMask(true);


		//GL32.glViewport(0, 0, 4096, 4096);

		glPushMatrix();
		GL32.glMatrixMode(GL11.GL_PROJECTION);
		GL32.glLoadIdentity();
		GL32.glOrtho((double)(-halfShadow), (double) halfShadow, (double)(-halfShadow), (double) halfShadow, 0.05000000074505806D, 256.0D);
		//CapturedRenderingState.INSTANCE.setShadowProjection(Matrix4f.projectionMatrix(1, 1, 0, 0));
		GlStateManager.bindTexture(RenderTargets.getShadowTexture().getTextureId());
		GL20C.glCopyTexImage2D(GL20C.GL_TEXTURE_2D, 0, GL20C.GL_DEPTH_COMPONENT, 0, 0, 4096, 4096, 0);
		glPopMatrix();

		//renderer.renderWorld(tickDelta, 1, matrices);
		WorldRenderer worldRenderer = MinecraftClient.getInstance().worldRenderer;
		MinecraftClient.getInstance().chunkCullingEnabled = false;

		((InvokerWorldRenderer) worldRenderer).terrainSetup(camera, frustum, false, framecount, MinecraftClient.getInstance().player.isSpectator());

		//worldRenderer.renderlayer



		//GL32.glOrtho((double)(-halfShadowMapPlane), (double)halfShadowMapPlane, (double)(-halfShadowMapPlane), (double)halfShadowMapPlane, 1D, 1.0D);
		//GL32.glOrtho(-1, 1, -1, 1, -1, 1);
		//MinecraftClient.getInstance().options.setPerspective(Perspective.FIRST_PERSON);
		glDisable(GL_DEPTH_TEST);
		GbufferPrograms.pop(GbufferProgram.SHADOW);
		//ShaderPipeline.baseline.bind();

		shadowframe.destroy();
	}
}
