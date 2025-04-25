package dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.TooltipProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class EnrichmentsContainer implements TooltipProvider {
    public static final String KEY = "enrichments";
    public static final Codec<EnrichmentsContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(EnrichmentInstance.CODEC, 0, 64).fieldOf(KEY).forGetter(EnrichmentsContainer::getEnrichments)
    ).apply(instance, EnrichmentsContainer::new));

    private final List<EnrichmentInstance> enrichments;
    private ListTag tag;

    private EnrichmentsContainer(List<EnrichmentInstance> list) {
        this.enrichments = new ArrayList<>(list);
    }

    private EnrichmentsContainer() {
        this.enrichments = new ArrayList<>();
    }

    private EnrichmentsContainer bind(ListTag tag) {
        this.tag = tag;
        return this;
    }

    public static EnrichmentsContainer of(ItemStack stack) {
        return ModifiersContainer.of(stack).getEnrichments();
    }

    public static EnrichmentsContainer deserializeNBT(CompoundTag baseTag) {
        if (!baseTag.contains(KEY))
            baseTag.put(KEY, new ListTag());
        var tag = baseTag.getList(KEY, 10);
        return CODEC.parse(NbtOps.INSTANCE, baseTag).resultOrPartial(TheForgotten.LOGGER::error).orElse(new EnrichmentsContainer()).bind(tag);
    }

    public void put(EnrichmentInstance instance) {
        remove(instance.getEnrichment());
        this.tag.add(instance.serializeNBT());
        this.enrichments.add(instance);
    }

    public void remove(ItemEnrichment enrichment) {
        for (int i = 0, size = enrichments.size(); i < size; i++) {
            if (enrichments.get(i).getEnrichment() == enrichment) {
                enrichments.remove(i);
                tag.remove(i);
            }
        }
    }

    @Override
    public List<Component> createToolTip(TooltipFlag flag) {
        List<Component> ret = new ArrayList<>();
        this.enrichments.forEach(instance -> {
            ret.add(Component.translatable(instance.getEnrichment().getTranslationKey()).setStyle(Style.EMPTY.withBold(true)));
            ret.add(Component.translatable(instance.getEnrichment().getLoreTranslationKey()));
            ret.add(Component.empty());
        });
        return ret;
    }

    public List<EnrichmentInstance> getEnrichments() {
        return enrichments;
    }
}
