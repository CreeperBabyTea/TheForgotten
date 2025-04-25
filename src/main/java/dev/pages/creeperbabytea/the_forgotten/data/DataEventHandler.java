package dev.pages.creeperbabytea.the_forgotten.data;

import dev.pages.creeperbabytea.the_forgotten.data.provider.ModifiableItemStateProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DataEventHandler {
    public static void init(IEventBus mod) {
        mod.addListener(DataEventHandler::gatherServerData);
    }

    public static void gatherServerData(GatherDataEvent.Client event) {
        //event.createProvider(ParticleDescriptionProvider::new);
        event.createProvider(ModifiableItemStateProvider::new);
    }
}
