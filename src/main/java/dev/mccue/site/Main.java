package dev.mccue.site;

import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.site.context.Context;

import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        var router = RegexRouter.<Context>builder()
                .addMapping("GET", Pattern.compile("/user/(?<id>.*)"), Handlers::byId)
                .addMapping("GET", Pattern.compile("/create"), Handlers::create)
                .addMapping("GET", Pattern.compile("/"), Handlers::index)
                .build();
        var context = Context.start();

        String host = "0.0.0.0";
        int port = 3213;
        new Server(router, context).start(host, port);
    }
}