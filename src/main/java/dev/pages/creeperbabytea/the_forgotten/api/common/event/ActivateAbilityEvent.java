package dev.pages.creeperbabytea.the_forgotten.api.common.event;

import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ActivateAbilityEvent extends PlayerEvent implements ICancellableEvent {
    protected final InteractionHand hand;
    protected final ItemStack itemStack;
    protected final ModifiableItemState state;
    private float scaling = 1.0f;

    public ActivateAbilityEvent(ServerPlayer player, InteractionHand hand, ItemStack itemStack, ModifiableItemState state) {
        super(player);
        this.hand = hand;
        this.itemStack = itemStack;
        this.state = state;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public float getScaling() {
        return scaling;
    }
}
