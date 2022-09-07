package dev.mccue.site;

import dev.mccue.rosie.Request;
import dev.mccue.site.context.HasAuthService;
import dev.mccue.site.context.HasUserService;
import dev.mccue.site.domain.User;
import jakarta.json.Json;
import jakarta.json.JsonValue;

public final class Handlers {
    private Handlers() {}

    public static <Ctx extends HasUserService> JsonResponse userOne(Ctx ctx, Request request) {
        var userService = ctx.userService();
        var user = userService.byId(1).orElse(null);
        if (user == null) {
            return new JsonResponse(404, Json.createValue("Not Found"));
        }
        else {
            return new JsonResponse(user.toJson());
        }
    }

    public static <Ctx extends HasUserService> JsonResponse create(Ctx ctx, Request request) {
        var userService = ctx.userService();
        userService.createRandom();

        var arrayBuilder = Json.createArrayBuilder();
        for (int i = 1; i <= 100; i++) {
            arrayBuilder.add(userService.byId(i).map(User::toJson).orElse(JsonValue.NULL));
        }
        return new JsonResponse(arrayBuilder.build());
    }

    public static <Ctx> JsonResponse index(Ctx ctx, Request request) {
        return new JsonResponse(Json.createValue("Welcome!"));
    }
}
