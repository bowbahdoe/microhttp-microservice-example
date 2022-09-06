package dev.mccue.site.context;

import dev.mccue.site.service.UserService;

public interface HasUserService {
    UserService userService();
}
