package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.StackableMarker;
import net.minecraft.world.entity.LivingEntity;

/**
 * 创建一个默认可以叠加的技能。该技能的冷却从启动该技能起算，冷却结束后，无论技能持续时间是否结束，可以再次释放技能，叠加层数。<br>
 * 套层数后，每层叠加会单独计算持续时间。<br>
 */
public class StackableAbility extends DurableAbility<StackableMarker> {
    private final int maxTier;

    public StackableAbility(int maxLvl, int coolDownTicks, int duration, int interval, int maxTier) {
        super(maxLvl, coolDownTicks, duration, interval);
        this.maxTier = maxTier;
    }

    @Override
    public void activate(int lvl, LivingEntity user, float reScaling) {
        var markers = AbilityMarkers.from(user);
        if (markers.isActivated(this))
            markers.tierStack(this, user);
        else
            markers.applyStackable(this, user, lvl, reScaling);
    }

    @Override
    public void postActivate(int lvl, LivingEntity entity, float reScaling, int finalManaCost, int expectedManaCost) {
        super.postActivate(lvl, entity, reScaling, finalManaCost, expectedManaCost);
    }

    /**
     * 套层数时。也就是说，当玩家已经启动了该技能，且技能尚未结束，再次启动技能套层数会触发该方法。<br>
     */
    public void tierStack(LivingEntity user, StackableMarker marker) {
    }

    public int getMaxTier() {
        return maxTier;
    }
}
