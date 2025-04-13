package dev.pages.creeperbabytea.the_forgotten.api.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class DataReader {
    /*public static JsonObject parse(IResource resource) {
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
        try {
            reader.close();
        } catch (IOException e) {
            TheForgotten.LOGGER.error("Failed to parse resource: {}", resource.getLocation(), e);
        }
        return json;
    }

    @Nullable
    public static JsonObject readJson(IResourceManager manager, ResourceLocation fileLoc) {
        try {
            IResource resource = manager.getResource(fileLoc);
            return parse(resource);
        } catch (IOException e) {
            TheForgotten.LOGGER.error("Failed to read data from {}", fileLoc, e);
        }
        return null;
    }

    public static List<JsonObject> readAllJson(IResourceManager manager, ResourceLocation folderLoc) {
        try {
            List<IResource> resources = manager.getAllResources(folderLoc);
            return resources.stream().map(DataReader::parse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
