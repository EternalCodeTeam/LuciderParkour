package com.duck.api.user;

import java.util.UUID;

public interface UserAPI {

    void createUser(User user);

    void removeUser();

    void set(UUID uuid,UserValueType userValueType);

    <T> T get(UUID uuid, UserValueType userValueType, Class<T> genericClass);


}
