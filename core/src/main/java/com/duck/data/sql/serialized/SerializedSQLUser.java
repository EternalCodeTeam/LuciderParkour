package com.duck.data.sql.serialized;

import com.duck.LuciderParkour;
import com.duck.data.sql.SQLManager;
import com.duck.data.sql.SQLStructureBuilder;
import com.duck.data.sql.SerializableSQLObject;
import com.duck.feature.timer.TimerType;
import com.duck.user.User;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import panda.std.stream.PandaStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SerializedSQLUser implements SerializableSQLObject<User, UUID> {

    private final SQLManager sqlManager = LuciderParkour.getInstance().getSqlManager();


    @SneakyThrows
    @Override
    public User serialize(Connection connection, User obj) {
        String statement = new SQLStructureBuilder.StructureBuilder()
            .update("users")
            .set("uuid", "?")
            .set("username", "?")
            .set("active_id", "?")
            .set("level", "?")
            .set("xp", "?")
            .set("coins", "?")
            .set("completed_arenas", "?")
            .set("scores_limit", "?")
            .set("timer_type", "?")
            .build();

        PreparedStatement preparedStatement = connection
            .prepareStatement(statement);

        preparedStatement.setObject(1, obj.getUuid());
        preparedStatement.setString(2, obj.getUsername());
        preparedStatement.setInt(3, obj.getActiveId());
        preparedStatement.setInt(4, obj.getLevel());
        preparedStatement.setDouble(5, obj.getXp());
        preparedStatement.setDouble(6, obj.getCoins());
        preparedStatement.setString(7, processCompletedArenas(obj.getCompletedArenasIds()));
        preparedStatement.setInt(8, obj.getScoresLimit());
        preparedStatement.setObject(9, obj.getTimerType());

        preparedStatement.execute();

        return obj;
    }

    @SneakyThrows
    @Override
    public Map<UUID, User> deserialize(Connection connection) {
        Map<UUID, User> map = Maps.newHashMap();

        String statement = new SQLStructureBuilder.StructureBuilder()
            .selectAsDistinct("*")
            .from("?")
            .build();

        PreparedStatement preparedStatement = connection
            .prepareStatement(statement);

        preparedStatement.setString(1, "users");
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            UUID uuid = resultSet.getObject("uuid", UUID.class);

            map.put(uuid, User.builder()
                    .uuid(uuid)
                    .username(resultSet.getString("username"))
                    .activeId(resultSet.getInt("active_id"))
                    .level(resultSet.getInt("level"))
                    .xp(resultSet.getDouble("xp"))
                    .coins(resultSet.getDouble("coins"))
                    .completedArenasIds(processCompletedArenas(resultSet.getString("completed_arenas")))
                    .scoresLimit(resultSet.getInt("scores_limit"))
                    .timerType(sqlManager.getEnum(TimerType.class, resultSet, "timer_type"))
                    .build()
            );
        }



        return ImmutableMap.<UUID, User>builder()
            .putAll(map)
            .build();
    }

    private String processCompletedArenas(List<Integer> integerList){
        return StringUtils.join(integerList, ", ");
    }

    private List<Integer> processCompletedArenas(String processedString){
        return PandaStream
            .of(processedString.split(", "))
            .map(Integer::parseInt)
            .toList();
    }

}
