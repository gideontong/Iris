package net.coderbot.iris.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;

@Mixin(WorldRenderer.class)
public interface InvokerWorldRenderer {
	@Invoker("setupTerrain")
	public void terrainSetup(Camera camera, Frustum frustum, boolean hasForcedFrustum, int frame, boolean spectator);
}
