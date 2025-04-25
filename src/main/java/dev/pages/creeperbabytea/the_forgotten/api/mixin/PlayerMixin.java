package dev.pages.creeperbabytea.the_forgotten.api.mixin;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.player.MainHandItemChangedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Shadow
    private ItemStack lastItemInMainHand;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void injectTick(Player instance) {
        instance.resetAttackStrengthTicker();
        TheForgotten.GAME.post(new MainHandItemChangedEvent(instance, lastItemInMainHand, instance.getMainHandItem()));
    }
}
