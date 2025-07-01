package com.mycompany.javafx_mvc.dao;

import com.mycompany.javafx_mvc.controllers.MainController;
import com.mycompany.javafx_mvc.db.Connexion;
import java.io.*;
import java.sql.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.stage.*;

public class LoginDAO {
    private final Connection con;
    
    public LoginDAO() {
        con = Connexion.getConnection();
    }
    
    
public void changeScene(ActionEvent event, String fxmlFile, String title, String name) {
        Parent root = null;
        int width, height;
        
        try {
            if (name != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javafx_mvc/" + fxmlFile));
                root = loader.load();
                MainController mainController = loader.getController();
                mainController.setName(name);
            } else {
                root = FXMLLoader.load(getClass().getResource("/com/mycompany/javafx_mvc/" + fxmlFile));
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            switch (fxmlFile) {
                case "view/Login.fxml":
                    width = 600;
                    height = 400;
                    break;
                case "view/Main.fxml":
                    width = 800;
                    height = 700;
                    break;
                case "view/Etudiants.fxml":
                    width = 800;
                    height = 800;
                    break;
                case "view/Notes.fxml":
                    width = 900;
                    height = 650;
                    break;
                case "view/Consultation.fxml":
                    width = 1000;
                    height = 750;
                    break;
                case "view/Archives.fxml":
                    width = 850;
                    height = 600;
                    break;
                default:
                    width = 600;
                    height = 400;
                    break;
            }
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (IOException ex) {
            System.err.println("Error loading FXML: " + ex.getMessage());
            showAlert("Error", "Failed to load view", Alert.AlertType.ERROR);
   }
}

    
    public void loginUser(ActionEvent event, String username, String password) {
        String sql = "SELECT name, password FROM user WHERE username = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, username);  
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    showAlert("Login Failed", "Username incorrect", Alert.AlertType.ERROR);
                } else {
                    String storedName = rs.getString("name");
                    String storedPassword = rs.getString("password");
                    
                    if (storedPassword.equals(password)) {
                        changeScene(event, "view/Main.fxml", "Main", storedName);
                    } else {
                        showAlert("Login Failed", "Password incorrect", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
            showAlert("Error", "Database error occurred", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error closing connection: " + ex.getMessage());
        }
    }
}