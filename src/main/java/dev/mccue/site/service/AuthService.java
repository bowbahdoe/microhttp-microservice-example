package dev.mccue.site.service;

import com.auth0.client.auth.AuthAPI;

public final class AuthService {
    private final AuthAPI authAPI;
    private final String auth0IssuerBaseUrl;

    public AuthService(
            String auth0BaseUrl,
            String auth0ClientId,
            String auth0Secret,
            String auth0IssuerBaseUrl
    ) {
        this.authAPI = new AuthAPI(auth0BaseUrl, auth0ClientId, auth0Secret);
        this.auth0IssuerBaseUrl = auth0IssuerBaseUrl;
    }

    public String authorizeUrl() {
       return this.authAPI.authorizeUrl(this.auth0IssuerBaseUrl).build();
    }

    public String logoutUrl() {
        return this.authAPI.logoutUrl("", true).build();
    }
}
