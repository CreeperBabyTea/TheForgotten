package dev.pages.creeperbabytea.the_forgotten.api.common.item;


import com.google.common.collect.Lists;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

import java.util.Objects;

public class ModifiersContainer {
    public static final String MODIFIER_BASE_KEY = "modifiers";

    private CustomData data;
    private ItemLore toolTip;
    private final ItemStack itemStack;
    private final CompoundTag baseTag;
    private final CompoundTag modifierTag;

    private final AbilitiesContainer abilities;

    public ModifiersContainer(ItemStack itemStack) {
        this.itemStack = itemStack;
        if (!Registrations.ITEMS.hasState(itemStack.getItem()))
            throw new IllegalStateException("Can't create modifier container for a plain item: " + itemStack.getItem());

        if (!itemStack.has(DataComponents.CUSTOM_DATA)) {
            this.data = CustomData.EMPTY;
            itemStack.applyComponents(DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, data).build());
        } else
            this.data = itemStack.get(DataComponents.CUSTOM_DATA);

        Objects.requireNonNull(this.data);

        this.baseTag = data.copyTag();
        if (baseTag.contains(MODIFIER_BASE_KEY))
            this.modifierTag = baseTag.getCompound(MODIFIER_BASE_KEY);
        else {
            this.modifierTag = new CompoundTag();
            baseTag.put(MODIFIER_BASE_KEY, this.modifierTag);
        }

        this.abilities = new AbilitiesContainer(modifierTag, this);
    }

    public ModifiersContainer(Item item, int amount) {
        this(new ItemStack(item, amount));
    }

    public ModifiersContainer update() {
        this.data = CustomData.of(baseTag);
        itemStack.set(DataComponents.CUSTOM_DATA, data);
        this.toolTip = new ItemLore(abilities.createToolTip());
        itemStack.set(DataComponents.LORE, toolTip);
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public AbilitiesContainer getAbilities() {
        return abilities;
    }
}
