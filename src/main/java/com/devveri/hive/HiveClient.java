package com.devveri.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HiveClient {

    private static Logger LOG = LoggerFactory.getLogger(HiveClient.class);

    public void executeFile(final String fileName) throws IOException, SQLException, ClassNotFoundException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        executeQuery(content);
    }

    public void executeQuery(String content) throws SQLException, IOException, ClassNotFoundException {
        for (String query : content.split(";")) {
            executeSQL(query);
        }
    }

    private void executeSQL(final String query) throws ClassNotFoundException, SQLException, IOException {
        final long start = System.currentTimeMillis();
        LOG.info("Executing hive query: " + query);

        // read properties
        Properties config = getConfig();
        Class.forName(config.getProperty("jdbc.driver"));
        final String url = config.getProperty("jdbc.url"),
                user = config.getProperty("jdbc.user"),
                pass = config.getProperty("jdbc.pass");
        final long wait = (Long.parseLong(config.getProperty("jdbc.wait")) * 1000L);
        final long timeout = System.currentTimeMillis() + (Long.parseLong(config.getProperty("jdbc.timeout")) * 1000L);

        try (Connection connection = getConnection(url, user, pass, timeout, wait)) {
            try (Statement stmt = connection.createStatement()) {
                if (query.trim().toLowerCase().startsWith("select")) {
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        processResults(rs);
                    }
                } else {
                    stmt.execute(query);
                }
            }
        }
        LOG.info("Finished in {} ms", (System.currentTimeMillis() - start));
    }

    private Connection getConnection(String url, String user, String pass, long timeout, long wait) throws SQLException {
        while (System.currentTimeMillis() < timeout) {
            try {
                Connection connection = DriverManager.getConnection(url, user, pass);
                return connection;
            } catch (SQLException e) {
                LOG.warn("Failed to get connection, will wait {} ms...", wait);
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        throw new SQLException("Failed to connect database");
    }

    private void processResults(ResultSet rs) throws SQLException {
        int rows = 0, columns = 0;
        while (rs.next()) {
            if (rows++ == 0) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (i > 1) {
                        System.out.print("\t");
                    }
                    System.out.print(rs.getMetaData().getColumnName(i));
                    ++columns;
                }
                System.out.println();
            }
            for (int i = 1; i <= columns; i++) {
                if (i > 1) {
                    System.out.print("\t");
                }
                System.out.print(rs.getObject(i));
            }
            System.out.println();
        }
    }

    private Properties getConfig() throws IOException {
        Properties properties = new Properties();
        final String fileName = System.getProperty("config") == null ?
                "/etc/hive-client/config.properties" : System.getProperty("config");
        try (InputStream in = new FileInputStream(fileName)) {
            properties.load(in);
        }
        return properties;
    }

}
