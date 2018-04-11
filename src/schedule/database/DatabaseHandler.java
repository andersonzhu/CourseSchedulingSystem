package schedule.database;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class DatabaseHandler {

    private static DatabaseHandler instance = new DatabaseHandler();

    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {}

    public static DatabaseHandler getInstance() {
        return instance;
    }

    // user relative path instead of absolute path
    private URL getDBPath() {
        return getClass().getResource("Courses.db");
    }

    public void createConnection() {

        try {
            Class.forName("org.sql.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Courses.db");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }


    }

    public static void main(String[] args) {
        try {
            Class.forName("org.sql.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Courses.db");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

}
