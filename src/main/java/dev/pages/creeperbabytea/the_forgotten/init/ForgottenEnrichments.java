package dev.pages.creeperbabytea.the_forgotten.init;

import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import dev.pages.creeperbabytea.the_forgotten.items.enrichments.SuperBigBrain;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ForgottenEnrichments {
    public static final DeferredHolder<ItemEnrichment, SuperBigBrain> SUPER_BIG_BRAIN = Registrations.ENRICHMENTS.registerSingleton(
            "super_big_brain", new SuperBigBrain()
    );

    public static void init() {}
}
