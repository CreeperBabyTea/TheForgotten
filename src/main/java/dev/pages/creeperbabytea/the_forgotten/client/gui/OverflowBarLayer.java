package dev.pages.creeperbabytea.the_forgotten.client.gui;

import dev.pages.creeperbabytea.client.gui.HotbarSideBarLayer;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.init.ForgottenAbilities;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;

public class OverflowBarLayer extends HotbarSideBarLayer {
    public OverflowBarLayer() {
        super(TheForgotten.modLoc("overflow"),
                player -> {
                    var manaFlown = ForgottenAbilities.OVER_FLOW.get().getManaFlown(player);
                    var healthFlown = ForgottenAbilities.OVER_FLOW.get().getHealthFlown(player);
                    return Math.min(1f, healthFlown > 0 ? 1f * healthFlown / 20 : 1f * manaFlown / 100);
                },
                TheForgotten.modLoc("hud/overflow_background"), TheForgotten.modLoc("hud/overflow_mana_progress"), true);
        this.setShowPredicate(mc -> mc.player != null && AbilityMarkers.from(mc.player).isActivated(ForgottenAbilities.OVER_FLOW.get()));
    }

    @Override
    protected void renderBarProgress(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        var healthFlown = ForgottenAbilities.OVER_FLOW.get().getHealthFlown(player);
        if (healthFlown > 0) {
            if (healthFlown >= 20)
                guiGraphics.blitSprite(RenderType::guiTextured, TheForgotten.modLoc("hud/overflow_mark_death"), xOffset, yOffset, width, height);
            else
                guiGraphics.blitSprite(RenderType::guiTextured, TheForgotten.modLoc("hud/overflow_health_progress"), width, height, 0, 0, xOffset, yOffset, Math.round(width * value), 5);
        } else
            super.renderBarProgress(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
    }

    @Override
    protected void renderBarBackground(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        var healthFlown = ForgottenAbilities.OVER_FLOW.get().getHealthFlown(player);
        if (healthFlown > 0) {
            if (healthFlown < 20)
                guiGraphics.blitSprite(RenderType::guiTextured, this.getProgressSprite(), xOffset, yOffset, width, height);
        } else
            super.renderBarBackground(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
    }
}
