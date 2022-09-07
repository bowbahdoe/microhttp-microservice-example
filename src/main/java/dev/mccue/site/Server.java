package dev.mccue.site;

import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.Response;
import dev.mccue.rosie.microhttp.MicrohttpAdapter;
import dev.mccue.site.context.Context;
import org.microhttp.EventLoop;
import org.microhttp.Options;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class Server {
    private static final Response NOT_FOUND_RESPONSE =
            new Response(404, Body.empty());

    private final RegexRouter<Context> router;
    private final ExecutorService executor;

    public Server(RegexRouter<Context> router) {
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

    private void handle(String host, int port, org.microhttp.Request request, Consumer<org.microhttp.Response> consumer) {
        executor.submit(() -> {
            try {
                var response = router.handleRequest(
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

    public void start(String host, int port) throws IOException, InterruptedException {
        var loop = new EventLoop(
                new Options().withHost(host).withPort(port),
                ((request, consumer) -> handle(host, port, request, consumer))
        );
        loop.start();
        loop.join();
    }
}
