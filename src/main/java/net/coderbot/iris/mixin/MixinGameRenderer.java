package net.coderbot.iris.mixin;

import static net.coderbot.iris.ShadowProject.camera2;

import net.coderbot.iris.gui.TransparentBackgroundScreen;
import net.coderbot.iris.uniforms.SystemTimeUniforms;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinGameRenderer {
	@Shadow @Final private MinecraftClient client;
	//private final LightmapTextureManager lightmapTextureManager = new LightmapTextureManager(client.gameRenderer, client);;
	//Matrix4f matrix4f;
	//MinecraftClient client = MinecraftClient.getInstance();


	@Inject(method = "render(FJZ)V", at = @At("HEAD"))
	private void iris$beginFrame(float tickDelta, long startTime, boolean tick, CallbackInfo callback) {
		SystemTimeUniforms.TIMER.beginFrame(startTime);
	}

	//@Inject(method = "renderWorld", at = @At("TAIL"))
	//private void iris$camerastuff(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo callback)
	//{
	//	this.client.worldRenderer.render(matrix, tickDelta, limitTime, true, camera2, client.gameRenderer, this.lightmapTextureManager, matrix4f);
	//}


	@Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
	public void handleTransparentGui(CallbackInfoReturnable<Boolean> cir) {
		if(this.client.currentScreen instanceof TransparentBackgroundScreen && !((TransparentBackgroundScreen)this.client.currentScreen).renderHud()) cir.setReturnValue(false);
	}



}
