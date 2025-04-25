package dev.pages.creeperbabytea.the_forgotten.api.mixin;

import dev.pages.creeperbabytea.the_forgotten.init.Misc;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        Profiler.get().push("sorcery");
        var living = (LivingEntity) (Object) (this);
        var attachments = living.getData(Misc.ENTITY_ATTACHMENTS);
        attachments.tick(living);
        Profiler.get().pop();
    }
}
