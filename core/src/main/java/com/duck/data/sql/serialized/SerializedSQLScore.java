package com.duck.data.sql.serialized;

import com.duck.data.sql.SerializableSQLObject;
import com.duck.scores.ParkourScore;

import java.sql.Connection;
import java.util.Map;
import java.util.UUID;

public class SerializedSQLScore implements SerializableSQLObject<ParkourScore, UUID> {

    @Override
    public ParkourScore serialize(Connection connection, ParkourScore obj) {
        return null;
    }

    @Override
    public Map<UUID, ParkourScore> deserialize(Connection connection) {
        return null;
    }
}
