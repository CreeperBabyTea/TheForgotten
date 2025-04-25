package dev.pages.creeperbabytea.the_forgotten.api.common.item;


import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.EnrichmentsContainer;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModifiersContainer {
    public static final String MODIFIER_BASE_KEY = "modifiers";

    private CustomData data;
    private final ItemStack itemStack;
    private final ModifiableItemInfo<?> info;
    private final CompoundTag baseTag;

    private final EnrichmentsContainer enrichments;
    private final AbilitiesContainer abilities;

    private ModifiersContainer(ItemStack itemStack) {
        this.itemStack = itemStack;
        if (!ModifiableItemInfo.hasInfoFor(itemStack.getItem()))
            throw new IllegalStateException("Can't create modifier container for item: " + itemStack.getItem());
        this.info = ModifiableItemInfo.getInfo(itemStack.getItem());

        if (!itemStack.has(DataComponents.CUSTOM_DATA)) {
            this.data = CustomData.EMPTY;
            itemStack.applyComponents(DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, data).build());
        } else
            this.data = itemStack.get(DataComponents.CUSTOM_DATA);

        Objects.requireNonNull(this.data);

        this.baseTag = data.copyTag();
        CompoundTag modifierTag;
        if (baseTag.contains(MODIFIER_BASE_KEY))
            modifierTag = baseTag.getCompound(MODIFIER_BASE_KEY);
        else {
            modifierTag = new CompoundTag();
            baseTag.put(MODIFIER_BASE_KEY, modifierTag);
        }

        this.abilities = AbilitiesContainer.deserialize(modifierTag);
        this.enrichments = EnrichmentsContainer.deserializeNBT(modifierTag);
    }

    public static ModifiersContainer of(ItemStack itemStack) {
        return new ModifiersContainer(itemStack);
    }

    public static boolean hasModifiers(ItemStack stack) {
        return ModifiableItemInfo.isModifiable(stack.getItem()) &&
                stack.has(DataComponents.CUSTOM_DATA) &&
                Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).copyTag().contains(MODIFIER_BASE_KEY);
    }

    /**
     * 从<code>ModifiersContainer</code>的下级(即{@link #abilities}等)，所有操作都是直接修改<code>baseTag</code>，
     * 所以在更新数据时仅需将baseTag直接更新到itemStack
     */
    public ModifiersContainer update() {
        this.data = CustomData.of(baseTag);
        itemStack.set(DataComponents.CUSTOM_DATA, data);
        return this;
    }

    public List<Component> getTooltip(TooltipFlag flag) {
        List<Component> ret = new ArrayList<>();
        ret.add(Component.empty());
        ret.addAll(info.createToolTip(flag));
        ret.add(Component.empty());
        ret.addAll(abilities.createToolTip(flag));
        ret.add(Component.empty());
        ret.addAll(enrichments.createToolTip(flag));
        return ret;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public AbilitiesContainer getAbilities() {
        return abilities;
    }

    public EnrichmentsContainer getEnrichments() {
        return enrichments;
    }

    public static void onTooltipAttaching(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (ModifiableItemInfo.isModifiable(itemStack.getItem())) {
            ModifiersContainer modifiers = of(itemStack);
            event.getToolTip().addAll(modifiers.getTooltip(event.getFlags()));
        }
    }
}
