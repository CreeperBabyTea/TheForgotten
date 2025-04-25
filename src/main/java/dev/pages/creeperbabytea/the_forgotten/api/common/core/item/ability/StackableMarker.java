package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.StackableAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class StackableMarker extends DurableMarker<StackableAbility> {
    public static final Codec<StackableMarker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.INT).fieldOf("tiers").forGetter(StackableMarker::getTierDurations),
            Codec.INT.fieldOf("lvl").forGetter(StackableMarker::getLvl),
            Codec.FLOAT.fieldOf("rescaling").forGetter(StackableMarker::getReScaling),
            CompoundTag.CODEC.fieldOf("extra_info").forGetter(StackableMarker::getExtraInfo)
    ).apply(instance, StackableMarker::new));

    private final List<Tier> tierDurations;
    private int currentTier;
    private int tailTicks = 0;

    protected StackableMarker(int duration, int lvl, float reScaling, CompoundTag extraInfo) {
        this(List.of(duration), lvl, reScaling, extraInfo);
    }

    private StackableMarker(List<Integer> tierDurations, int lvl, float reScaling, CompoundTag extraInfo) {
        super(lvl, reScaling, extraInfo);
        this.tierDurations = new ArrayList<>(tierDurations.stream().map(Tier::new).toList());
        this.currentTier = tierDurations.size();
    }

    public static StackableMarker create(StackableAbility ability, LivingEntity player, int lvl, float reScaling) {
        return new StackableMarker(ability.getDuration(), lvl, reScaling, ability.apply(lvl, player, reScaling));
    }

    public void tierStack(StackableAbility ability, LivingEntity player) {
        if (currentTier < ability.getMaxTier()) {
            tierDurations.add(new Tier(ability.getDuration()));
            ability.tierStack(player, this);
            currentTier ++;
        }
    }

    @Override
    public boolean tick(StackableAbility ability, LivingEntity entity) {
        if (tailTicks % ability.getInterval() == 0) {
            ability.tick(entity, this);
            tailTicks = 0;
        } else
            tailTicks ++;

        tierDurations.forEach(t -> t.duration--);
        Tier tier = tierDurations.getFirst();
        while (tier.duration <= 0) {
            tierDurations.removeFirst();
            tier = tierDurations.getFirst();
            currentTier--;
        }
        if (tierDurations.isEmpty()) {
            ability.expire(entity, this);
            return false;
        }
        return true;
    }

    public List<Integer> getTierDurations() {
        return tierDurations.stream().map(t -> t.duration).toList();
    }

    public int getCurrentTier() {
        return currentTier;
    }

    private static class Tier {
        int duration;

        private Tier(int duration) {
            this.duration = duration;
        }
    }
}
