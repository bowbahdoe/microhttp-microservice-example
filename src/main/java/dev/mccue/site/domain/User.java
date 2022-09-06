package dev.mccue.site.domain;

import jakarta.json.Json;
import jakarta.json.JsonValue;

public record User(int id, String email) {
    public JsonValue toJson() {
        var obj = Json.createObjectBuilder();
        obj = obj.add("id", id);
        obj = email == null ? obj.addNull("email") : obj.add("email", email);
        return obj.build();
    }
}
