package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.DurableAbility;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.StackableAbility;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.UnstackableAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对一切生物有效。
 */
public class AbilityMarkers {
    public static final Codec<AbilityMarkers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ActiveAbility.CODEC, CooldownMarker.CODEC).fieldOf("cooldown").forGetter(AbilityMarkers::getCooldownMarkerMap),
            Codec.unboundedMap(ActiveAbility.CODEC, StackableMarker.CODEC).fieldOf("stackable").forGetter(AbilityMarkers::getStackableMarkerMap),
            Codec.unboundedMap(ActiveAbility.CODEC, UnstackableMarker.CODEC).fieldOf("unstackable").forGetter(AbilityMarkers::getUnstackableMarkerMap)
    ).apply(instance, AbilityMarkers::new));

    private final Map<ActiveAbility, CooldownMarker> cooldownMarkerMap;
    private final Map<ActiveAbility, StackableMarker> stackableMarkerMap;
    private final Map<ActiveAbility, UnstackableMarker> unstackableMarkerMap;

    private AbilityMarkers(Map<ActiveAbility, CooldownMarker> cooldownMarkerMap, Map<ActiveAbility, StackableMarker> stackableMarkerMap, Map<ActiveAbility, UnstackableMarker> unstackableMarkerMap) {
        this.cooldownMarkerMap = new LinkedHashMap<>(cooldownMarkerMap);
        this.stackableMarkerMap = new LinkedHashMap<>(stackableMarkerMap);
        this.unstackableMarkerMap = new LinkedHashMap<>(unstackableMarkerMap);
    }

    public AbilityMarkers() {
        this.cooldownMarkerMap = new LinkedHashMap<>();
        this.stackableMarkerMap = new LinkedHashMap<>();
        this.unstackableMarkerMap = new LinkedHashMap<>();
    }

    public static AbilityMarkers from(LivingEntity owner) {
        return SorceryAttachments.from(owner).abilityMarkers();
    }

    public void tick(LivingEntity owner) {
        this.cooldownMarkerMap.entrySet().removeIf(e -> !e.getValue().tick());
        this.stackableMarkerMap.entrySet().removeIf(e -> !e.getValue().tick((StackableAbility) e.getKey(), owner));
        this.unstackableMarkerMap.entrySet().removeIf(e -> !e.getValue().tick((UnstackableAbility) e.getKey(), owner));
    }

    public boolean isOnCooldown(ActiveAbility ability) {
        return cooldownMarkerMap.containsKey(ability);
    }

    public CooldownMarker getCooldownMarker(ActiveAbility ability) {
        return cooldownMarkerMap.get(ability);
    }

    public void applyCooldown(ActiveAbility ability, int duration) {
        cooldownMarkerMap.put(ability, new CooldownMarker(duration));
    }

    public boolean isActivated(DurableAbility<?> ability) {
        return stackableMarkerMap.containsKey(ability) || unstackableMarkerMap.containsKey(ability);
    }

    public UnstackableMarker getUnstackableMarker(UnstackableAbility ability) {
        return unstackableMarkerMap.get(ability);
    }

    public void applyUnstackable(UnstackableAbility ability, LivingEntity owner, int lvl, float reScaling) {
        unstackableMarkerMap.put(ability, UnstackableMarker.create(ability, owner, lvl, reScaling));
    }

    public StackableMarker getStackableMarker(StackableAbility ability) {
        return stackableMarkerMap.get(ability);
    }

    public void applyStackable(StackableAbility ability, LivingEntity owner, int lvl, float reScaling) {
        stackableMarkerMap.put(ability, StackableMarker.create(ability, owner, lvl, reScaling));
    }

    public void tierStack(StackableAbility ability, LivingEntity owner) {
        stackableMarkerMap.get(ability).tierStack(ability, owner);
    }

    private Map<ActiveAbility, CooldownMarker> getCooldownMarkerMap() {
        return cooldownMarkerMap;
    }

    private Map<ActiveAbility, StackableMarker> getStackableMarkerMap() {
        return stackableMarkerMap;
    }

    public Map<ActiveAbility, UnstackableMarker> getUnstackableMarkerMap() {
        return unstackableMarkerMap;
    }
}
