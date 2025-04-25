package dev.pages.creeperbabytea.the_forgotten.api.common.event.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MainHandItemChangedEvent extends PlayerEvent {
    private final ItemStack from, to;

    public MainHandItemChangedEvent(Player player, ItemStack from, ItemStack stack) {
        super(player);
        this.from = from;
        to = stack;
    }

    public ItemStack getFrom() {
        return from;
    }

    public ItemStack getTo() {
        return to;
    }
}
