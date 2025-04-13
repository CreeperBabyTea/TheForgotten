package dev.pages.creeperbabytea.the_forgotten.api.common.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public interface ISimpleNbtSerializable<T extends Tag> {
    T serializeNBT();

    void deserializeNBT(T tag);
}
