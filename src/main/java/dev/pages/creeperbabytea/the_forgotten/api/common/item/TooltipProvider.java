package dev.pages.creeperbabytea.the_forgotten.api.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface TooltipProvider {
    List<Component> createToolTip(TooltipFlag flag);
}
