package dev.pages.creeperbabytea.the_forgotten.items.abilities;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.UnstackableMarker;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.UnstackableAbility;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

public class OverflowAbility extends UnstackableAbility {
    public OverflowAbility() {
        super(3, 0, 30, 1);
        this.setManaCostRaw(5);
        //this.setManaCostPercentage(0.3f);
    }

    @Override
    public CompoundTag apply(int lvl, LivingEntity user, float reScaling) {
        var mana = Mana.from(user);
        mana.setBlockManaConsume(true);
        return super.apply(lvl, user, reScaling);
    }

    @Override
    public void expire(LivingEntity user, UnstackableMarker marker) {
        int healthFlown = getHealthFlown(user);
        int manaFlown = getManaFlown(user);
        var mana = Mana.from(user);
        mana.setBlockManaConsume(false);
        if (!(user instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.isCreative())) {
            if (healthFlown > 0) {
                if (!user.level().isClientSide())
                    user.hurtServer((ServerLevel) user.level(), user.level().damageSources().magic(), healthFlown);
                mana.clearMana();
            } else
                mana.addMana(-manaFlown);
        }
    }

    @Override
    public boolean hasCustomListeners() {
        return true;
    }

    @Override
    public void registerCustomListeners(IEventBus mod, IEventBus game) {
        game.addListener(EventPriority.LOWEST, this::onOtherAbilityActivated);
    }

    public void onOtherAbilityActivated(ActivateAbilityEvent.Post event) {
        LivingEntity user = event.getEntity();
        var markers = AbilityMarkers.from(user);
        if (markers.isActivated(this)) {
            var marker = markers.getUnstackableMarker(this);
            var extraInfo = marker.getExtraInfo();
            var manaFlown = extraInfo.getInt("mana_flown");
            extraInfo.putInt("mana_flown", manaFlown + event.getExpectedManaCost());
        }
    }

    public int getManaFlown(LivingEntity player) {
        var markers = AbilityMarkers.from(player);
        if (markers.isActivated(this)) {
            var marker = markers.getUnstackableMarker(this);
            var extraInfo = marker.getExtraInfo();
            return extraInfo.getInt("mana_flown");
        }
        return 0;
    }

    public int getHealthFlown(LivingEntity player) {
        var manaFlown = getManaFlown(player);
        var mana = Mana.from(player);
        return manaFlown > mana.getMaxMana() ? (manaFlown - mana.getMaxMana()) / 5 : 0;
    }
}
