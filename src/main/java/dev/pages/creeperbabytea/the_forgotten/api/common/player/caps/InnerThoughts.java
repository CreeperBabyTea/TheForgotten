package dev.pages.creeperbabytea.the_forgotten.api.common.player.caps;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class InnerThoughts implements ICapabilityContent<InnerThoughts> {
    public static final ResourceLocation SOUL = TheForgotten.modLoc("soul");
    public static final ResourceLocation RATIONALITY = TheForgotten.modLoc("rationality");
    public static final ResourceLocation INTELLECT = TheForgotten.modLoc("intellect");

    private float soul = 0.0f;
    private int rationality = 100;
    private int intellect = 100;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag base = new CompoundTag();
        base.putFloat(SOUL.toString(), soul);
        base.putInt(RATIONALITY.toString(), rationality);
        base.putInt(INTELLECT.toString(), intellect);
        return base;
    }

    @Override
    public InnerThoughts deserializeNBT(CompoundTag nbt) {
        this.soul = nbt.getFloat(SOUL.toString());
        this.rationality = nbt.getInt(RATIONALITY.toString());
        this.intellect = nbt.getInt(INTELLECT.toString());
        return this;
    }

    public float getSoul() {
        return soul;
    }

    public int getRationality() {
        return rationality;
    }

    public int getIntellect() {
        return intellect;
    }

    public void setSoul(float soul) {
        this.soul = soul;
    }

    public void setRationality(int rationality) {
        this.rationality = rationality;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    @Override
    public String toString() {
        return "InnerThoughts{" +
                "soul=" + soul +
                ", rationality=" + rationality +
                ", intellect=" + intellect +
                '}';
    }
}
