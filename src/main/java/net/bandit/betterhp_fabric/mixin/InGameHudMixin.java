package net.bandit.betterhp_fabric.mixin;

import net.bandit.betterhp_fabric.config.ConfigManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusBars(CallbackInfo ci) {
        // Check if the vanilla HUD should be rendered
        if (!ConfigManager.renderVanillaHud()) {
            ci.cancel(); // Cancel rendering of the vanilla HUD
        }
    }
}
