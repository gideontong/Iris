package net.coderbot.iris.uniforms;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;

public class CapturedRenderingState {
	public static final CapturedRenderingState INSTANCE = new CapturedRenderingState();

	private Matrix4f gbufferModelView;
	private Matrix4f gbufferProjection;
	private Matrix4f shadowProjection;
	private Matrix4f shadowModelView;
	private float tickDelta;
	private BlockEntity currentRenderedBlockEntity;
	private Entity currentRenderedEntity;

	private CapturedRenderingState() {
	}

	public Matrix4f getGbufferModelView() {
		return gbufferModelView;
	}

	public void setGbufferModelView(Matrix4f gbufferModelView) {
		this.gbufferModelView = gbufferModelView.copy();
	}

	public Matrix4f getGbufferProjection() {
		return gbufferProjection;
	}

	public void setGbufferProjection(Matrix4f gbufferProjection) {
		this.gbufferProjection = gbufferProjection.copy();
	}

	public Matrix4f getShadowProjection() {
		return shadowProjection;
	}

	public void setShadowProjection(Matrix4f shadowProjection) {
		this.shadowProjection = shadowProjection.copy();
	}

	public Matrix4f getShadowModelView() {
		return shadowModelView;
	}

	public void setShadowModelView(Matrix4f shadowModelView) {
		this.shadowModelView = shadowModelView.copy();
	}

	public void setTickDelta(float tickDelta) {
		this.tickDelta = tickDelta;
	}

	public float getTickDelta() {
		return tickDelta;
	}

	public void setCurrentBlockEntity(BlockEntity entity) {
		this.currentRenderedBlockEntity = entity;
	}

	public BlockEntity getCurrentRenderedBlockEntity() {
		return currentRenderedBlockEntity;
	}

	public void setCurrentEntity(Entity entity) {
		this.currentRenderedEntity = entity;
	}

	public Entity getCurrentRenderedEntity() {
		return currentRenderedEntity;
	}
}
