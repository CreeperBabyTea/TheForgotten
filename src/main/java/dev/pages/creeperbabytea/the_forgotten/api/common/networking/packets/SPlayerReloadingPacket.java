package dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets;

import dev.pages.creeperbabytea.common.networking.IPacket;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SPlayerReloadingPacket(SorceryAttachments attachments) implements IPacket<SPlayerReloadingPacket> {
    public static final ResourceLocation TYPE = TheForgotten.modLoc("player_joining");
    public static final StreamCodec<ByteBuf, SPlayerReloadingPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(SorceryAttachments.CODEC),
            SPlayerReloadingPacket::attachments,
            SPlayerReloadingPacket::new
    );
    public static final PacketProvider<SPlayerReloadingPacket> PROVIDER = new PacketProvider.ToClient<>(TYPE, STREAM_CODEC);

    @Override
    public PacketProvider<SPlayerReloadingPacket> provider() {
        return PROVIDER;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PROVIDER.getType();
    }
}
