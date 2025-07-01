package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.javafx_mvc.dao.EleveDAO;
import com.mycompany.javafx_mvc.dao.FiliereDAO;
import com.mycompany.javafx_mvc.models.Eleve;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class EtudiantsController implements Initializable {

    @FXML private ComboBox<String> comboFiliere;
    @FXML private ComboBox<String> comboNiveau;
    @FXML private TextField txtCode;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colCode;
    @FXML private TableColumn<Eleve, String> colNom;
    @FXML private TableColumn<Eleve, String> colPrenom;
    @FXML private TableColumn<Eleve, String> colNiveau;
    @FXML private TableColumn<Eleve, String> colFiliere;
    @FXML private CheckBox cbCode;
    @FXML private CheckBox cbNom;
    @FXML private CheckBox cbPrenom;
    @FXML private CheckBox cbFiliere;
    @FXML private CheckBox cbNiveau;
    @FXML private TextField txtSearch;
    
    private FiliereDAO filiereDAO;
    private EleveDAO eleveDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filiereDAO = new FiliereDAO();
        eleveDAO = new EleveDAO();
        
        // Setup table columns
        colCode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCode()));
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrenom()));
        colNiveau.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNiveau()));
        colFiliere.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCodeFiliere()));
        
        tableEleves.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getCode());
                txtNom.setText(newSelection.getNom());
                txtPrenom.setText(newSelection.getPrenom());
                comboFiliere.setValue(newSelection.getCodeFiliere());
                comboNiveau.setValue(newSelection.getNiveau());
            }
        });
        
        List<String> codesFilieres = filiereDAO.getAllCodesFilieres();
        ObservableList<String> observableFilieres = FXCollections.observableArrayList(codesFilieres);
        comboFiliere.setItems(observableFilieres);
        
        rafraichirTable();
    }

    @FXML
    private void FiliereChange(ActionEvent event) {
        String selectedFiliere = comboFiliere.getValue();
        if (selectedFiliere != null) {
            ObservableList<String> niveaux = FXCollections.observableArrayList(
                "1er année",
                "2eme année", 
                "3eme année",
                "4eme année",
                "5eme année"
            );
            comboNiveau.setItems(niveaux);
        }
    }

    @FXML
    private void ajouterEtudiant() {
        String code = txtCode.getText();
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String niveau = comboNiveau.getValue();
        String codeFil = comboFiliere.getValue();

        if (code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || niveau == null || codeFil == null) {
            afficherAlerte(AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (eleveDAO.getEleve_Par_code(code) != null) {
            afficherAlerte(AlertType.ERROR, "Code existe", "Ce code existe deja, veuillez en choisir un autre.");
            return;
        }

        Eleve e = new Eleve( code, nom, prenom, niveau, codeFil);
        if (eleveDAO.AjouterEleve(e)) {
            afficherAlerte(AlertType.INFORMATION, "Succes", "Etudiant ajoute avec succes.");
            rafraichirTable();
            nouveauEtudiant();
        } else {
            afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'etudiant.");
        }
    }

    @FXML
    private void modifierEtudiant() {
        String code = txtCode.getText();
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String niveau = comboNiveau.getValue();
        String codeFil = comboFiliere.getValue();

        if (code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || niveau == null || codeFil == null) {
            afficherAlerte(AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        Eleve e = new Eleve(code, nom, prenom, niveau, codeFil);
        if (eleveDAO.ModifierEleve(e)) {
            afficherAlerte(AlertType.INFORMATION, "Succes", "Etudiant modifie avec succes.");
            rafraichirTable();
            nouveauEtudiant();
        } else {
            afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'etudiant.");
        }
    }

    @FXML
    private void supprimerEtudiant() {
        String code = txtCode.getText();

        if (code.isEmpty()) {
            afficherAlerte(AlertType.ERROR, "Code manquant", "Veuillez saisir le code de l'etudiant a supprimer.");
            return;
        }

        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet etudiant ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            if (eleveDAO.supprimerEleve(code)) {
                afficherAlerte(AlertType.INFORMATION, "Succes", "Etudiant supprime avec succes.");
                rafraichirTable();
                nouveauEtudiant();
            } else {
                afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'etudiant.");
            }
        }
    }

    @FXML
    private void nouveauEtudiant() {
        txtCode.clear();
        txtNom.clear();
        txtPrenom.clear();
        comboFiliere.getSelectionModel().clearSelection();
        comboNiveau.getSelectionModel().clearSelection();
    }

    @FXML
    public void retourMain(ActionEvent event) {
        try {
            new com.mycompany.javafx_mvc.dao.LoginDAO().changeScene(event, "view/Main.fxml", "Main", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void rechercherEtudiants() {
        String searchValue = txtSearch.getText().trim();
        if (searchValue.isEmpty()) {
            rafraichirTable();
            return;
        }

        // If code is checked, ignore other criteria
        if (cbCode.isSelected()) {
            cbNom.setSelected(false);
            cbPrenom.setSelected(false);
            cbFiliere.setSelected(false);
            cbNiveau.setSelected(false);
        }

        List<Eleve> results = new ArrayList<>();
        for (Eleve e : eleveDAO.getAllEleves()) {
            boolean match = false;
            if (cbCode.isSelected() && e.getCode().equalsIgnoreCase(searchValue)) match = true;
            if (cbNom.isSelected() && e.getNom().toLowerCase().contains(searchValue.toLowerCase())) match = true;
            if (cbPrenom.isSelected() && e.getPrenom().toLowerCase().contains(searchValue.toLowerCase())) match = true;
            if (cbFiliere.isSelected() && e.getCodeFiliere().toLowerCase().contains(searchValue.toLowerCase())) match = true;
            if (cbNiveau.isSelected() && e.getNiveau().toLowerCase().contains(searchValue.toLowerCase())) match = true;
            if (match) results.add(e);
        }
        tableEleves.setItems(FXCollections.observableArrayList(results));
    }

    private void rafraichirTable() {
        ObservableList<Eleve> liste = FXCollections.observableArrayList(eleveDAO.getAllEleves());
        tableEleves.setItems(liste);
    }

    private void afficherAlerte(AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
