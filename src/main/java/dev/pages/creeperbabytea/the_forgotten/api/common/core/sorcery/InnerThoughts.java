package dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class InnerThoughts implements ISorcery<InnerThoughts, InnerThoughts.TemporaryBonus> {
    public static final Codec<InnerThoughts> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("soul").forGetter(InnerThoughts::getSoul),
            Codec.INT.fieldOf("rationality").forGetter(InnerThoughts::getRationality),
            Codec.INT.fieldOf("intellect").forGetter(InnerThoughts::getIntellect),
            TemporaryBonus.CODEC.optionalFieldOf("bonus").forGetter(InnerThoughts::getOptionalTemporaryBonus)
    ).apply(instance, (integer, integer1, integer2, temporaryBonus1) -> new InnerThoughts(integer, integer1, integer2, temporaryBonus1.orElse(new TemporaryBonus()))));

    private int soul = 10;
    private int rationality = 50;
    private int intellect = 50;

    private final TemporaryBonus temporaryBonus;

    public InnerThoughts() {
        this.temporaryBonus = new TemporaryBonus();
    }

    private InnerThoughts(int soul, int rationality, int intellect, TemporaryBonus temporaryBonus) {
        this.soul = soul;
        this.rationality = rationality;
        this.intellect = intellect;
        this.temporaryBonus = temporaryBonus;
    }

    public static InnerThoughts from(Player entity) {
        return EntitySorcery.from(entity).innerThoughts();
    }

    public Tag serializeNBT() {
        return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
    }

    public static InnerThoughts deserializeNBT(Tag nbt) {
        return CODEC.parse(NbtOps.INSTANCE, nbt).getOrThrow();
    }

    public int getSoul() {
        return soul;
    }

    public int getRationality() {
        return rationality + temporaryBonus.rationality;
    }

    public int getIntellect() {
        return intellect + temporaryBonus.intellect;
    }

    public void setSoul(int soul) {
        this.soul = soul;
    }

    public void setRationality(int rationality) {
        this.rationality = rationality;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    @Override
    public String toString() {
        return "InnerThoughts{" +
                "soul=" + soul +
                ", rationality=" + rationality +
                ", intellect=" + intellect +
                '}';
    }

    public void tick() {
    }

    @Override
    public void applyTempBonus(TemporaryBonus bonus) {
        temporaryBonus.merge(bonus);
    }

    @Override
    public void removeTempBonus(TemporaryBonus bonus) {
        temporaryBonus.split(bonus);
    }

    public TemporaryBonus getTemporaryBonus() {
        return temporaryBonus;
    }

    public Optional<TemporaryBonus> getOptionalTemporaryBonus() {
        return temporaryBonus.isEmpty() ? Optional.empty() : Optional.of(temporaryBonus);
    }

    public static class TemporaryBonus implements ITemporaryBonus<TemporaryBonus> {
        public static final Codec<TemporaryBonus> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("intellect").forGetter(m -> m.intellect),
                Codec.INT.fieldOf("rationality").forGetter(m -> m.rationality)
        ).apply(instance, TemporaryBonus::new));

        private int intellect, rationality;

        public TemporaryBonus(int intellect, int rationality) {
            this.intellect = intellect;
            this.rationality = rationality;
        }

        public TemporaryBonus() {
            this(0, 0);
        }

        @Override
        public TemporaryBonus merge(TemporaryBonus bonus) {
            this.intellect += bonus.intellect;
            this.rationality += bonus.rationality;
            return this;
        }

        @Override
        public TemporaryBonus split(TemporaryBonus bonus) {
            this.intellect -= bonus.intellect;
            this.rationality -= bonus.rationality;
            return this;
        }

        @Override
        public TemporaryBonus copy() {
            return new TemporaryBonus().merge(this);
        }

        @Override
        public boolean isEmpty() {
            return rationality == 0 && intellect == 0;
        }
    }
}
