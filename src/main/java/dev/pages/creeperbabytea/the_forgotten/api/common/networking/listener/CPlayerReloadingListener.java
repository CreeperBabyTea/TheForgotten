package dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener;

import dev.pages.creeperbabytea.common.networking.PacketListener;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets.SPlayerReloadingPacket;
import dev.pages.creeperbabytea.the_forgotten.init.Misc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CPlayerReloadingListener extends PacketListener<SPlayerReloadingPacket> {
    @Override
    public void call(SPlayerReloadingPacket packet, IPayloadContext ctx) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (!(localPlayer == ctx.player()))
            return;

        localPlayer.setData(Misc.ENTITY_ATTACHMENTS, packet.attachments());
    }

    public void init(IEventBus mod, IEventBus game) {
        game.addListener(this::onPlayerLoggedIn);
        game.addListener(this::onPlayerChangingDimension);
    }

    public void onPlayerChangingDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sendPack((ServerPlayer) event.getEntity());
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendPack((ServerPlayer) event.getEntity());
    }

    public static void sendPack(ServerPlayer player) {
        TheForgotten.NETWORKING.sendToPlayer(player, new SPlayerReloadingPacket(player.getData(Misc.ENTITY_ATTACHMENTS)));
    }
}
