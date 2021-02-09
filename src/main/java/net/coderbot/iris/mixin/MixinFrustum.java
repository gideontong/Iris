package net.coderbot.iris.mixin;


import static net.coderbot.iris.pipeline.ShaderPipeline.frustumOff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.Vector4f;

@Mixin(Frustum.class)
public class MixinFrustum {
	@Overwrite
	public boolean isVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

		return true;
	}
	public boolean isAnyCornerVisible(float x1, float y1, float z1, float x2, float y2, float z2) {

		return true;
	}
}
