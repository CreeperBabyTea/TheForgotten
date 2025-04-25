package dev.pages.creeperbabytea.the_forgotten.api.common.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.enrichment.EnrichmentMarker;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import dev.pages.creeperbabytea.the_forgotten.init.Misc;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;

public record SorceryAttachments(EntitySorcery sorcery, AbilityMarkers abilityMarkers, EnrichmentMarker enrichmentMarker) {
    public static final Codec<SorceryAttachments> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntitySorcery.CODEC.fieldOf("sorcery").forGetter(SorceryAttachments::sorcery),
            AbilityMarkers.CODEC.fieldOf("ability_markers").forGetter(SorceryAttachments::abilityMarkers),
            EnrichmentMarker.CODEC.fieldOf("enrichment_marker").forGetter(SorceryAttachments::enrichmentMarker)
    ).apply(instance, SorceryAttachments::new));

    public void tick(LivingEntity owner) {
        enrichmentMarker.tick(owner);
        sorcery.tick();
        abilityMarkers.tick(owner);
    }

    public static SorceryAttachments empty() {
        return new SorceryAttachments(new EntitySorcery(), new AbilityMarkers(), new EnrichmentMarker());
    }

    public static SorceryAttachments from(LivingEntity owner) {
        return owner.getData(Misc.ENTITY_ATTACHMENTS);
    }

    public static void init(IEventBus mod, IEventBus game) {
        game.addListener(EnrichmentMarker::onPlayerActiveItemChange);
    }
}
