package dev.pages.creeperbabytea.the_forgotten.items.enrichments;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.InnerThoughts;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.StaticEnrichment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class SuperBigBrain extends StaticEnrichment {
    public SuperBigBrain() {
        super(1, new EntitySorcery.TemporaryBonusBundle(
                new Mana.TemporaryBonus(0, 1000),
                new InnerThoughts.TemporaryBonus()
        ));
    }
}
