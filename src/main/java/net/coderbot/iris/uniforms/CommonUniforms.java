package net.coderbot.iris.uniforms;

import static net.coderbot.iris.gl.uniform.UniformUpdateFrequency.ONCE;
import static net.coderbot.iris.gl.uniform.UniformUpdateFrequency.PER_FRAME;
import static net.coderbot.iris.gl.uniform.UniformUpdateFrequency.PER_TICK;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.function.IntSupplier;

import com.mojang.blaze3d.platform.GlStateManager;
import net.coderbot.iris.gl.uniform.UniformHolder;
import net.coderbot.iris.gl.uniform.UniformUpdateFrequency;
import net.coderbot.iris.shaderpack.IdMap;
import net.coderbot.iris.texunits.TextureUnit;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;

public final class CommonUniforms {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	public static float fogColorR;
	public static float fogColorB;
	public static float fogColorG;
	public static float fogDensitys;
	public static int fogMode;
	private static Vec3f fogColor;
	private CommonUniforms() {
		// no construction allowed
	}

	public static void addCommonUniforms(UniformHolder uniforms, IdMap idMap) {
		CameraUniforms.addCameraUniforms(uniforms);
		ViewportUniforms.addViewportUniforms(uniforms);
		WorldTimeUniforms.addWorldTimeUniforms(uniforms);
		SystemTimeUniforms.addSystemTimeUniforms(uniforms);
		CelestialUniforms.addCelestialUniforms(uniforms);
		IdMapUniforms.addIdMapUniforms(uniforms, idMap);
		MatrixUniforms.addMatrixUniforms(uniforms);
		//enableFog();
		//GL43.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

		uniforms
			.uniform1i(ONCE, "texture", TextureUnit.TERRAIN::getSamplerId)
			.uniform1i(ONCE, "lightmap", TextureUnit.LIGHTMAP::getSamplerId)
			.uniform1b(PER_FRAME, "hideGUI", () -> client.options.hudHidden)
			.uniform1i(ONCE, "noisetex", () -> 15)
			.uniform1f(PER_FRAME, "eyeAltitude", () -> Objects.requireNonNull(client.getCameraEntity()).getEyeY())
			.uniform1i(PER_FRAME, "isEyeInWater", CommonUniforms::isEyeInWater)
			.uniform1f(PER_FRAME, "blindness", CommonUniforms::getBlindness)
			.uniform1i(PER_FRAME, "heldBlockLightValue", new HeldItemLightingSupplier(Hand.MAIN_HAND))
			.uniform1i(PER_FRAME, "heldBlockLightValue2", new HeldItemLightingSupplier(Hand.OFF_HAND))
			.uniform1f(PER_FRAME, "nightVision", CommonUniforms::getNightVision)
			.uniform1f(PER_FRAME, "screenBrightness", () -> client.options.gamma)
			.uniform1f(PER_TICK, "playerMood", CommonUniforms::getPlayerMood)
			.uniform2i(PER_FRAME, "eyeBrightness", CommonUniforms::getEyeBrightness)
			.uniform1f(PER_TICK, "rainStrength", CommonUniforms::getRainStrength)
			.uniform1f(PER_TICK, "wetness", CommonUniforms::getRainStrength)
			.uniform3d(PER_FRAME, "skyColor", CommonUniforms::getSkyColor)
			.uniform3f(PER_FRAME, "fogColor", CommonUniforms::getFogColor)
			.uniform1f(PER_FRAME, "fogDensity", CommonUniforms::getFogDensity)
			.uniform1i(PER_FRAME, "fogMode", CommonUniforms::getFogMode);
	}

	public static Vec3d getSkyColor() {
		if (client.world == null || client.cameraEntity == null) {
			return Vec3d.ZERO;
		}

		return client.world.method_23777(Vec3d.ofCenter(client.cameraEntity.getBlockPos()), CapturedRenderingState.INSTANCE.getTickDelta());
	}

	public static Vec3f getFogColor(){
		IntBuffer fogColor = BufferUtils.createIntBuffer(4);
		fogColor.put((byte) fogColorR).put((byte) fogColorG).put((byte) fogColorB).put((byte) 1.0).flip();
		GL11.glFogiv(GL11.GL_FOG_COLOR, fogColor);
		GL43.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		fogMode = GL_LINEAR;
		Vec3f fogColors = new Vec3f(fogColorR, fogColorG, fogColorB);
		//Iris.logger.warn("fogColors is" + fogColors);
		return fogColors;

	}
	public static void enableFog(){
		GlStateManager.enableDepthTest();
		GlStateManager.enableFog();
		GL11.glEnable(GL_FOG);
		//fogEnabled = true;
	}

	public static void setFogMode(int pname, int param) {
		GL11.glFogi(pname, param);
		if (pname == 2917) {
			fogMode = param;
		//	if (fogEnabled) {
			//}
		}

	}

	//public static Vector3f fogColor;
	public static int getFogMode() {
		//if(isRaining()){
		//	fogMode = GL_EXP;
		//}
		return fogMode;
	}


	public static void setFogColor(float r, float g, float b)
	{
		fogColorR = r;
		fogColorG = g;
		fogColorB = b;
		//Iris.logger.warn("fogColor is" + getFogColor());
		IntBuffer fogColor = BufferUtils.createIntBuffer(4);
		fogColor.put((byte) fogColorR).put((byte) fogColorG).put((byte) fogColorB).put((byte) 1.0).flip();
		GL11.glFogiv(GL11.GL_FOG_COLOR, fogColor);

	}


	public static float getFogDensity() {
		//if (fogEnabled) {
			fogDensitys = 0.0F;
			if (client.world.isRaining()) {
				fogDensitys = 1.0F;
			}
			return fogDensitys;
		//}
		//else {
		//	return 0;
	//	}
	}

	public static void setFogDensity(float value) {
		GL11.glFogf(GL_FOG_DENSITY, value);
		fogDensitys = value;
		//if (fogEnabled) {

		//}
		//else {

		//}
	}


	private static float getBlindness() {
		Entity cameraEntity = client.getCameraEntity();

		if (cameraEntity instanceof LivingEntity) {
			StatusEffectInstance blindness = ((LivingEntity) cameraEntity).getStatusEffect(StatusEffects.BLINDNESS);

			if (blindness != null) {
				// Guessing that this is what OF uses, based on how vanilla calculates the fog value in BackgroundRenderer
				// TODO: Add this to ShaderDoc
				return Math.min(1.0F, blindness.getDuration() / 20.0F);
			}
		}

		return 0.0F;
	}

	private static float getPlayerMood() {
		if (!(client.cameraEntity instanceof ClientPlayerEntity)) {
			return 0.0F;
		}

		return ((ClientPlayerEntity)client.cameraEntity).getMoodPercentage();
	}

	private static float getRainStrength() {
		if (client.world == null) {
			return 0f;
		}

		return client.world.getRainGradient(CapturedRenderingState.INSTANCE.getTickDelta());
	}

	private static Vec2f getEyeBrightness() {
		if (client.cameraEntity == null || client.world == null) {
			return Vec2f.ZERO;
		}
		int blockLight = client.world.getLightLevel(LightType.BLOCK, client.cameraEntity.getBlockPos());
		int skyLight = client.world.getLightLevel(LightType.SKY, client.cameraEntity.getBlockPos());
		return new Vec2f(blockLight, skyLight);
	}

	private static float getNightVision() {
		Entity cameraEntity = client.getCameraEntity();

		if (cameraEntity instanceof LivingEntity) {

			LivingEntity livingEntity = (LivingEntity) cameraEntity;

			if (livingEntity.getStatusEffect(StatusEffects.NIGHT_VISION) != null) {

				return GameRenderer.getNightVisionStrength(livingEntity, CapturedRenderingState.INSTANCE.getTickDelta());
			}
		}

		return 0.0F;
	}

	private static int isEyeInWater() {
		CameraSubmersionType submergedFluid = client.gameRenderer.getCamera().getSubmersionType();

		if (submergedFluid == CameraSubmersionType.WATER) {
			return 1;
		} else if (submergedFluid == CameraSubmersionType.LAVA) {
			return 2;
		} else {
			return 0;
		}
	}

	private static class HeldItemLightingSupplier implements IntSupplier {

		private final Hand hand;

		private HeldItemLightingSupplier(Hand targetHand) {
			this.hand = targetHand;
		}

		@Override
		public int getAsInt() {
			if (client.player == null) {
				return 0;
			}

			ItemStack stack = client.player.getStackInHand(hand);

			if (stack == ItemStack.EMPTY || stack == null || !(stack.getItem() instanceof BlockItem)) {
				return 0;
			}

			BlockItem item = (BlockItem) stack.getItem();

			return item.getBlock().getDefaultState().getLuminance();
		}
	}

}
