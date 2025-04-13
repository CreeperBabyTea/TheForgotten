package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.util.Formatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

public record AbilityInstance(ItemAbility ability, int lvl) {
    public static AbilityInstance of(ItemAbility ability) {
        return new AbilityInstance(ability, 1);
    }

    public AbilityInstance setLvl(int lvl) {
        return new AbilityInstance(this.ability, lvl);
    }

    public void activate(final Player player, float scaling, final ItemStack stack) {
        if (ability != null) {
            ability.activate(lvl, player, scaling, stack);
        }
    }

    public List<Component> getTranslatableComponent() {
        List<Component> ret = new ArrayList<>();
        ret.add(Component.translatable(ability.getTranslationKey()).withStyle(Style.EMPTY.withColor(ability.toolTipColor())).append(" " + Formatting.convertToRoman(lvl)));
        ret.add(Component.translatable(ability.getLoreTranslationKey()));
        return ret;
    }

    public int getManaCost() {
        return ability.getManaCost();
    }

    public CompoundTag serializeNBT() {
        CompoundTag base = new CompoundTag();
        base.putString("id", Objects.requireNonNull(Registrations.ABILITIES.getRegistry().get().getKey(ability)).toString());
        base.putInt("lvl", lvl);
        return base;
    }

    @Nullable
    public static AbilityInstance deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("id")) {
            if (!nbt.getString("id").equals("empty")) {
                ItemAbility ability = Registrations.ABILITIES.getRegistry().get().getValue(ResourceLocation.parse(nbt.getString("id")));
                int lvl = nbt.contains("lvl") ? nbt.getInt("lvl") : 1;
                if (ability != null) {
                    return new AbilityInstance(ability, lvl);
                }
            }
        }
        TheForgotten.LOGGER.error("Failed to deserialize NBT of an ability instance!");
        return null;
    }
}
