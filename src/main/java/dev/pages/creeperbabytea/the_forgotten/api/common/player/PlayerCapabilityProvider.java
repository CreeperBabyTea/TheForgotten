package dev.pages.creeperbabytea.the_forgotten.api.common.player;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;


public class PlayerCapabilityProvider implements ICapabilityProvider<Entity, Void, IPlayerCapability> {
    IPlayerCapability cap;

    @Nullable
    @Override
    public IPlayerCapability getCapability(Entity object, @Nullable Void context) {
        if (cap == null)
            return cap = new PlayerCapability();
        return cap;
    }
}
