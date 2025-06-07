
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
   
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lunarestfilm"; 
    private static final String USER = "root"; 
    private static final String PASS = "*****";

    private static Connection connection = null;


    private DBConnection() {}

    public static Connection getConnection() {
        try {

            if (connection == null || connection.isClosed()) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("MySQL JDBC Sürücüsü bulunamadı!");
                    e.printStackTrace();
                    return null;
                }
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Veritabanına başarıyla bağlanıldı: lunarestfilm");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası!");
            e.printStackTrace();

            return null;
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; 
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Uygulama kapatılıyor, veritabanı bağlantısı kapatılacak...");
            closeConnection();
        }));
    }
}
