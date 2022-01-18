package com.duck.data.sql.serialized;

import com.duck.data.sql.SerializableSQLObject;
import com.duck.parkour.Parkour;

import java.sql.Connection;
import java.util.Map;

public class SerializedSQLParkour implements SerializableSQLObject<Parkour, Integer> {


    @Override
    public Parkour serialize(Connection connection, Parkour obj) {
        return null;
    }

    @Override
    public Map<Integer, Parkour> deserialize(Connection connection) {
        return null;
    }
}
