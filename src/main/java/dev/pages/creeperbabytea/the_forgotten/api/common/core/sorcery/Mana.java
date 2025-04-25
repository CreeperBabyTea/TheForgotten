package dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class Mana implements ISorcery<Mana, Mana.TemporaryBonus> {
    public static final Codec<Mana> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("mana").forGetter(Mana::getMana),
            Codec.BOOL.fieldOf("mana_regen_blocked").forGetter(Mana::isManaRegenBlocked),
            Codec.BOOL.fieldOf("mana_consume_blocked").forGetter(Mana::isManaConsumeBlocked),
            Codec.INT.fieldOf("tail_tick").forGetter(m -> m.tailTick),
            TemporaryBonus.CODEC.optionalFieldOf("bonus").forGetter(Mana::getOptionalTemporaryBonus)
    ).apply(instance, (mana1, blockManaRegen1, blockManaConsume1, tailTick1, optionalTemporaryBonus) -> new Mana(mana1, blockManaRegen1, blockManaConsume1, tailTick1, optionalTemporaryBonus.orElse(new TemporaryBonus()))));

    private int maxMana = 100;
    private float manaRegen = 0.5f;
    private int mana = 100;
    private boolean blockManaRegen = false;
    private boolean blockManaConsume = false;
    private int tailTick = 0;

    private final TemporaryBonus temporaryBonus;

    public Mana() {
        this.temporaryBonus = new TemporaryBonus();
    }

    private Mana(int mana, boolean blockManaRegen, boolean blockManaConsume, int tailTick, TemporaryBonus bonus) {
        this.mana = mana;
        this.blockManaRegen = blockManaRegen;
        this.blockManaConsume = blockManaConsume;
        this.tailTick = tailTick;
        this.temporaryBonus = bonus;
    }

    private Mana loadIT(InnerThoughts it) {
        this.maxMana = it.getIntellect() * 2;
        this.manaRegen = it.getRationality() * 0.01f;
        return this;
    }

    @Override
    public void tick() {
        if (mana > getMaxMana()) {
            mana--;
        } else {
            if (getManaRegen() > 0)
                if (!(isManaRegenBlocked() || isManaFull()))
                    applyManaRegen();
            if (getManaRegen() < 0)
                if (!(isManaConsumeBlocked() || isManaEmpty()))
                    applyManaRegen();
        }
    }

    private void applyManaRegen() {
        float rawRegen = getManaRegen();
        int main = (int) (rawRegen > 0 ? rawRegen : rawRegen - 1);
        if (main > 0) {
            if (main + getMana() <= getMaxMana())
                addMana(main);
            else
                fullMana();
        } else {
            if (main + getMana() >= 0)
                addMana(main);
            else clearMana();
        }
        float tail = rawRegen - main;
        int regenTick = tailTick;
        if (Math.abs(tail * regenTick) >= 1f) {
            if (mana < getMaxMana())
                addMana(1);
            tailTick = 0;
        } else
            tailTick++;
    }

    public int getMaxMana() {
        return maxMana + temporaryBonus.maxMana;
    }

    public float getManaRegen() {
        return manaRegen + temporaryBonus.manaRegen;
    }

    public int getMana() {
        return mana;
    }

    public void setBlockManaConsume(boolean blockManaConsume) {
        this.blockManaConsume = blockManaConsume;
    }

    public void setBlockManaRegen(boolean blockManaRegen) {
        this.blockManaRegen = blockManaRegen;
    }

    public boolean isManaConsumeBlocked() {
        return blockManaConsume;
    }

    public boolean isManaRegenBlocked() {
        return blockManaRegen;
    }

    public boolean isManaFull() {
        return mana >= maxMana;
    }

    public boolean isManaEmpty() {
        return mana == 0;
    }

    public void addMaxMana(int maxMana) {
        this.maxMana += maxMana;
    }

    public void addManaRegen(float manaRegen) {
        this.manaRegen += manaRegen;
    }

    public void setMana(int mana) {
        if (mana >= 0)
            this.mana = mana;
    }

    public void fullMana() {
        this.mana = getMaxMana();
    }

    public void clearMana() {
        this.mana = 0;
    }

    public TemporaryBonus getTemporaryBonus() {
        return temporaryBonus;
    }

    @Nullable
    public Optional<TemporaryBonus> getOptionalTemporaryBonus() {
        return temporaryBonus.isEmpty() ? Optional.empty() : Optional.of(temporaryBonus);
    }

    @Override
    public void applyTempBonus(TemporaryBonus bonus) {
        temporaryBonus.merge(bonus);
    }

    @Override
    public void removeTempBonus(TemporaryBonus bonus) {
        temporaryBonus.split(bonus);
    }

    /**
     * 可能超过蓝量上线，超上限的部分会逐渐消退。
     * @param amount 可以是正的或负的或0.
     */
    public void addMana(int amount) {
        if (amount > 0 && !blockManaRegen)
            mana += amount;
        if (amount < 0 && !blockManaConsume) {
            if (mana + amount < 0)
                mana = 0;
            else
                mana += amount;
        }
    }

    public static Mana from(LivingEntity entity) {
        return EntitySorcery.from(entity).mana();
    }

    public static class TemporaryBonus implements ITemporaryBonus<TemporaryBonus> {
        public static final Codec<TemporaryBonus> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("max_mana").forGetter(m -> m.maxMana),
                Codec.FLOAT.fieldOf("regen").forGetter(m -> m.manaRegen)
        ).apply(instance, TemporaryBonus::new));

        private int maxMana;
        private float manaRegen;

        public TemporaryBonus(int maxMana, float manaRegen) {
            this.maxMana = maxMana;
            this.manaRegen = manaRegen;
        }

        public TemporaryBonus() {
            this(0, 0);
        }

        public TemporaryBonus merge(TemporaryBonus bonus) {
            this.maxMana += bonus.maxMana;
            this.manaRegen += bonus.manaRegen;
            return this;
        }

        public TemporaryBonus split(TemporaryBonus bonus) {
            this.maxMana -= bonus.maxMana;
            this.manaRegen -= bonus.manaRegen;
            return this;
        }

        @Override
        public TemporaryBonus copy() {
            return new TemporaryBonus().merge(this);
        }

        @Override
        public boolean isEmpty() {
            return manaRegen == 0 && maxMana == 0;
        }
    }
}
