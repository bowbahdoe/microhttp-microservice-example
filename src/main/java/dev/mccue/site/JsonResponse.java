package dev.mccue.site;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.microhttp.Header;
import org.microhttp.Response;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public record JsonResponse(int status, List<Header> headers, JsonValue json) implements IntoResponse {
    public JsonResponse(int status, JsonValue value) {
        this(status, List.of(), value);
    }

    public JsonResponse(JsonValue value) {
        this(200, value);
    }

    @Override
    public Response intoResponse() {
        var sw = new StringWriter();
        Json.createWriter(sw).write(json);
        var newHeaders = new ArrayList<>(headers);
        newHeaders.add(new Header("Content-Type", "application/json"));
        return new Response(
                status,
                IntoResponse.STATUS_TO_REASON.apply(status),
                newHeaders,
                sw.toString().getBytes(StandardCharsets.UTF_8)
        );
    }
}
