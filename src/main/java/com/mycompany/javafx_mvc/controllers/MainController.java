package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.javafx_mvc.dao.LoginDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController implements Initializable {

    @FXML
    private Label lbl_welcome;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
    public void setName(String name){
        lbl_welcome.setText("Welcome "+ name + " !");
    }
    
    @FXML
    private void openEtudiants(ActionEvent event) {
        try {
            new LoginDAO().changeScene(event, "view/Etudiants.fxml", "Gestion des Étudiants", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openNotes(ActionEvent event) {
        try {
            new LoginDAO().changeScene(event, "view/Notes.fxml", "Gestion des Notes", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openConsultation(ActionEvent event) {
        try {
            new LoginDAO().changeScene(event, "view/Consultation.fxml", "Consultation des Résultats", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openArchives(ActionEvent event) {
        try {
            new LoginDAO().changeScene(event, "view/Archives.fxml", "Archives", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void logout(ActionEvent event) {
        try {
            new LoginDAO().changeScene(event, "view/Login.fxml", "Login - Student Grade Management System", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}