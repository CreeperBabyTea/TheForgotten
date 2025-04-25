package dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery;

public interface ITemporaryBonus<T extends ITemporaryBonus<T>> {
    T merge(T bonus);

    T split(T bonus);

    T copy();

    boolean isEmpty();
}
