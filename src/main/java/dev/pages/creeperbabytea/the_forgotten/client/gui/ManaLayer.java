package dev.pages.creeperbabytea.the_forgotten.client.gui;

import dev.pages.creeperbabytea.client.gui.ExpLikeLayer;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ManaLayer extends ExpLikeLayer {
    private static final ResourceLocation REGEN_BLOCK = TheForgotten.modLoc("hud/mana_bar_background_regen_blocked");
    private static final ResourceLocation CONSUME_BLOCK = TheForgotten.modLoc("hud/mana_bar_progress_consume_blocked");

    public ManaLayer() {
        super(TheForgotten.modLoc("mana"),
                player -> {
                    Mana mana = Mana.from(player);
                    return 1f * mana.getMana() / mana.getMaxMana();
                },
                player -> {
                    Mana mana = Mana.from(player);
                    return mana.getMana() + "/" + mana.getMaxMana();
                },
                AbilitiesContainer.MANA_COST_TEXT_COLOR,
                TheForgotten.modLoc("hud/mana_bar_background"),
                TheForgotten.modLoc("hud/mana_bar_progress"), null);
        this.setShowPredicate(mc -> mc.gameMode != null && mc.gameMode.canHurtPlayer());
    }

    @Override
    protected void renderBarProgress(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        Mana mana = Mana.from(player);
        if (mana.isManaConsumeBlocked())
            guiGraphics.blitSprite(RenderType::guiTextured, CONSUME_BLOCK, width, height, 0, 0, xOffset, yOffset, Math.round(width * value), 5);
        else
            super.renderBarProgress(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
    }

    @Override
    protected void renderBarBackground(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        Mana mana = Mana.from(player);
        if (mana.isManaRegenBlocked())
            guiGraphics.blitSprite(RenderType::guiTextured, REGEN_BLOCK, width, height, 0, 0, xOffset, yOffset, width, 5);
        else
            super.renderBarBackground(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
    }
}
