package dev.pages.creeperbabytea.the_forgotten.data.provider;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import net.minecraft.data.PackOutput;

public class ParticleDescriptionProvider extends net.neoforged.neoforge.common.data.ParticleDescriptionProvider {
    public ParticleDescriptionProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {

    }

    @Override
    public String getName() {
        return super.getName() + " of " + TheForgotten.TITLE;
    }
}
