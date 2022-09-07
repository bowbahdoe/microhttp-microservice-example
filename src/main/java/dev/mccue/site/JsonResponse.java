package dev.mccue.site;

import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Response;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.microhttp.Header;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record JsonResponse(int status, Map<String, String> headers, JsonValue json) implements IntoResponse {
    public JsonResponse(int status, JsonValue value) {
        this(status, Map.of(), value);
    }

    public JsonResponse(JsonValue value) {
        this(200, value);
    }

    @Override
    public Response intoResponse() {
        var sw = new StringWriter();
        Json.createWriter(sw).write(json);
        var newHeaders = new HashMap<>(headers);
        newHeaders.put("Content-Type", "application/json");
        return new Response(
                status,
                newHeaders,
                Body.fromString(sw.toString())
        );
    }
}
