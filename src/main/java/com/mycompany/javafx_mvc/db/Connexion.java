package com.mycompany.javafx_mvc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    
    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_notes?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";  

   
    private Connexion() {
     
    }
    public static Connection getConnection() {
        if (connection == null) {
            try {
           
                Class.forName("com.mysql.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Connexion reussie !");
            } catch (ClassNotFoundException |SQLException e) {
                System.out.println(" Erreur de connexion : " + e.getMessage());
            }
        }
        return connection;
    }
}
