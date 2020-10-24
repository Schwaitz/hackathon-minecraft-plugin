package com.Buildathon.Handlers;

import com.Buildathon.Buildathon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;

public class ConnectionHandler {

    Buildathon plugin;

    String username;
    String password;
    String hostname;
    String database;
    int port;

    String url;

    Connection connection;


    public ConnectionHandler(Buildathon plugin) {
        this.plugin = plugin;

        username = this.plugin.config.getString("mysql_user");
        password = this.plugin.config.getString("mysql_pass");
        hostname = this.plugin.config.getString("mysql_hostname");
        database = this.plugin.config.getString("mysql_database");
        port = this.plugin.config.getInt("mysql_port");

        url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
    }

    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.logToFile("Failed to connect (JDBC Driver is unavailable)");
            plugin.getLogger().severe(String.format("[%s] JDBC Driver is unavailable", plugin.getDescription().getName()));
            return;
        }
        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            plugin.logToFile("Failed to connect (Error connecting to MySQL Database)");
            plugin.getLogger().severe(String.format("[%s] Error connecting to MySQL Database", plugin.getDescription().getName()));
            e.printStackTrace();
        }
    }

    //    public void insertIntoDatabase(String table, String colName, String value) {
//        String sql = "INSERT INTO Users" + table + " (" + colName + ") VALUES ('" + value + "');";
//
//        try {
//            PreparedStatement stmt = connection.prepareStatement(sql);
//
//
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void queryDatabase(String table, String col, String search, Player p) {
        String sql = "SELECT * FROM ? WHERE ? = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, table);
            stmt.setString(2, col);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                plugin.sendInfo(p, results.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTeams(Player p) {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                connectToDatabase();

                String sql = "SELECT * FROM Users;";

                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);

                    ResultSet results = stmt.executeQuery();

                    HashMap<String, List<String>> teams = new HashMap<>();

                    while (results.next()) {
                        if (teams.containsKey(results.getString("team"))) {
                            teams.get(results.getString("team")).add(results.getString("name"));
                        }
                        else {
                            List<String> tempList = new ArrayList<>();
                            tempList.add(results.getString("name"));
                            teams.put(results.getString("team"), tempList);
                        }
                    }

                    String sendString = "\n";
                    sendString += ChatColor.GRAY + "--------[" + ChatColor.BLUE + "Buildathon Teams" + ChatColor.GRAY + "]--------\n";

                    for (String k : teams.keySet()) {
                        sendString += ChatColor.BLUE + k + ": " + ChatColor.GRAY;

                        List<String> tempList = teams.get(k);

                        for (String s : tempList) {
                            sendString += s + ", ";
                        }

                        sendString = sendString.substring(0, sendString.length() - 2);
                        sendString += "\n";
                    }

                    p.sendMessage(sendString);


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                disconnectFromDatabase();
            }
        };

        r.runTaskAsynchronously(plugin);
    }


    public void addUser(String name, String team) {

        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                connectToDatabase();

                String sql = "INSERT INTO Users (name, team) VALUES ('" + name + "', '" + team + "');";

                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);

                    stmt.executeUpdate();
                    plugin.logToFile("Added new user '" + name + "' to team '" + team + "'");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                disconnectFromDatabase();
            }
        };

        r.runTaskAsynchronously(plugin);

    }


    public boolean isConnected() {
        try {
            return connection.isValid(5);
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("[%s] Error checking connection status", plugin.getDescription().getName()));
            e.printStackTrace();
            return false;
        }
    }


    public void createTable(String table, String col) {
        String sql = "CREATE TABLE ? (id MEDIUMINT NOT NULL AUTO_INCREMENT, ? VARCHAR(60) NOT NULL, PRIMARY KEY(id));";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, table);
            stmt.setString(2, col);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void disconnectFromDatabase() {
        // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection != null && !connection.isClosed()) { //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

