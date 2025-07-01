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

public class BilanAnnuelController implements Initializable {
    
    @FXML private ComboBox<String> comboFiliere;
    @FXML private ComboBox<String> comboNiveau;
    @FXML private ComboBox<Eleve> comboEtudiant;
    @FXML private TableView<BilanRow> tableBilan;
    @FXML private TableColumn<BilanRow, String> colCodeMatiere;
    @FXML private TableColumn<BilanRow, String> colDesignation;
    @FXML private TableColumn<BilanRow, String> colSemestre;
    @FXML private TableColumn<BilanRow, String> colNote;
    @FXML private TextField txtMoyenneAnnuelle;

    private FiliereDAO filiereDAO;
    private MatiereDAO matiereDAO;
    private EleveDAO eleveDAO;
    private NoteDAO noteDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filiereDAO = new FiliereDAO();
        matiereDAO = new MatiereDAO();
        eleveDAO = new EleveDAO();
        noteDAO = new NoteDAO();
        colCodeMatiere.setCellValueFactory(new PropertyValueFactory<>("codeMatiere"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableBilan.setItems(FXCollections.observableArrayList());
        comboFiliere.setItems(FXCollections.observableArrayList(filiereDAO.getAllCodesFilieres()));
        comboFiliere.setOnAction(e -> updateNiveaux());
        comboNiveau.setOnAction(e -> updateEtudiants());
    }

    private void updateNiveaux() {
        String filiere = comboFiliere.getValue();
        if (filiere != null) {
            comboNiveau.setItems(FXCollections.observableArrayList(filiereDAO.getNiveauxByFiliere(filiere)));
            comboNiveau.getSelectionModel().clearSelection();
            comboEtudiant.getItems().clear();
        }
    }

    private void updateEtudiants() {
        String filiere = comboFiliere.getValue();
        String niveau = comboNiveau.getValue();
        if (filiere != null && niveau != null) {
            ObservableList<Eleve> etudiants = FXCollections.observableArrayList();
            for (Eleve e : eleveDAO.getAllEleves()) {
                if (filiere.equals(e.getCodeFiliere()) && niveau.equals(e.getNiveau())) {
                    etudiants.add(e);
                }
            }
            comboEtudiant.setItems(etudiants);
            comboEtudiant.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleRechercher() {
        String filiere = comboFiliere.getValue();
        String niveau = comboNiveau.getValue();
        Eleve etudiant = comboEtudiant.getValue();
        if (filiere == null || niveau == null || etudiant == null) {
            showAlert("Veuillez sélectionner la filière, le niveau et l'étudiant.");
            return;
        }
        List<Matiere> matieres = matiereDAO.getMatier_Par_Filier_Et_Niveauet(filiere, niveau);
        ObservableList<BilanRow> tableData = FXCollections.observableArrayList();
        double total = 0;
        int count = 0;
        for (Matiere matiere : matieres) {
            List<Note> notes = noteDAO.getNotesParEleve(etudiant.getCode(), matiere.getCode());
            String noteStr = notes.isEmpty() ? "" : String.valueOf(notes.get(0).getNote());
            tableData.add(new BilanRow(matiere.getCode(), matiere.getDesignation(), matiere.getCodeModule(), noteStr));
            if (!notes.isEmpty()) {
                total += notes.get(0).getNote();
                count++;
            }
        }
        tableBilan.setItems(tableData);
        txtMoyenneAnnuelle.setText(count > 0 ? String.format("%.2f", total / count) : "");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class BilanRow {
        private final String codeMatiere;
        private final String designation;
        private final String semestre;
        private final String note;
        public BilanRow(String codeMatiere, String designation, String semestre, String note) {
            this.codeMatiere = codeMatiere;
            this.designation = designation;
            this.semestre = semestre;
            this.note = note;
        }
        public String getCodeMatiere() { return codeMatiere; }
        public String getDesignation() { return designation; }
        public String getSemestre() { return semestre; }
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