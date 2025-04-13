package dev.pages.creeperbabytea.the_forgotten;

import dev.pages.creeperbabytea.the_forgotten.api.common.EventHandler;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.ListenerInit;
import dev.pages.creeperbabytea.the_forgotten.api.common.player.Capabilities;
import net.neoforged.bus.api.IEventBus;

public class StartUpHandler {
    public static void init(IEventBus mod, IEventBus game) {
        Registrations.init(mod, game);
        Capabilities.init(mod, game);
        EventHandler.init(game);
        ListenerInit.init();
    }
}
