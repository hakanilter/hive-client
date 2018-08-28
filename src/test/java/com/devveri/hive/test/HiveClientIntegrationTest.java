package com.devveri.hive.test;

import com.devveri.hive.HiveClient;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class HiveClientIntegrationTest {

    @Test
    public void test() throws SQLException, IOException, ClassNotFoundException {
        HiveClient hiveClient = new HiveClient();
        hiveClient.executeQuery("SELECT 1 AS test; SELECT 2 AS test");
    }

}
