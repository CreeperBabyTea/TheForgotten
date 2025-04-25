package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import dev.pages.creeperbabytea.the_forgotten.api.util.Formatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record AbilityInstance(ActiveAbility ability, int lvl) {
    public static final Codec<AbilityInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActiveAbility.CODEC.fieldOf("ability").forGetter(AbilityInstance::ability),
            Codec.INT.fieldOf("lvl").forGetter(AbilityInstance::lvl)
    ).apply(instance, AbilityInstance::new));

    public static AbilityInstance of(ActiveAbility ability) {
        return new AbilityInstance(ability, 1);
    }

    public AbilityInstance setLvl(int lvl) {
        return new AbilityInstance(this.ability, lvl);
    }

    public void maybeActivate(final ServerPlayer player, final ItemStack stack, InteractionHand hand) {
        ability.maybeActivate(lvl, player, stack);
    }

    public List<Component> getDescription() {
        List<Component> ret = new ArrayList<>();
        ret.add(Component.translatable(ability.getTranslationKey()).withStyle(Style.EMPTY.withColor(ability.getToolTipColor())).append(" " + Formatting.convertToRoman(lvl)));
        ret.add(Component.translatable(ability.getLoreTranslationKey()));
        return ret;
    }

    public int getManaCost() {
        return ability.getManaCostRaw();
    }

    public Tag serializeNBT() {
        return CODEC.encodeStart(NbtOps.INSTANCE, this).resultOrPartial(TheForgotten.LOGGER::error).orElse(new CompoundTag());
    }

    @Nullable
    public static AbilityInstance deserializeNBT(Tag nbt) {
        return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(TheForgotten.LOGGER::error).orElse(null);
    }
}
