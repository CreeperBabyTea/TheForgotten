package dev.pages.creeperbabytea.the_forgotten.items.abilities;

import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ItemAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OverflowAbility extends ItemAbility {
    public OverflowAbility(int maxLvl, float scaling) {
        super(maxLvl, scaling, 4*20);
    }

    @Override
    public void activate(int lvl, Player player, float reScaling, ItemStack stack) {
        //TODO: Overflow技能(过载)
        //给玩家一个Overflow效果，期间玩家蓝耗锁定，所用技能scaling直接+1.2。
        //过载结束后，消耗技能总蓝耗x2的蓝，不够的蓝扣除1%血量每点
    }
}
