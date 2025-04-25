package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.client.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.TooltipProvider;
import dev.pages.creeperbabytea.the_forgotten.i18n.TranslationKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.*;

public class AbilitiesContainer implements TooltipProvider {
    public static final String KEY = "abilities";
    public static final Codec<AbilitiesContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(AbilitySlot.CODEC, AbilityInstance.CODEC).fieldOf(KEY).forGetter(AbilitiesContainer::getAbilities)
    ).apply(instance, AbilitiesContainer::new));

    public static final int MANA_COST_TEXT_COLOR = 0x00D8FF;

    protected CompoundTag tag;

    private final Map<AbilitySlot, AbilityInstance> abilities;

    private AbilitiesContainer(Map<AbilitySlot, AbilityInstance> map) {
        if (map.isEmpty())
            this.abilities = new EnumMap<>(AbilitySlot.class);
        else
            this.abilities = new EnumMap<>(map);
    }

    private AbilitiesContainer() {
        this.abilities = new EnumMap<>(AbilitySlot.class);
    }

    private AbilitiesContainer bind(CompoundTag tag) {
        this.tag = tag;
        return this;
    }

    public static AbilitiesContainer deserialize(CompoundTag baseTag) {
        if (!baseTag.contains(KEY))
            baseTag.put(KEY, new CompoundTag());
        var tag = baseTag.getCompound(KEY);
        return CODEC.parse(NbtOps.INSTANCE, baseTag).resultOrPartial(TheForgotten.LOGGER::error).orElse(new AbilitiesContainer()).bind(tag);
    }

    public void put(AbilitySlot slot, AbilityInstance instance) {
        this.tag.put(slot.name(), instance.serializeNBT());
        this.abilities.put(slot, instance);
    }

    @Nullable
    public AbilityInstance get(AbilitySlot slot) {
        return this.abilities.get(slot);
    }

    public void remove(AbilitySlot slot) {
        this.abilities.remove(slot);
    }

    @Override
    public List<Component> createToolTip(TooltipFlag flag) {
        final List<Component> toolTip = new ArrayList<>();
        abilities.forEach((slot, instance) -> {
            toolTip.add(Component.translatable(slot.getTranslationKey()).setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)).append(":"));
            toolTip.add(Component.empty());
            toolTip.addAll(instance.getDescription());
            toolTip.add(Component.empty());
            toolTip.add(Component.translatable(TranslationKeys.MANA_COST).setStyle(Style.EMPTY.withColor(MANA_COST_TEXT_COLOR)).append(Component.literal(": " + instance.getManaCost())));
        });
        return toolTip;
    }

    private Map<AbilitySlot, AbilityInstance> getAbilities() {
        return abilities;
    }

    public enum AbilitySlot {
        left, right, shift_left, shift_right;

        public String getTranslationKey() {
            return TheForgotten.MODID + ".ability.slot." + name();
        }

        public static final Codec<AbilitySlot> CODEC = Codec.STRING.xmap(AbilitySlot::valueOf, AbilitySlot::name);

        public static AbilitySlot of(boolean isLeft, boolean isShiftHeld) {
            if (isLeft) {
                if (isShiftHeld)
                    return shift_left;
                else
                    return left;
            } else {
                if (isShiftHeld)
                    return shift_right;
                else
                    return right;
            }
        }

        public static AbilitySlot of(final CRawMouseInputPacket packet) {
            return of(packet.isLeftClick(), (packet.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0);
        }
    }
}
