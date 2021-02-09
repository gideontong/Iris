package net.coderbot.iris;

import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
//import net.minecraft.util.math.Vec3d;

public class EmptyFrustum extends Frustum {
	public EmptyFrustum() {
		super(new Matrix4f(), new Matrix4f());
	}

	public boolean isVisible(Box b) {
		return true;
	}
}
