package dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CooldownMarker {
    public static final Codec<CooldownMarker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("duration").forGetter(CooldownMarker::getDuration)
    ).apply(instance, CooldownMarker::new));

    private int duration;

    public CooldownMarker(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return 0;
    }

    /**
     * @return true如果技能冷却未结束
     */
    public boolean tick() {
        duration --;
        return duration > 0;
    }
}
