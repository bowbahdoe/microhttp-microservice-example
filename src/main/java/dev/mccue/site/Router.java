package dev.mccue.site;

import org.microhttp.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public final class Router<Ctx> {
    public record Path<Ctx>(
            String method,
            Pattern path,
            BiFunction<? super Ctx, Request, IntoResponse> f
    ) {}

    private final List<Path<Ctx>> paths;

    public Router() {
        this.paths = new ArrayList<>();
    }

    public Router<Ctx> addPath(String method,
                               String path,
                               BiFunction<? super Ctx, Request, IntoResponse> f) {
        this.paths.add(new Path<>(method, Pattern.compile(path), f));
        return this;
    }

    public Optional<IntoResponse> handle(Ctx context, Request request) {
        for (var path : paths) {
            if (path.method.equalsIgnoreCase(request.method()) && path.path.asPredicate().test(request.uri())) {
                return Optional.of(path.f.apply(context, request));
            }
        }

        return Optional.empty();
    }
}
