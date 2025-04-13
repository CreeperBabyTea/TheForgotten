package dev.pages.creeperbabytea.the_forgotten.api.common.data;

import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class ModifiableItemDefinitionReader {
    public static final String FOLDER_PATH = "data/items/modifiable";
    public static final ResourceLocation FOLDER_LOCATION = TheForgotten.modLoc(FOLDER_PATH);

    public static void onServerStart(ServerStartedEvent event) {
        /*IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        List<JsonObject> itemJsons = DataReader.readAllJson(resourceManager, FOLDER_LOCATION);
        itemJsons.forEach(json -> {
            ResourceLocation itemName = new ResourceLocation(json.get("item").getAsString());
            Item item = ForgeRegistries.ITEMS.getValue(itemName);
            if (item != null) {
                JsonObject dataTag = json.getAsJsonObject("modifiable");
                ModifiableItemState state = new ModifiableItemState(item);
                Registrations.ITEMS.putState(item, state);
                if (dataTag.has("offhand") && dataTag.get("offhand").getAsBoolean())
                    state.setAllowedOffhand(true);


            }
        });*/   //TODO
    }
}
