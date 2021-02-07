package net.coderbot.iris.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

@Mixin(Camera.class)
public interface InvokerCamera {
       @Invoker("setPos")
	 void posSet(Vec3d pos);
}
