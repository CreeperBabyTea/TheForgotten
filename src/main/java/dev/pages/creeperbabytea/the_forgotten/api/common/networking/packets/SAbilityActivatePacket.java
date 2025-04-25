package dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets;

import dev.pages.creeperbabytea.client.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.common.networking.IPacket;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SAbilityActivatePacket(ActiveAbility ability, int lvl, float reScaling, int finalManaCost, int expectedManaCost) implements IPacket<SAbilityActivatePacket> {
    public static final ResourceLocation TYPE = TheForgotten.modLoc("ability_activate");
    public static final StreamCodec<ByteBuf, SAbilityActivatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ActiveAbility.CODEC),
            SAbilityActivatePacket::ability,
            ByteBufCodecs.INT,
            SAbilityActivatePacket::lvl,
            ByteBufCodecs.FLOAT,
            SAbilityActivatePacket::reScaling,
            ByteBufCodecs.INT,
            SAbilityActivatePacket::finalManaCost,
            ByteBufCodecs.INT,
            SAbilityActivatePacket::expectedManaCost,
            SAbilityActivatePacket::new
    );
    public static final PacketProvider<SAbilityActivatePacket> PROVIDER = new PacketProvider.ToClient<>(TYPE, STREAM_CODEC);

    @Override
    public PacketProvider<SAbilityActivatePacket> provider() {
        return PROVIDER;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PROVIDER.getType();
    }
}
