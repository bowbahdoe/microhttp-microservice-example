package dev.mccue.site.context;

import dev.mccue.site.persistence.UserPersistence;

public interface HasUserPersistence {
    UserPersistence userPersistence();
}
