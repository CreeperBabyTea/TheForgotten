package dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class StaticEnrichment extends ItemEnrichment {
    private final EntitySorcery.TemporaryBonusBundle staticBonus;

    public StaticEnrichment(int maxLvl, EntitySorcery.TemporaryBonusBundle staticBonus) {
        super(maxLvl);
        this.staticBonus = staticBonus;
    }

    @Override
    public EntitySorcery.TemporaryBonusBundle initialBonus(int lvl, Player player, CompoundTag extraInfo) {
        return staticBonus;
    }
}
