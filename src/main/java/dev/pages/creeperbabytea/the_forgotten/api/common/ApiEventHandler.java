package dev.pages.creeperbabytea.the_forgotten.api.common;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.data.ModifiableItemDefinitionReader;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

public class ApiEventHandler {
    public static void init(IEventBus mod, IEventBus game) {
        bothSideData(game, mod, TheForgotten.modLoc("modifiable_item"), new ModifiableItemDefinitionReader());
        game.addListener(ModifiersContainer::onTooltipAttaching);
    }

    public static void bothSideData(IEventBus game, IEventBus mod, ResourceLocation id, SimplePreparableReloadListener<?> listener) {
        game.addListener(AddServerReloadListenersEvent.class, e -> e.addListener(id, listener));
        mod.addListener(AddClientReloadListenersEvent.class, e -> e.addListener(id, listener));
    }
}
