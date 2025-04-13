package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import dev.pages.creeperbabytea.client.networking.packet.RawMouseInputPacket;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifierEntryContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.i18n.TranslationKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.*;

public class AbilitiesContainer implements ModifierEntryContainer {
    public static final String TAG = "abilities";
    public static final int MANA_COST_TEXT_COLOR = 0x00D8FF;

    private final ModifiersContainer parent;

    private final CompoundTag abilityTag;
    private final Map<AbilitySlot, AbilityInstance> abilities = new EnumMap<>(AbilitySlot.class);

    public AbilitiesContainer(CompoundTag modifierTag, ModifiersContainer parent) {
        this.parent = parent;
        CompoundTag abilityTag;
        if (modifierTag.contains(TAG))
            abilityTag = modifierTag.getCompound(TAG);
        else {
            abilityTag = new CompoundTag();
            modifierTag.put(TAG, abilityTag);
        }
        Set<String> slots = abilityTag.getAllKeys();
        slots.forEach(slot -> abilities.put(AbilitySlot.valueOf(slot), AbilityInstance.deserializeNBT(abilityTag.getCompound("slot"))));

        this.abilityTag = abilityTag;
    }

    @Nullable
    public AbilityInstance put(AbilitySlot slot, AbilityInstance instance) {
        abilityTag.put(slot.name(), instance.serializeNBT());
        return this.abilities.put(slot, instance);
    }

    @Nullable
    public AbilityInstance get(AbilitySlot slot) {
        return this.abilities.get(slot);
    }

    @Override
    public List<Component> createToolTip() {
        final List<Component> toolTip = new ArrayList<>();
        abilities.forEach((slot, instance) -> {
            toolTip.add(Component.translatable(slot.getTranslationKey()).setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)).append(":"));
            toolTip.addAll(instance.getTranslatableComponent());
            toolTip.add(Component.translatable(TranslationKeys.MANA_COST).setStyle(Style.EMPTY.withColor(MANA_COST_TEXT_COLOR)).append(Component.literal(": " + instance.getManaCost())));
            toolTip.add(Component.empty());
        });
        return toolTip;
    }

    public enum AbilitySlot {
        LEFT, RIGHT, SHIFT_LEFT, SHIFT_RIGHT;

        public String getTranslationKey() {
            return TheForgotten.MODID + ".ability.slot." + name().toLowerCase();
        }

        public static AbilitySlot of(boolean isLeft, boolean isShiftHeld) {
            if (isLeft) {
                if (isShiftHeld)
                    return SHIFT_LEFT;
                else
                    return LEFT;
            } else {
                if (isShiftHeld)
                    return SHIFT_RIGHT;
                else
                    return RIGHT;
            }
        }

        public static AbilitySlot of(final RawMouseInputPacket packet) {
            return of(packet.isLeftClick(), (packet.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0);
        }
    }
}
