package dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery;

public interface ISorcery<CC extends ISorcery<?, ?>, T extends ITemporaryBonus<T>> {
    void tick();

    void applyTempBonus(T bonus);

    void removeTempBonus(T bonus);
}
