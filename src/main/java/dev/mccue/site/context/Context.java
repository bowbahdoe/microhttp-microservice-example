package dev.mccue.site.context;

import dev.mccue.site.SiteDatabase;
import dev.mccue.site.persistence.UserPersistence;
import dev.mccue.site.service.AuthService;
import dev.mccue.site.service.UserService;

import java.io.IOException;

public record Context(
        AuthService authService,
        UserPersistence userPersistence,
        UserService userService
) implements
        HasAuthService,
        HasUserPersistence,
        HasUserService
{
    public static Context start() throws IOException {
        AuthService authService = null;/* new AuthService(
                System.getenv("AUTH0_DOMAIN"),
                System.getenv("AUTH0_CLIENT_ID"),
                System.getenv("AUTH0_CLIENT_SECRET"),
                System.getenv("AUTH0_ISSUER_BASE_URL")
        );*/

        var db = SiteDatabase.create();
        var userPersistence = new UserPersistence(db);
        var userService = new UserService(userPersistence);
        return new Context(authService, userPersistence, userService);
    }
}
