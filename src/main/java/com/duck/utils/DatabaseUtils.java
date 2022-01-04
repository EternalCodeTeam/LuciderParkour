package com.duck.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public final class DatabaseUtils {

    private static final String SELECT_BY_FIELD = "SELECT ? FROM ? WHERE ? = ?";
    private static final String SELECT_OBJECT = "SELECT ? FROM ?";

    public static Object select(String fieldName, String table,
                              String whereField, Object whereValue, Connection connection) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_FIELD);

        preparedStatement.setString(1, fieldName);
        preparedStatement.setString(2, table);
        preparedStatement.setString(3, whereField);
        preparedStatement.setObject(4, whereValue);

        ResultSet resultSet = preparedStatement.executeQuery();

        Object currentValue = resultSet.getObject(fieldName);

        preparedStatement.close();
        resultSet.close();

        return currentValue;
    }

    public static List<Object> selectAllValues(String fieldName, String table, Connection connection) throws SQLException {

        List<Object> objects = new java.util.ArrayList<>(Collections.emptyList());

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OBJECT);

        preparedStatement.setString(1, fieldName);
        preparedStatement.setString(2, table);

        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            objects.add(resultSet.getObject(fieldName));
        }

        preparedStatement.close();
        resultSet.close();

        return objects;
    }

    public static List<Object> selectAllValuesWhere(String fieldName, String table,
                                                    String whereField, Object whereValue,
                                                    Connection connection) throws SQLException {

        List<Object> objects = new java.util.ArrayList<>(Collections.emptyList());

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_FIELD);

        preparedStatement.setString(1, fieldName);
        preparedStatement.setString(2, table);
        preparedStatement.setString(3, whereField);
        preparedStatement.setObject(4, whereValue);


        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            objects.add(resultSet.getObject(fieldName));
        }

        preparedStatement.close();
        resultSet.close();

        return objects;
    }
}
