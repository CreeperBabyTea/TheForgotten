package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.UnstackableMarker;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import net.minecraft.world.entity.LivingEntity;

public class UnstackableAbility extends DurableAbility<UnstackableMarker> {
    public UnstackableAbility(int maxLvl, int coolDownTicks, int duration, int interval) {
        super(maxLvl, coolDownTicks, duration, interval);
    }

    @Override
    protected void applyCooldown(LivingEntity user) {
        if (!noCooldown()) {
            var markers = AbilityMarkers.from(user);
            markers.applyCooldown(this, getCoolDownTicks() + getDuration());
        } else {
            var markers = AbilityMarkers.from(user);
            markers.applyCooldown(this, getDuration());
        }
    }

    @Override
    public void canActivate(ActivateAbilityEvent.Pre event) {
        var markers = AbilityMarkers.from(event.getEntity());
        if (markers.isActivated(this))
            event.setCanceled(true);
    }

    @Override
    public void activate(int lvl, LivingEntity user, float reScaling) {
        var markers = AbilityMarkers.from(user);
        markers.applyUnstackable(this, user, lvl, reScaling);
    }
}
