package dev.mccue.site;

import dev.mccue.site.context.Context;
import org.microhttp.Handler;
import org.microhttp.Request;
import org.microhttp.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MicrohttpHandler implements Handler {
    private static final Response NOT_FOUND_RESPONSE =
            new Response(404, IntoResponse.STATUS_TO_REASON.apply(404), List.of(), new byte[]{});

    private final Context ctx;
    private final Router<Context> router;
    private final ExecutorService executor;

    public MicrohttpHandler(Context ctx, Router<Context> router) {
        this.ctx = ctx;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(50);
    }

    private Response errorResponse(Throwable t) {
        var sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return new Response(
                500,
                IntoResponse.STATUS_TO_REASON.apply(500),
                List.of(),
                sw.toString().getBytes(StandardCharsets.UTF_8)
        );
    }
    @Override
    public void handle(Request request, Consumer<Response> consumer) {
        executor.submit(() -> {
            try {
                var response = router.handle(ctx, request).orElse(null);
                System.out.println(response);
                if (response == null) {
                    consumer.accept(NOT_FOUND_RESPONSE);
                }
                else {
                    consumer.accept(response.intoResponse());
                }
            }
            catch (Throwable t) {
                consumer.accept(errorResponse(t));
            }
        });
    }
}
