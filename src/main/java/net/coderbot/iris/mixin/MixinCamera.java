package net.coderbot.iris.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public class MixinCamera {
	//@Invoker(setPos)
	//protected void setPos(double x, double y, double z);
}
