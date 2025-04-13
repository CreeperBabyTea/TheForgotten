package dev.pages.creeperbabytea.the_forgotten.api.common.player;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.player.caps.ICapabilityContent;
import dev.pages.creeperbabytea.the_forgotten.api.common.player.caps.InnerThoughts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayerCapability implements IPlayerCapability{
    public static final ResourceLocation INNER_THOUGHTS = TheForgotten.modLoc("inner_thoughts");

    private InnerThoughts innerThoughts = new InnerThoughts();
    private final Map<ResourceLocation, ICapabilityContent<?>> additionalCapabilities = new HashMap<>();

    @Override
    public InnerThoughts getInnerThoughts() {
        return innerThoughts;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag base = new CompoundTag();
        base.put(INNER_THOUGHTS.toString(), innerThoughts.serializeNBT());
        return base;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.innerThoughts = new InnerThoughts().deserializeNBT(nbt.getCompound(INNER_THOUGHTS.toString()));
    }

    @Override
    public Map<ResourceLocation, ICapabilityContent<?>> getAdditionalCapabilities() {
        return additionalCapabilities;
    }
}
