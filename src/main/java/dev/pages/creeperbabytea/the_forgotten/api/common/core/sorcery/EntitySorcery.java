package dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import net.minecraft.world.entity.LivingEntity;

public record EntitySorcery(InnerThoughts innerThoughts, Mana mana) implements ISorcery<EntitySorcery, EntitySorcery.TemporaryBonusBundle> {
    public static final Codec<EntitySorcery> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            InnerThoughts.CODEC.fieldOf("inner_thoughts").forGetter(EntitySorcery::innerThoughts),
            Mana.CODEC.fieldOf("mana").forGetter(EntitySorcery::mana)
    ).apply(instance, EntitySorcery::new));

    public EntitySorcery() {
        this(new InnerThoughts(), new Mana());
    }

    @Override
    public void tick() {
        innerThoughts.tick();
        mana.tick();
    }

    @Override
    public void applyTempBonus(TemporaryBonusBundle bonus) {
        this.mana.applyTempBonus(bonus.mana);
        this.innerThoughts.applyTempBonus(bonus.innerThoughts);
    }

    @Override
    public void removeTempBonus(TemporaryBonusBundle bonus) {
        this.mana.removeTempBonus(bonus.mana);
        this.innerThoughts.removeTempBonus(bonus.innerThoughts);
    }

    public static EntitySorcery from(LivingEntity owner) {
        return SorceryAttachments.from(owner).sorcery();
    }

    public static class TemporaryBonusBundle implements ITemporaryBonus<TemporaryBonusBundle> {
        public static final Codec<TemporaryBonusBundle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Mana.TemporaryBonus.CODEC.fieldOf("mana").forGetter(m -> m.mana),
                InnerThoughts.TemporaryBonus.CODEC.fieldOf("inner_thoughts").forGetter(m -> m.innerThoughts)
        ).apply(instance, TemporaryBonusBundle::new));

        private final Mana.TemporaryBonus mana;
        private final InnerThoughts.TemporaryBonus innerThoughts;

        public TemporaryBonusBundle(Mana.TemporaryBonus mana, InnerThoughts.TemporaryBonus innerThoughts) {
            this.mana = mana;
            this.innerThoughts = innerThoughts;
        }

        public TemporaryBonusBundle(EntitySorcery sorcery) {
            this(sorcery.mana.getTemporaryBonus(), sorcery.innerThoughts.getTemporaryBonus());
        }

        public TemporaryBonusBundle() {
            this(new Mana.TemporaryBonus(), new InnerThoughts.TemporaryBonus());
        }

        @Override
        public TemporaryBonusBundle merge(TemporaryBonusBundle bonus) {
            this.mana.merge(bonus.mana);
            this.innerThoughts.merge(bonus.innerThoughts);
            return this;
        }

        @Override
        public TemporaryBonusBundle split(TemporaryBonusBundle bonus) {
            this.mana.split(bonus.mana);
            this.innerThoughts.split(bonus.innerThoughts);
            return this;
        }

        @Override
        public TemporaryBonusBundle copy() {
            return new TemporaryBonusBundle().merge(this);
        }

        @Override
        public boolean isEmpty() {
            return this.innerThoughts.isEmpty() && this.mana.isEmpty();
        }
    }
}
