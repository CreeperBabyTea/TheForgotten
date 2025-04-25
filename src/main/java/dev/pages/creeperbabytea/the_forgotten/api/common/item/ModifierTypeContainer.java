package dev.pages.creeperbabytea.the_forgotten.api.common.item;

import net.minecraft.nbt.CompoundTag;

public abstract class ModifierTypeContainer<T extends ModifierTypeContainer<T>> implements TooltipProvider {
    protected final CompoundTag tag;

    protected ModifierTypeContainer(CompoundTag baseTag) {
        var key = getKey();
        if (baseTag.contains(key, 10))
            this.tag = baseTag.getCompound(key);
        else {
            tag = new CompoundTag();
            baseTag.put(key, tag);
        }
    }

    protected abstract void load();

    protected abstract String getKey();
}
