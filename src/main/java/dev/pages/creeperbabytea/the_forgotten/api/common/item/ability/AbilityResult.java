package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class AbilityResult {
    private final AbilityResultType type;
    private final CompoundTag info;

    private AbilityResult(AbilityResultType type, @Nullable CompoundTag info) {
        this.type = type;
        this.info = info;
    }

    public AbilityResultType type() {
        return type;
    }

    public CompoundTag info() {
        return info;
    }

    @Override
    public String toString() {
        return "AbilityResult[" +
                "type=" + type + ", " +
                "info=" + info + ']';
    }

    public enum AbilityResultType {
        DAMAGE,
        TELEPORT,
        DEFENCE
    }
}
