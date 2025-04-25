package dev.pages.creeperbabytea.the_forgotten;

import com.mojang.logging.LogUtils;
import dev.pages.creeperbabytea.common.networking.Networking;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

@Mod(TheForgotten.MODID)
public class TheForgotten
{
    public static final String TITLE = "The Forgotten";
    public static final String MODID = "the_forgotten";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String VERSION = "0.0.1";

    public static IEventBus MOD;
    public static IEventBus GAME;

    public static Networking NETWORKING;

    public TheForgotten(IEventBus modEventBus, ModContainer modContainer) {
        MOD = modEventBus;
        GAME = NeoForge.EVENT_BUS;
        NETWORKING = new Networking(modLoc("main"), VERSION);

        StartUpHandler.init(MOD, GAME);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
