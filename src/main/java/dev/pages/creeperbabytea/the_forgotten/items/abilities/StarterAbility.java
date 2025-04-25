package dev.pages.creeperbabytea.the_forgotten.items.abilities;

import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StarterAbility extends ActiveAbility {
    public StarterAbility() {
        super(1, 5);
        this.setManaCostRaw(30);
    }

    @Override
    public void activate(int lvl, LivingEntity user, float reScaling) {
        if (user instanceof Player player)
            player.displayClientMessage(Component.literal("meow"), false);
    }
}
