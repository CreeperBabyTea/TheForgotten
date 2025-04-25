package dev.pages.creeperbabytea.the_forgotten;

import dev.pages.creeperbabytea.the_forgotten.api.common.ApiInit;
import dev.pages.creeperbabytea.the_forgotten.api.common.ApiEventHandler;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.ListenerInit;
import dev.pages.creeperbabytea.the_forgotten.data.DataEventHandler;
import dev.pages.creeperbabytea.the_forgotten.command.ModCommands;
import net.neoforged.bus.api.IEventBus;

public class StartUpHandler {
    public static void init(IEventBus mod, IEventBus game) {
        Registrations.init(mod, game);
        ApiInit.init(mod, game);

        ModCommands.init(game);
        ApiEventHandler.init(mod, game);

        DataEventHandler.init(mod);
    }
}
