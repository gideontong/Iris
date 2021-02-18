package net.coderbot.iris.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.RunArgs;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public interface AccessorGameRenderer {
	@Invoker("getFov")
	public double fovGet(Camera camera, float tickDelta, boolean changingFov);
}
