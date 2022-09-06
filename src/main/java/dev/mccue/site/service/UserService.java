package dev.mccue.site.service;

import dev.mccue.site.domain.User;
import dev.mccue.site.persistence.UserPersistence;

import java.util.Optional;

public final class UserService {
    private final UserPersistence userPersistence;

    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public Optional<User> byId(int id) {
        return this.userPersistence.byId(id);
    }

    public void createRandom() {
        this.userPersistence.createRandom();
    }
}
