package com.divinitysreach.beaconbeamtoggle.mixin;

import com.divinitysreach.beaconbeamtoggle.BeaconBeamToggleClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin {

    @Inject(
        method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void beaconbeamtoggle$hideBeam(BeaconRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                                           CameraRenderState camera, CallbackInfo ci) {
        if (!BeaconBeamToggleClient.beamsEnabled) {
            ci.cancel();
        }
    }
}
