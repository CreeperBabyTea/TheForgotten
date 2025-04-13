package dev.pages.creeperbabytea.the_forgotten;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TheForgotten.MODID)
public class TheForgotten
{
    public static final String MODID = "the_forgotten";
    public static final Logger LOGGER = LogManager.getLogger();

    public static IEventBus MOD;
    public static IEventBus GAME;

    public TheForgotten(IEventBus modEventBus, ModContainer modContainer) {
        MOD = modEventBus;
        GAME = NeoForge.EVENT_BUS;
        StartUpHandler.init(MOD, GAME);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
