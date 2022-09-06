package dev.mccue.site.context;

import dev.mccue.site.service.AuthService;

public interface HasAuthService {
    AuthService authService();
}
