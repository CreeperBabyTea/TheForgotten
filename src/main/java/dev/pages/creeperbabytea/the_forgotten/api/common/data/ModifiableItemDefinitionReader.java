package dev.pages.creeperbabytea.the_forgotten.api.common.data;

import dev.pages.creeperbabytea.common.register.EntryInfo;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemInfo;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ModifiableItemDefinitionReader extends SimpleJsonResourceReloadListener<EntryInfo.BuiltInEntryInfo<Item>> {
    public static final String FOLDER_PATH = "tea/item/modifiable";

    public ModifiableItemDefinitionReader() {
        super(ModifiableItemInfo.CODEC, FileToIdConverter.json(FOLDER_PATH));
    }

    @Override
    protected void apply(Map<ResourceLocation, EntryInfo.BuiltInEntryInfo<Item>> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        object.values().forEach(Registrations.ITEMS::putInfo);
    }
}
