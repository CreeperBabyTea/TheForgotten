package dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public final class EnrichmentInstance {
    public static Codec<EnrichmentInstance> CODEC = RecordCodecBuilder.create(instacne -> instacne.group(
            ItemEnrichment.CODEC.fieldOf("enrichment").forGetter(EnrichmentInstance::getEnrichment),
            Codec.INT.fieldOf("lvl").forGetter(EnrichmentInstance::getLvl),
            CompoundTag.CODEC.fieldOf("extra_info").forGetter(EnrichmentInstance::getExtraInfo)
    ).apply(instacne, EnrichmentInstance::new));
    private final ItemEnrichment enrichment;
    private final int lvl;
    private CompoundTag extraInfo;

    private EnrichmentInstance(ItemEnrichment enrichment, int lvl, CompoundTag extraInfo) {
        this.enrichment = enrichment;
        this.lvl = lvl;
        this.extraInfo = extraInfo;
    }

    public EnrichmentInstance(ItemEnrichment enrichment, int lvl, Player player) {
        this(enrichment, lvl, new CompoundTag());
    }

    public EntitySorcery.TemporaryBonusBundle initialBonus(Player player) {
        return enrichment.initialBonus(lvl, player, extraInfo);
    }

    public EntitySorcery.TemporaryBonusBundle mapBonus(Player player, int ticksActivated, EntitySorcery.TemporaryBonusBundle bonus) {
        return enrichment.mapBonus(lvl, player, ticksActivated, bonus, extraInfo);
    }

    public Tag serializeNBT() {
        return CODEC.encodeStart(NbtOps.INSTANCE, this).resultOrPartial(TheForgotten.LOGGER::error).orElse(new CompoundTag());
    }

    @Nullable
    public static EnrichmentInstance deserializeNBT(Tag tag) {
        return CODEC.parse(NbtOps.INSTANCE, tag).resultOrPartial(TheForgotten.LOGGER::error).orElse(null);
    }

    public ItemEnrichment getEnrichment() {
        return enrichment;
    }

    public int getLvl() {
        return lvl;
    }

    public CompoundTag getExtraInfo() {
        return extraInfo;
    }
}
