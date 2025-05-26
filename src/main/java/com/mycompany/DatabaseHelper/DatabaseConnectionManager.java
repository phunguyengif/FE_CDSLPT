/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DatabaseHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ntphu
 */
public class DatabaseConnectionManager {
    private static final Map<String, String> DB_URLS = new HashMap<>();
    private static final Map<String, String> DB_USERNAMES = new HashMap<>();
    private static final Map<String, String> DB_PASSWORDS = new HashMap<>();

    static {
        // Thông tin kết nối
        DB_URLS.put("Bắc", "jdbc:sqlserver://LAPTOP-GTU45K0B\\SERVER3:1435;databaseName=QLTH;encrypt=true;trustServerCertificate=true");
        DB_USERNAMES.put("Bắc", "sa");
        DB_PASSWORDS.put("Bắc", "12345");

        DB_URLS.put("Nam", "jdbc:sqlserver://LAPTOP-GTU45K0B\\SERVER2:1434;databaseName=QLTH;encrypt=true;trustServerCertificate=true");
        DB_USERNAMES.put("Nam", "sa");
        DB_PASSWORDS.put("Nam", "12345");

        DB_URLS.put("central", "jdbc:sqlserver://LAPTOP-GTU45K0B\\SERVER1:1433;databaseName=QLTH;encrypt=true;trustServerCertificate=true");
        DB_USERNAMES.put("central", "sa");
        DB_PASSWORDS.put("central", "12345");
    }

    public static Connection getConnection(String branch) throws SQLException {
        String url = DB_URLS.get(branch);
        String username = DB_USERNAMES.get(branch);
        String password = DB_PASSWORDS.get(branch);

        if (url == null || username == null || password == null) {
            throw new IllegalArgumentException("Không tìm thấy cấu hình cho branch: " + branch);
        }

        return DriverManager.getConnection(url, username, password);
    }
}
