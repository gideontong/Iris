package net.coderbot.iris.mixin.gui;

//import net.coderbot.iris.gui.option.ShaderPackScreenButtonOption;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//import net.minecraft.client.gui.widget.ButtonListWidget
@Mixin(VideoOptionsScreen.class)
public class MixinVideoOptionsScreen {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/option/Option;)V"))
    public void addShaderPackOptionsButton(ButtonListWidget inst, net.minecraft.client.option.Option[] old) {
        AccessorScreen screen = (AccessorScreen)this;
        Option[] options = new Option[old.length + 1];
        for (int i = 0; i < old.length; i++) options[i] = old[i];
      //  options[options.length - 1] = new ShaderPackScreenButtonOption((VideoOptionsScreen)(Object)this, screen.getClient());
      ///  inst.addAll(options);
    }
}
