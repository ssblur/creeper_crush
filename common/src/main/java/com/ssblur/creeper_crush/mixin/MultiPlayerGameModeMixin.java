package com.ssblur.creeper_crush.mixin;

import com.ssblur.creeper_crush.helper.ReplaceInteractHelper;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "interact", at = @At(value = "HEAD", shift = At.Shift.AFTER, by = 1), cancellable = true)
    private void creeperCrush$interact(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        var self = (MultiPlayerGameMode) (Object) this;
        if(self.getPlayerMode() == GameType.SPECTATOR) return;
        if(ReplaceInteractHelper.INSTANCE.replaceInteract(player, entity)) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
