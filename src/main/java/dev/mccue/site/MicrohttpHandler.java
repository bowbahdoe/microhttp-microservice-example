package dev.mccue.site;

import dev.mccue.rosie.Body;
import dev.mccue.rosie.Response;
import dev.mccue.rosie.microhttp.MicrohttpAdapter;
import dev.mccue.site.context.Context;
import org.microhttp.Handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MicrohttpHandler implements Handler {
    private static final Response NOT_FOUND_RESPONSE =
            new Response(404, Map.of(), Body.empty());

    private final Context ctx;
    private final Router<Context> router;
    private final ExecutorService executor;
    private final int port;
    private final String host;

    public MicrohttpHandler(String host, int port, Context ctx, Router<Context> router) {
        this.port = port;
        this.host = host;
        this.ctx = ctx;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(50);
    }

    private Response errorResponse(Throwable t) {
        var sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return new Response(
                500,
                Map.of(),
                Body.fromString(sw.toString())
        );
    }
    @Override
    public void handle(org.microhttp.Request request, Consumer<org.microhttp.Response> consumer) {
        executor.submit(() -> {
            try {
                var response = router.handle(
                        ctx,
                        MicrohttpAdapter.fromMicrohttpRequest(host, port, request)
                ).orElse(null);
                if (response == null) {
                    consumer.accept(MicrohttpAdapter.toMicrohttpResponse(NOT_FOUND_RESPONSE));
                }
                else {
                    consumer.accept(MicrohttpAdapter.toMicrohttpResponse(response.intoResponse()));
                }
            }
            catch (Throwable t) {
                consumer.accept(MicrohttpAdapter.toMicrohttpResponse(errorResponse(t)));
            }
        });
    }
}
