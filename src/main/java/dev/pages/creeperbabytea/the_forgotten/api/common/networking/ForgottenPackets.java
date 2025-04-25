package dev.pages.creeperbabytea.the_forgotten.api.common.networking;

import dev.pages.creeperbabytea.common.networking.Networking;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets.SAbilityActivatePacket;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets.SPlayerReloadingPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;

public class ForgottenPackets {
    private static final Networking INSTANCE = TheForgotten.NETWORKING;

    public static void init(IEventBus mod, IEventBus game) {
        INSTANCE.init(mod, game);
    }

    public static final PacketProvider<SAbilityActivatePacket> S_ABILITY_ACTIVATE = register(SAbilityActivatePacket.PROVIDER);
    public static final PacketProvider<SPlayerReloadingPacket> S_PLAYER_RELOADING = register(SPlayerReloadingPacket.PROVIDER);

    private static <P extends CustomPacketPayload> PacketProvider<P> register(PacketProvider<P> pp) {
        INSTANCE.registerPack(pp);
        return pp;
    }
}
