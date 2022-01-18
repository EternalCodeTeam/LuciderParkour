package com.duck.data.sql;

import java.sql.Connection;
import java.util.Map;

public interface SerializableSQLObject<T, K> {
    T serialize(Connection connection, T obj);
    Map<K, T> deserialize(Connection connection);
}
