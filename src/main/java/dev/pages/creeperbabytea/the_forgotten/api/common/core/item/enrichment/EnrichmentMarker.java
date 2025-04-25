package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.enrichment;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.player.MainHandItemChangedEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.EnrichmentInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 仅对玩家有效
 */
public class EnrichmentMarker {
    public static final Codec<EnrichmentMarker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(EnrichmentInstance.CODEC, EntitySorcery.TemporaryBonusBundle.CODEC).fieldOf("enrichments").forGetter(EnrichmentMarker::getBonusBundleMap),
            Codec.INT.fieldOf("ticks").forGetter(EnrichmentMarker::getTicksActivated)
    ).apply(instance, EnrichmentMarker::new));
    public static final EnrichmentMarker EMPTY = new EnrichmentMarker();

    private Map<EnrichmentInstance, EntitySorcery.TemporaryBonusBundle> bonusBundleMap;
    private int ticksActivated = 0;
    private EntitySorcery.TemporaryBonusBundle bonusLastTick = new EntitySorcery.TemporaryBonusBundle();

    private EnrichmentMarker(Map<EnrichmentInstance, EntitySorcery.TemporaryBonusBundle> enrichments, int ticksActivated) {
        this.bonusBundleMap = new LinkedHashMap<>(enrichments);
        this.ticksActivated = ticksActivated;
    }

    public EnrichmentMarker() {
        this.bonusBundleMap = new LinkedHashMap<>();
    }

    public static EnrichmentMarker from(LivingEntity owner) {
        return owner instanceof Player ? SorceryAttachments.from(owner).enrichmentMarker() : EMPTY;
    }

    public void tick(LivingEntity owner) {
        if (owner instanceof Player player) {
            ticksActivated++;

            var sorcery = EntitySorcery.from(player);
            sorcery.removeTempBonus(bonusLastTick);
            bonusLastTick = new EntitySorcery.TemporaryBonusBundle();
            bonusBundleMap.forEach((k, v) -> {
                k.mapBonus(player, ticksActivated, v);
                bonusLastTick.merge(v);
            });
            sorcery.applyTempBonus(bonusLastTick);
        }
    }

    public Map<EnrichmentInstance, EntitySorcery.TemporaryBonusBundle> getBonusBundleMap() {
        return bonusBundleMap;
    }

    public static void update(Player player) {
        var held = player.getMainHandItem();
        var marker = EnrichmentMarker.from(player);
        marker.clear(player);
        if (ModifiersContainer.hasModifiers(held)) {
            ModifiersContainer modifiers = ModifiersContainer.of(held);
            var sorcery = EntitySorcery.from(player);

            marker.getBonusBundleMap().values().forEach(sorcery::removeTempBonus);
            marker.bonusBundleMap = modifiers.getEnrichments().getEnrichments().stream().collect(Collectors.toMap(k -> k, k -> k.initialBonus(player)));

            marker.bonusBundleMap.forEach((k, v) -> {
                marker.bonusLastTick.merge(v);
            });
            sorcery.applyTempBonus(marker.bonusLastTick);
        }
    }

    private void clear(Player player) {
        var sorcery = EntitySorcery.from(player);
        sorcery.removeTempBonus(bonusLastTick);
        this.bonusLastTick = new EntitySorcery.TemporaryBonusBundle();
        this.bonusBundleMap.clear();
        this.ticksActivated = 0;
    }

    public static void onPlayerActiveItemChange(MainHandItemChangedEvent event) {
        EnrichmentMarker.update(event.getEntity());
    }

    public int getTicksActivated() {
        return ticksActivated;
    }
}
