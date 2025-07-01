package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.mycompany.javafx_mvc.dao.EleveDAO;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;

public class NotesController implements Initializable {

    @FXML private TextField txtCodeEleve;


    @FXML private ComboBox<Matiere> comboMatier;

    @FXML private TextField txtNote;
    @FXML private Button btnNouveau, btnAjouter, btnModifier, btnSupprimer, btnRechercher;

    @FXML private TableView<MatiereNoteRow> tableMatieres;
    @FXML private TableColumn<MatiereNoteRow, String> colDesignation;
    @FXML private TableColumn<MatiereNoteRow, String> colNote;

    private NoteDAO noteDAO;
    private MatiereDAO matiereDAO;
    private EleveDAO eleveDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noteDAO = new NoteDAO();
        matiereDAO = new MatiereDAO();
        eleveDAO = new EleveDAO();

        validationNote();
        // TableView setup
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableMatieres.setItems(FXCollections.observableArrayList());
        // Add listener for table selection
        tableMatieres.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Find the corresponding Matiere in the ComboBox
                Matiere selectedMatiere = null;
                for (Matiere m : comboMatier.getItems()) {
                    if (m.getDesignation().equals(newSelection.getDesignation())) {
                        selectedMatiere = m;
                        break;
                    }
                }
                if (selectedMatiere != null && comboMatier.getValue() != selectedMatiere) {
                    comboMatier.setValue(selectedMatiere);
                }
                if (newSelection.getNote() == null || newSelection.getNote().isEmpty()) {
                    txtNote.clear();
                } else {
                    txtNote.setText(newSelection.getNote());
                }
            }
        });
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
        tableMatieres.getItems().clear();
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
            comboMatier.getItems().clear();
            txtNote.clear();
            tableMatieres.getItems().clear();
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
        boolean success = noteDAO.ajouterNote(n);
        refreshTableForStudent(code);
        if (!success) showAlert("Erreur lors de l'ajout de la note.");
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

        if (!eleveDAO.existe(code)) {
            comboMatier.getItems().clear();
            txtNote.clear();
            tableMatieres.getItems().clear();
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
        refreshTableForStudent(code);
        if (success) showInfo("Note modifiee.");
        else showAlert("Echec de la modification. Vérifiez la base de données.");
    }

    @FXML
    private void handleSupprimer() {
        String code = txtCodeEleve.getText().trim();
        Matiere matiere = comboMatier.getValue();

        if (code.isEmpty() || matiere == null) {
            showAlert("Veuillez saisir le code et la matiere.");
            return;
        }

        if (!eleveDAO.existe(code)) {
            comboMatier.getItems().clear();
            txtNote.clear();
            tableMatieres.getItems().clear();
            return;
        }

        boolean success = noteDAO.supprimer(code, matiere.getCode());
        refreshTableForStudent(code);
        if (success) showInfo("Note supprimee.");
        else showAlert("Echec de la suppression. Vérifiez la base de données.");
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
            tableMatieres.getItems().clear();
            return;
        }
        List<Matiere> matieres = matiereDAO.getMatier_Par_Filier_Et_Niveauet(eleve.getCodeFiliere(), eleve.getNiveau());
        System.out.println("Matieres found: " + matieres.size());
        for (Matiere m : matieres) {
            System.out.println("Matiere: " + m.getDesignation() + " (code: " + m.getCode() + ")");
        }
        System.out.println("Student filiere: " + eleve.getCodeFiliere() + ", niveau: " + eleve.getNiveau());
        if (matieres.isEmpty()) {
            showAlert("Aucune matiere trouvee pour cette filiere (" + eleve.getCodeFiliere() + ") et niveau (" + eleve.getNiveau() + ").");
            comboMatier.getItems().clear();
            txtNote.clear();
            tableMatieres.getItems().clear();
            return;
        }
        comboMatier.getItems().clear();
        comboMatier.getItems().addAll(matieres);
        // Populate TableView
        ObservableList<MatiereNoteRow> tableData = FXCollections.observableArrayList();
        for (Matiere matiere : matieres) {
            List<Note> notes = noteDAO.getNotesParEleve(code, matiere.getCode());
            String noteStr = notes.isEmpty() ? "" : String.valueOf(notes.get(0).getNote());
            tableData.add(new MatiereNoteRow(matiere.getDesignation(), noteStr));
        }
        txtNote.clear();
        tableMatieres.setItems(tableData);
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
    public void retourMain(ActionEvent event) {
        try {
            new com.mycompany.javafx_mvc.dao.LoginDAO().changeScene(event, "view/Main.fxml", "Main", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTableForStudent(String code) {
        Eleve eleve = eleveDAO.getEleve_Par_code(code);
        if (eleve == null) {
            tableMatieres.getItems().clear();
            return;
        }
        List<Matiere> matieres = matiereDAO.getMatier_Par_Filier_Et_Niveauet(eleve.getCodeFiliere(), eleve.getNiveau());
        ObservableList<MatiereNoteRow> tableData = FXCollections.observableArrayList();
        for (Matiere matiere : matieres) {
            List<Note> notes = noteDAO.getNotesParEleve(code, matiere.getCode());
            String noteStr = notes.isEmpty() ? "" : String.valueOf(notes.get(0).getNote());
            tableData.add(new MatiereNoteRow(matiere.getDesignation(), noteStr));
        }
        tableMatieres.setItems(tableData);
    }

    public static class MatiereNoteRow {
        private final String designation;
        private final String note;
        public MatiereNoteRow(String designation, String note) {
            this.designation = designation;
            this.note = note;
        }
        public String getDesignation() { return designation; }
        public String getNote() { return note; }
    }

}