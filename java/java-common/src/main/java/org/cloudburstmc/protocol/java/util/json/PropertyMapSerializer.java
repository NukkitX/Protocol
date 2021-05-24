package org.cloudburstmc.protocol.java.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.cloudburstmc.protocol.java.data.profile.property.Property;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;

import java.lang.reflect.Type;
import java.util.Map;

public class PropertyMapSerializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

    @Override
    public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PropertyMap propertyMap = new PropertyMap();
        if (json instanceof JsonArray) {
            for (JsonElement element : json.getAsJsonArray()) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject object = element.getAsJsonObject();
                String name = object.getAsJsonPrimitive("name").getAsString();
                String value = object.getAsJsonPrimitive("value").getAsString();
                if (object.has("signature")) {
                    propertyMap.put(name, new Property(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                } else {
                    propertyMap.put(name, new Property(name, value));
                }
            }
        }
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (!entry.getValue().isJsonArray()) {
                    continue;
                }
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    propertyMap.put(entry.getKey(), new Property(entry.getKey(), element.getAsString()));
                }
            }
        }
        return propertyMap;
    }

    @Override
    public JsonElement serialize(PropertyMap propertyMap, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (Property property : propertyMap.values()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", property.getName());
            object.addProperty("value", property.getValue());
            if (property.getSignature() != null) {
                object.addProperty("signature", property.getSignature());
            }
            array.add(object);
        }
        return array;
    }
}
