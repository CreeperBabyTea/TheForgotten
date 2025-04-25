package dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener;

import dev.pages.creeperbabytea.client.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.common.networking.PacketListener;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemInfo;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilityInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SModifiableItemRawMouseInputListener extends PacketListener<CRawMouseInputPacket> {
    public SModifiableItemRawMouseInputListener() {
        super(true);
    }

    @Override
    public void call(CRawMouseInputPacket rawMouseInputPacket, IPayloadContext ctx) {
        if (!rawMouseInputPacket.isPress())
            return;

        ServerPlayer player = (ServerPlayer) ctx.player();

        ItemStack active = null;

        InteractionHand hand = null;
        if (ModifiableItemInfo.isModifiable(player.getMainHandItem().getItem())) {
            active = player.getMainHandItem();
            hand = InteractionHand.MAIN_HAND;
        } else if (ModifiableItemInfo.isModifiable(player.getOffhandItem().getItem()) && ModifiableItemInfo.getInfo(player.getOffhandItem().getItem()).allowedOffhand()) {
            active = player.getOffhandItem();
            hand = InteractionHand.OFF_HAND;
        }

        if (active != null && ModifiersContainer.hasModifiers(active)) {
            AbilitiesContainer.AbilitySlot slot = AbilitiesContainer.AbilitySlot.of(rawMouseInputPacket);
            ModifiersContainer modifiers = ModifiersContainer.of(active);
            AbilityInstance abilityInstance = modifiers.getAbilities().get(slot);
            if (abilityInstance != null) {
                abilityInstance.maybeActivate(player, active, hand);
            }
        }
    }
}
