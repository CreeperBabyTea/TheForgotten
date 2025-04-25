package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.UnstackableAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class UnstackableMarker extends DurableMarker<UnstackableAbility> {
    public static final Codec<UnstackableMarker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("duration").forGetter(UnstackableMarker::getLvl),
            Codec.INT.fieldOf("lvl").forGetter(UnstackableMarker::getLvl),
            Codec.FLOAT.fieldOf("rescaling").forGetter(UnstackableMarker::getReScaling),
            CompoundTag.CODEC.fieldOf("extra_info").forGetter(UnstackableMarker::getExtraInfo)
    ).apply(instance, UnstackableMarker::new));

    private int duration;

    protected UnstackableMarker(int duration, int lvl, float reScaling, CompoundTag extraInfo) {
        super(lvl, reScaling, extraInfo);
        this.duration = duration;
    }

    public static UnstackableMarker create(UnstackableAbility ability, LivingEntity player, int lvl, float reScaling) {
        return new UnstackableMarker(ability.getDuration(), lvl, reScaling, ability.apply(lvl, player, reScaling));
    }

    @Override
    public boolean tick(UnstackableAbility ability, LivingEntity entity) {
        if (ability.getDuration() - duration % ability.getInterval() == 0)
            ability.tick(entity, this);

        duration --;
        if (duration <= 0) {
            ability.expire(entity, this);
            return false;
        }
        return true;
    }

    public int getDuration() {
        return duration;
    }
}
