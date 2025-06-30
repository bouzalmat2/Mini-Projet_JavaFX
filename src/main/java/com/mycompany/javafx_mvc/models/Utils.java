package com.mycompany.javafx_mvc.models;

import com.mycompany.javafx_mvc.controllers.MainController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Utils {  
     
    public void changeScene(ActionEvent event, String fxmlFile, String title, String name) {
        Parent root = null;
        int width, height;
        
        try {
            if (name != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                root = loader.load();
                MainController mainController = loader.getController();
                mainController.setName(name);
            } else {
                root = FXMLLoader.load(getClass().getResource(fxmlFile));
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
                    height = 700;
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
    
    public void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
