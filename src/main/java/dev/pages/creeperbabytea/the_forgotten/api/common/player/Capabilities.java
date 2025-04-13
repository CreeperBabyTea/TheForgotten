package dev.pages.creeperbabytea.the_forgotten.api.common.player;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class Capabilities {
    public static final EntityCapability<IPlayerCapability, Void> PLAYER_CAPABILITY = EntityCapability.createVoid(TheForgotten.modLoc("base_capabilities"), IPlayerCapability.class);

    public static void init(IEventBus mod, IEventBus game) {
        mod.addListener(Capabilities::onCapability);
        game.addListener(Capabilities::onPlayerCloned);
    }

    private static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            IPlayerCapability oldCap = event.getOriginal().getCapability(PLAYER_CAPABILITY);
            IPlayerCapability newCap = event.getEntity().getCapability(PLAYER_CAPABILITY);

            if (oldCap != null && newCap != null) {
                newCap.deserializeNBT(oldCap.serializeNBT());
            }
        }
    }

    private static void onCapability(RegisterCapabilitiesEvent event) {
        event.registerEntity(PLAYER_CAPABILITY, EntityType.PLAYER, new PlayerCapabilityProvider());
    }
}
