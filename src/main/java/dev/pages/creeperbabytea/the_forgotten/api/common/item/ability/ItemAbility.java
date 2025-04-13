package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.common.register.Registrable;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class ItemAbility implements Registrable<ItemAbility> {
    public static final Codec<ItemAbility> CODEC = Registrations.ABILITIES.getRegistry().get().byNameCodec();

    private final int maxLvl;
    private final float scaling;
    private final int coolDownTicks;

    private ResourceLocation name;
    private int manaCost = 1;
    private int toolTipColor = 0xFFFFFF;

    public ItemAbility(int maxLvl, float scaling, int coolDownTicks) {
        this.maxLvl = maxLvl;
        this.scaling = scaling;
        this.coolDownTicks = coolDownTicks;
    }

    protected void setManaCost(int cost) {
        this.manaCost = cost;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getManaCost(ItemStack stack) {
        return getManaCost();
    }

    protected void setToolTipColor(int toolTipColor) {
        this.toolTipColor = toolTipColor;
    }

    public AbilityInstance getDefaultInstance() {
        return new AbilityInstance(this, 1);
    }

    public int toolTipColor() {
        return toolTipColor;
    }

    public String getTranslationKey() {
        return "ability." + name.toLanguageKey();
    }

    public String getLoreTranslationKey() {
        return "ability.lore." + name.toLanguageKey();
    }

    @Override
    public void setName(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    public abstract void activate(int lvl, final Player player, float reScaling, final ItemStack stack);

}
