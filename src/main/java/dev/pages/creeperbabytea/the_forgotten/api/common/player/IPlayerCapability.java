package dev.pages.creeperbabytea.the_forgotten.api.common.player;

import dev.pages.creeperbabytea.the_forgotten.api.common.nbt.ISimpleNbtSerializable;
import dev.pages.creeperbabytea.the_forgotten.api.common.player.caps.ICapabilityContent;
import dev.pages.creeperbabytea.the_forgotten.api.common.player.caps.InnerThoughts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

public interface IPlayerCapability extends ISimpleNbtSerializable<CompoundTag> {
    InnerThoughts getInnerThoughts();

    default Map<ResourceLocation, ICapabilityContent<?>> getAdditionalCapabilities() {
        return Collections.emptyMap();
    }

    @Nullable
    default ICapabilityContent<?> getCapability(ResourceLocation cap) {
        Map<ResourceLocation, ICapabilityContent<?>> contentMap = getAdditionalCapabilities();
        return contentMap.getOrDefault(cap, null);
    }
}
