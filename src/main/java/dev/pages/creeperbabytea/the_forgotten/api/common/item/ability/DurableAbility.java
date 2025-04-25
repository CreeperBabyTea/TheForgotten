package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.DurableMarker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public abstract class DurableAbility<M extends DurableMarker<?>> extends ActiveAbility {
    private final int interval;
    private final int duration;

    public DurableAbility(int maxLvl, int coolDownTicks, int duration, int interval) {
        super(maxLvl, coolDownTicks);
        this.duration = duration;
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * 玩家第一次获得这个技能时。也就是说，若玩家本来没有启动该技能，则这个技能启动时会触发这个方法。<br>
     */
    public CompoundTag apply(int lvl, LivingEntity user, float reScaling) {
        return new CompoundTag();
    }

    public void tick(LivingEntity player, M marker) {
    }

    public void expire(LivingEntity player, M marker) {
    }
}
