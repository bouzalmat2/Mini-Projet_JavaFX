package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;

import com.mycompany.javafx_mvc.dao.NoteDAO;
import com.mycompany.javafx_mvc.dao.MatiereDAO;
import com.mycompany.javafx_mvc.dao.EleveDAO;
import com.mycompany.javafx_mvc.models.Note;
import com.mycompany.javafx_mvc.models.Eleve;
import com.mycompany.javafx_mvc.models.Matiere;

public class NotesController implements Initializable {

    @FXML private TextField txtCodeEleve;


    @FXML private ComboBox<Matiere> comboMatier;

    @FXML private TextField txtNote;
    @FXML private Button btnNouveau, btnAjouter, btnModifier, btnSupprimer, btnRechercher;

    private NoteDAO noteDAO;
    private MatiereDAO matiereDAO;
    private EleveDAO eleveDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noteDAO = new NoteDAO();
        matiereDAO = new MatiereDAO();
        eleveDAO = new EleveDAO();

        validationNote();
    }

    private void validationNote() {
        UnaryOperator<Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}(\\.\\d{0,2})?")) {
                try {
                    double value = Double.parseDouble(newText);
                    if (value >= 0 && value <= 20) return change;
                } catch (NumberFormatException ignored) {}
            }
            return null;
        };
        txtNote.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    private void handleNouveau() {
        txtCodeEleve.clear();
        txtNote.clear();
        comboMatier.getItems().clear();
    }

    @FXML
    private void handleAjouter() {
        String code = txtCodeEleve.getText().trim();
        Matiere matiere = comboMatier.getValue();
        String noteStr = txtNote.getText().trim();

        if (code.isEmpty() || matiere == null || noteStr.isEmpty()) {
            showAlert("Tous les champs sont obligatoires.");
            return;
        }

        if (!eleveDAO.existe(code)) {
            showAlert("Ce code eleve n'existe pas.");
            return;
        }

        double note;
        try {
            note = Double.parseDouble(noteStr);
            if (note < 0 || note > 20) {
                showAlert("La note doit être entre 0 et 20.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Note invalide.");
            return;
        }

        Note n = new Note(code, matiere.getCode(), note);
        noteDAO.ajouterNote(n);
        showInfo("Note ajoutee avec succes.");
    }

    @FXML
    private void handleModifier() {
        String code = txtCodeEleve.getText().trim();
        Matiere matiere = comboMatier.getValue();
        String noteStr = txtNote.getText().trim();

        if (code.isEmpty() || matiere == null || noteStr.isEmpty()) {
            showAlert("Tous les champs sont obligatoires.");
            return;
        }

        double note;
        try {
            note = Double.parseDouble(noteStr);
        } catch (NumberFormatException e) {
            showAlert("Note invalide.");
            return;
        }

        Note n = new Note(code, matiere.getCode(), note);
        boolean success = noteDAO.modifier(n);
        if (success) showInfo("Note modifiee.");
        else showAlert("Echec de la modification.");
    }

    @FXML
    private void handleSupprimer() {
        String code = txtCodeEleve.getText().trim();
        Matiere matiere = comboMatier.getValue();

        if (code.isEmpty() || matiere == null) {
            showAlert("Veuillez saisir le code et la matiere.");
            return;
        }

        boolean success = noteDAO.supprimer(code, matiere.getCode());
        if (success) showInfo("Note supprimee.");
        else showAlert("Echec de la suppression.");
    }

    @FXML
    private void handleRechercher() {
        String code = txtCodeEleve.getText().trim();

        if (code.isEmpty()) {
            showAlert("Veuillez saisir le code de l'etudiant.");
            return;
        }

        Eleve eleve = eleveDAO.getEleve_Par_code(code);
        if (eleve == null) {
            showAlert("Etudiant introuvable.");
            comboMatier.getItems().clear();
            txtNote.clear();
            return;
        }

        List<Matiere> matieres = matiereDAO.getMatier_Par_Filier_Et_Niveauet(eleve.getCodeFiliere(), eleve.getNiveau());

        if (matieres.isEmpty()) {
            showAlert("Aucune matiere trouvee pour cette filiere et niveau.");
            comboMatier.getItems().clear();
            txtNote.clear();
            return;
        }

        comboMatier.getItems().clear();
        comboMatier.getItems().addAll(matieres);  

        showInfo("Matieres chargees avec succes.");
        txtNote.clear();
    }

    @FXML
    private void handleSelectionMatiere() {
        String codeEleve = txtCodeEleve.getText().trim();
        Matiere matiere = comboMatier.getValue();

        if (codeEleve.isEmpty() || matiere == null) {
            txtNote.clear();
            return;
        }

        List<Note> notes = noteDAO.getNotesParEleve(codeEleve, matiere.getCode());

        if (!notes.isEmpty()) {
            Note note = notes.get(0);
            txtNote.setText(String.valueOf(note.getNote()));
        } else {
            txtNote.clear();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
    
    
    @FXML
    private void retourMain() {

        System.out.println("Bouton Retour cliqué");


    }

}