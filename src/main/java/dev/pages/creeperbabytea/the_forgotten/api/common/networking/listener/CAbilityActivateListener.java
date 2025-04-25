package dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener;

import dev.pages.creeperbabytea.common.networking.PacketListener;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets.SAbilityActivatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CAbilityActivateListener extends PacketListener<SAbilityActivatePacket> {
    @Override
    public void call(SAbilityActivatePacket packet, IPayloadContext ctx) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (!(localPlayer == ctx.player()))
            return;

        TheForgotten.GAME.post(new ActivateAbilityEvent.Post(packet.ability(), packet.lvl(), localPlayer, packet.reScaling(), packet.finalManaCost(), packet.expectedManaCost()));
    }
}
