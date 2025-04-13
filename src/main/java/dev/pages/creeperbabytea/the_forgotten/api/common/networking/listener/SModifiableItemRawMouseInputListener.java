package dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener;

import dev.pages.creeperbabytea.client.networking.packet.RawMouseInputPacket;
import dev.pages.creeperbabytea.common.networking.PacketListener;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemState;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilityInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class SModifiableItemRawMouseInputListener extends PacketListener<RawMouseInputPacket> {
    public SModifiableItemRawMouseInputListener() {
        super(true);
    }

    @Override
    public void call(RawMouseInputPacket rawMouseInputPacket, IPayloadContext ctx) {
        ServerPlayer player = (ServerPlayer) ctx.player();

        ItemStack active = null;

        if(Registrations.ITEMS.hasState(player.getMainHandItem().getItem()))
            active = player.getMainHandItem();
        else if (Registrations.ITEMS.hasState(player.getOffhandItem().getItem()) && ((ModifiableItemState) Objects.requireNonNull(Registrations.ITEMS.getState(player.getOffhandItem().getItem()))).isAllowedOffhand())
            active = player.getOffhandItem();

        if (active != null) {
            AbilitiesContainer.AbilitySlot slot = AbilitiesContainer.AbilitySlot.of(rawMouseInputPacket);
            ModifiersContainer modifiers = new ModifiersContainer(active);
            AbilityInstance abilityInstance = modifiers.getAbilities().get(slot);
            if (abilityInstance != null) {
                ActivateAbilityEvent event = new ActivateAbilityEvent(player, InteractionHand.MAIN_HAND, active, (ModifiableItemState) Objects.requireNonNull(Registrations.ITEMS.getState(active.getItem())));
                TheForgotten.GAME.post(event);
                if (!event.isCanceled()) {
                    abilityInstance.activate(player, event.getScaling(), active);
                }
            }
        }
    }
}
