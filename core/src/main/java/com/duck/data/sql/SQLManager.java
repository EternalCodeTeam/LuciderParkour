package com.duck.data.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sql manager class.
 */
@Getter
public class SQLManager {

    private HikariConfig config;
    private HikariDataSource hikariDataSource;


    /**
     *
     * @param url SQL url
     * @param username sql user name
     * @param password sql user password
     * make default connection.
     */
    public void makeConnection(String url, String username, String password){
        this.config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        this.hikariDataSource = new HikariDataSource(config);
    }

    /**
     *
     * @return existed connection.
     * @throws SQLException when connection is wrong.
     */
    public Connection getExistedConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }


    public <E extends Enum<E>> E getEnum(Class<E> enumClass, ResultSet rs, String columnName)
        throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        } else {/*from ww w.  j  av  a 2s  . c  o m*/
            return Enum.valueOf(enumClass, value);
        }
    }
}
