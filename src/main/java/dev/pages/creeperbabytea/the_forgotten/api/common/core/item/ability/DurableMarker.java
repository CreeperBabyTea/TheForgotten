package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability;

import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.DurableAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public abstract class DurableMarker<A extends DurableAbility<?>> {
    private final int lvl;
    private final float reScaling;
    private final CompoundTag extraInfo;

    protected DurableMarker(int lvl, float reScaling, CompoundTag extraInfo) {
        this.lvl = lvl;
        this.reScaling = reScaling;
        this.extraInfo = extraInfo;
    }

    /**
     * @return <code>false</code>如果技能结束。
     */
    public abstract boolean tick(A ability, LivingEntity entity);

    public CompoundTag getExtraInfo() {
        return extraInfo;
    }

    public int getLvl() {
        return lvl;
    }

    public float getReScaling() {
        return reScaling;
    }
}
