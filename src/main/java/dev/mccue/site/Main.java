package dev.mccue.site;

import dev.mccue.site.context.Context;
import org.microhttp.EventLoop;
import org.microhttp.Options;

public class Main {
    public static void main(String[] args) throws Exception {
        var ctx = Context.start();
        var router = new Router<Context>()
                .addPath("GET", "/user", Handlers::userOne)
                .addPath("POST", "/create", Handlers::create)
                .addPath("GET", "/", Handlers::index);

        String host = "0.0.0.0";
        int port = 3213;

        var loop = new EventLoop(
                new Options().withHost(host).withPort(port),
                new MicrohttpHandler(host, port, ctx, router)
        );
        loop.start();
        loop.join();
    }
}