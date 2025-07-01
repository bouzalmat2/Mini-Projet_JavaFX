/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.javafx_mvc.dao.EleveDAO;
import com.mycompany.javafx_mvc.dao.FiliereDAO;
import com.mycompany.javafx_mvc.dao.MatiereDAO;
import com.mycompany.javafx_mvc.dao.NoteDAO;
import com.mycompany.javafx_mvc.models.Eleve;
import com.mycompany.javafx_mvc.models.Matiere;
import com.mycompany.javafx_mvc.models.Note;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class ConsultationController implements Initializable {

    @FXML private ComboBox<String> comboFiliere;
    @FXML private ComboBox<String> comboNiveau;
    @FXML private ComboBox<Matiere> comboMatiere;
    @FXML private TableView<ConsultationRow> tableNotes;
    @FXML private TableColumn<ConsultationRow, String> colCode;
    @FXML private TableColumn<ConsultationRow, String> colNom;
    @FXML private TableColumn<ConsultationRow, String> colPrenom;
    @FXML private TableColumn<ConsultationRow, String> colNote;
    @FXML private TextField txtMoyenneClasse;

    private FiliereDAO filiereDAO;
    private MatiereDAO matiereDAO;
    private EleveDAO eleveDAO;
    private NoteDAO noteDAO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filiereDAO = new FiliereDAO();
        matiereDAO = new MatiereDAO();
        eleveDAO = new EleveDAO();
        noteDAO = new NoteDAO();
        // Table setup
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableNotes.setItems(FXCollections.observableArrayList());
        // Populate filiere ComboBox
        comboFiliere.setItems(FXCollections.observableArrayList(filiereDAO.getAllCodesFilieres()));
        comboFiliere.setOnAction(e -> updateNiveaux());
        comboNiveau.setOnAction(e -> updateMatieres());
    }

    private void updateNiveaux() {
        String filiere = comboFiliere.getValue();
        if (filiere != null) {
            comboNiveau.setItems(FXCollections.observableArrayList(filiereDAO.getNiveauxByFiliere(filiere)));
            comboNiveau.getSelectionModel().clearSelection();
            comboMatiere.getItems().clear();
        }
    }

    private void updateMatieres() {
        String filiere = comboFiliere.getValue();
        String niveau = comboNiveau.getValue();
        if (filiere != null && niveau != null) {
            comboMatiere.setItems(FXCollections.observableArrayList(matiereDAO.getMatier_Par_Filier_Et_Niveauet(filiere, niveau)));
            comboMatiere.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleRechercher() {
        String filiere = comboFiliere.getValue();
        String niveau = comboNiveau.getValue();
        Matiere matiere = comboMatiere.getValue();
        if (filiere == null || niveau == null || matiere == null) {
            showAlert("Veuillez sélectionner la filière, le niveau et la matière.");
            return;
        }
        ObservableList<ConsultationRow> tableData = FXCollections.observableArrayList();
        double total = 0;
        int count = 0;
        for (Eleve eleve : eleveDAO.getAllEleves()) {
            if (filiere.equals(eleve.getCodeFiliere()) && niveau.equals(eleve.getNiveau())) {
                List<Note> notes = noteDAO.getNotesParEleve(eleve.getCode(), matiere.getCode());
                String noteStr = notes.isEmpty() ? "" : String.valueOf(notes.get(0).getNote());
                tableData.add(new ConsultationRow(eleve.getCode(), eleve.getNom(), eleve.getPrenom(), noteStr));
                if (!notes.isEmpty()) {
                    total += notes.get(0).getNote();
                    count++;
                }
            }
        }
        tableNotes.setItems(tableData);
        txtMoyenneClasse.setText(count > 0 ? String.format("%.2f", total / count) : "");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ConsultationRow {
        private final String code;
        private final String nom;
        private final String prenom;
        private final String note;
        public ConsultationRow(String code, String nom, String prenom, String note) {
            this.code = code;
            this.nom = nom;
            this.prenom = prenom;
            this.note = note;
        }
        public String getCode() { return code; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getNote() { return note; }
    }

    @FXML
    public void retourMain(ActionEvent event) {
        try {
            new com.mycompany.javafx_mvc.dao.LoginDAO().changeScene(event, "view/Main.fxml", "Main", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
