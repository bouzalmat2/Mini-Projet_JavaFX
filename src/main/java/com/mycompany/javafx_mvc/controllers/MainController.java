package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.javafx_mvc.dao.EleveDAO;
import com.mycompany.javafx_mvc.dao.FiliereDAO;
import com.mycompany.javafx_mvc.dao.LoginDAO;
import com.mycompany.javafx_mvc.dao.MatiereDAO;
import com.mycompany.javafx_mvc.dao.NoteDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainController implements Initializable {

    @FXML
    private Label lbl_welcome;
    
    @FXML
    private Label lbl_students;
    
    @FXML
    private Label lbl_matieres;
    
    @FXML
    private Label lbl_notes;
    
    @FXML
    private Label lbl_filieres;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Update statistics
        EleveDAO eleveDAO = new EleveDAO();
        FiliereDAO filiereDAO = new FiliereDAO();
        MatiereDAO matiereDAO = new MatiereDAO();
        NoteDAO noteDAO = new NoteDAO();
        lbl_students.setText(String.valueOf(eleveDAO.getAllEleves().size()));
        lbl_filieres.setText(String.valueOf(filiereDAO.getAllFilieres().size()));
        lbl_matieres.setText(String.valueOf(matiereDAO.getMatier_Par_Filier_Et_Niveauet("%", "%").size())); // fallback: count all matieres
        lbl_notes.setText(String.valueOf(noteDAO.getNotesParEleve("%", "%").size())); // fallback: count all notes
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
    
    @FXML
    private void handleBilanAnnuel(ActionEvent event) {
        try {
            new com.mycompany.javafx_mvc.dao.LoginDAO().changeScene(event, "view/BilanAnnuel.fxml", "Bilan Annuel", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}