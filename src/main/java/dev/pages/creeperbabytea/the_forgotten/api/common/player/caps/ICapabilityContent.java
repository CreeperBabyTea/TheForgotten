package dev.pages.creeperbabytea.the_forgotten.api.common.player.caps;

import net.minecraft.nbt.CompoundTag;

public interface ICapabilityContent<CC extends ICapabilityContent<?>> {
    /**
     * Returns the nbt data of this capacity content.
     * @return The nbt tag that includes all the necessary information about this instance.
     */
    CompoundTag serializeNBT();

    /**
     * Loads the content from a known nbt tag
     * @return The content instance, or "this".
     */
    CC deserializeNBT(CompoundTag nbt);
}
