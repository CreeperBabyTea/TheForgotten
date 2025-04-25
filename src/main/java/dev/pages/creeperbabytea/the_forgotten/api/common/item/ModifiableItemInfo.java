package dev.pages.creeperbabytea.the_forgotten.api.common.item;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.common.register.EntryInfo;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.i18n.TranslationKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModifiableItemInfo<I extends Item> extends EntryInfo<Item, I> implements TooltipProvider {
    public static final Codec<EntryInfo.BuiltInEntryInfo<Item>> CODEC = Registrations.ITEMS.getInfoCodec();

    private boolean allowedOffhand = false;
    private float reScaling = 1.0F;
    private final List<AbilitiesContainer.AbilitySlot> disabledSlots = new ArrayList<>();

    public ModifiableItemInfo(I item) {
        super(() -> item);
    }

    private ModifiableItemInfo(I item, CompoundTag info) {
        super(() -> item, info);
        if (info.contains("offhand"))
            this.setAllowOffhand(info.getBoolean("offhand"));
        if (info.contains("rescaling"))
            this.setRescaling(info.getFloat("rescaling"));
        if (info.contains("disabled_slots"))
            disabledSlots.addAll(info.getList("disabled_slots", 8).stream()
                    .map(tag -> AbilitiesContainer.AbilitySlot.valueOf(tag.getAsString())).toList());
    }

    public ModifiableItemInfo<I> setAllowOffhand(boolean val) {
        this.allowedOffhand = val;
        info.putBoolean("offhand", val);
        return this;
    }

    public ModifiableItemInfo<I> setRescaling(float val) {
        this.reScaling = val;
        info.putFloat("rescaling", val);
        return this;
    }

    public ModifiableItemInfo<I> ULTRA_HIGH_RESCALING() {
        return setRescaling(255);
    }

    public ModifiableItemInfo<I> disableSlots(AbilitiesContainer.AbilitySlot... slots) {
        this.disabledSlots.clear();
        this.disabledSlots.addAll(checkLegalityOfSlots(slots));
        var nbt = new ListTag();
        nbt.addAll(disabledSlots.stream().map(s -> StringTag.valueOf(s.name())).toList());
        info.put("disabled_slots", nbt);
        return this;
    }

    public ModifiableItemInfo<I> disableAllSlots() {
        return disableSlots(AbilitiesContainer.AbilitySlot.values());
    }

    protected static List<AbilitiesContainer.AbilitySlot> checkLegalityOfSlots(AbilitiesContainer.AbilitySlot... slots) {
        List<AbilitiesContainer.AbilitySlot> ret = new ArrayList<>();
        for (AbilitiesContainer.AbilitySlot slot : slots)
            if (!ret.contains(slot))
                ret.add(slot);
        return ret;
    }

    public boolean allowedOffhand() {
        return allowedOffhand;
    }

    public float getReScaling() {
        return reScaling;
    }

    public List<AbilitiesContainer.AbilitySlot> getDisabledSlots() {
        return Collections.unmodifiableList(disabledSlots);
    }

    public static boolean isModifiable(Item item) {
        return Registrations.ITEMS.hasInfoFor(item);
    }

    public static boolean hasInfoFor(Item item) {
        return Registrations.ITEMS.hasInfoFor(item);
    }

    public static <I extends Item> ModifiableItemInfo<I> getInfo(I item) {
        if (hasInfoFor(item)) {
            var rawInfo = Registrations.ITEMS.getInfo(item);
            assert rawInfo != null;
            return new ModifiableItemInfo<>(item, rawInfo.getInfo());
        } else {
            TheForgotten.LOGGER.warn("No info found for item {}", item);
            return new ModifiableItemInfo<>(item);
        }
    }

    /**
     * 仅用于生成原版物品的附加信息
     */
    @Override
    public BuiltInEntryInfo<Item> mapToRawInfo() {
        var srcKey = BuiltInRegistries.ITEM.getResourceKey(this.get());
        if (srcKey.isPresent())
            return new BuiltInEntryInfo<>(BuiltInRegistries.ITEM.getOrThrow(srcKey.get()), this.info);
        throw new IllegalStateException("Can't get ResourceKey for item " + this.get() + ". Check if u called mapToRawInfo() on a vanilla item.");
    }

    @Override
    public List<Component> createToolTip(TooltipFlag flag) {
        List<Component> ret = new ArrayList<>();
        ret.add(Component.translatable(this.allowedOffhand ? TranslationKeys.ITEM_INFO_ALLOW_OFFHAND : TranslationKeys.ITEM_INFO_DISABLE_OFFHAND));
        ret.add(Component.translatable(TranslationKeys.ITEM_INFO_RESCALING, this.reScaling));
        if (!this.disabledSlots.isEmpty()) {
            ret.add(Component.translatable(TranslationKeys.ITEM_INFO_DISABLED_SLOTS));
            this.disabledSlots.forEach(slot -> ret.add(Component.literal("    ").append(Component.translatable(slot.getTranslationKey()))));
        }
        return ret;
    }
}
