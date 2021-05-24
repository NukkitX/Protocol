package org.cloudburstmc.protocol.java.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;

import java.lang.reflect.Type;
import java.util.UUID;

public class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

    @Override
    public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        UUID id = jsonObject.has("id") ? context.deserialize(jsonObject.get("id"), UUID.class) : null;
        String name = jsonObject.has("name") ? jsonObject.getAsJsonPrimitive("name").getAsString() : null;
        return new GameProfile(id, name);
    }

    @Override
    public JsonElement serialize(GameProfile profile, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if (profile.getId() != null) {
            jsonObject.add("id", context.serialize(profile.getId()));
        }
        if (profile.getName() != null) {
            jsonObject.addProperty("name", profile.getName());
        }
        return jsonObject;
    }
}
