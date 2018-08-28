package com.devveri.hive;

public final class HiveTool {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            error();
        }
        final String argument = args[0];
        final String value = args[1];
        if ("-e".equals(argument) || "-q".equals(argument)) {
            new HiveClient().executeQuery(value);
        } else if ("-f".equals(argument)) {
            new HiveClient().executeFile(value);
        } else {
            error();
        }
    }

    private static void error() {
        System.err.println("Error! You should provide a query!");
        System.err.println("Usage: ");
        System.err.println("\tHiveClient -e \"SELECT * FROM test\"");
        System.err.println("OR");
        System.err.println("\tHiveClient -f query.sql");
        System.exit(0);
    }

}
