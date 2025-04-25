package dev.pages.creeperbabytea.the_forgotten.api.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataReader {
    public static JsonObject parse(Resource resource) {
        try {
            InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static JsonObject readJson(ResourceManager manager, ResourceLocation fileLoc) {
        Optional<Resource> resource = manager.getResource(fileLoc);
        return resource.map(DataReader::parse).orElse(null);
    }

    public static List<JsonObject> readAllJson(ResourceManager manager, ResourceLocation folderLoc) {
        List<Resource> resources = manager.getResourceStack(folderLoc);
        return resources.stream().map(DataReader::parse).collect(Collectors.toList());
    }
}
