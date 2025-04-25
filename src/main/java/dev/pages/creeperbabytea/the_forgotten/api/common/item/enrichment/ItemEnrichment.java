package dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.common.register.Registrable;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class ItemEnrichment implements Registrable<ItemEnrichment> {
    public static final Codec<ItemEnrichment> CODEC = Registrations.ENRICHMENTS.getRegistry().get().byNameCodec();
    private ResourceLocation name;
    private final int maxLvl;

    protected ItemEnrichment(int maxLvl) {
        this.maxLvl = maxLvl;
    }

    public abstract EntitySorcery.TemporaryBonusBundle initialBonus(int lvl, Player player, CompoundTag extraInfo);

    public EntitySorcery.TemporaryBonusBundle mapBonus(int lvl, Player player, int ticksActivated, EntitySorcery.TemporaryBonusBundle bonus, CompoundTag extraInfo) {
        return bonus;
    }

    @Override
    public void setName(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getName() {
        return this.name;
    }

    public String getTranslationKey() {
        return "item.enrichment." + name.toLanguageKey();
    }

    public String getLoreTranslationKey() {
        return "item.enrichment.lore." + name.toLanguageKey();
    }

    public int getMaxLvl() {
        return maxLvl;
    }
}
