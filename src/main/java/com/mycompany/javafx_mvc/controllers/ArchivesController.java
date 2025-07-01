package com.mycompany.javafx_mvc.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mycompany.javafx_mvc.models.Eleve;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ArchivesController implements Initializable {

    @FXML private TableView<ArchiveRow> tableArchives;
    @FXML private TableColumn<ArchiveRow, String> colCode;
    @FXML private TableColumn<ArchiveRow, String> colNom;
    @FXML private TableColumn<ArchiveRow, String> colPrenom;
    @FXML private TableColumn<ArchiveRow, String> colNiveau;
    @FXML private TableColumn<ArchiveRow, String> colFiliere;
    @FXML private TableColumn<ArchiveRow, String> colDateSuppression;
    @FXML private Button btnRestaurer;
    @FXML private Button btnExporterPDF;

    private static final String ARCHIVE_PATH = "src/main/resources/ENSIT.xml";

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        colDateSuppression.setCellValueFactory(new PropertyValueFactory<>("dateSuppression"));
        loadArchives();
    }    
    
    private void loadArchives() {
        ObservableList<ArchiveRow> data = FXCollections.observableArrayList();
        try {
            File file = new File(ARCHIVE_PATH);
            if (!file.exists()) return;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);
            NodeList eleves = doc.getElementsByTagName("eleve");
            for (int i = 0; i < eleves.getLength(); i++) {
                Element e = (Element) eleves.item(i);
                String code = e.getAttribute("code");
                String nom = getTagValue(e, "nom");
                String prenom = getTagValue(e, "prenom");
                String niveau = getTagValue(e, "niveau");
                String filiere = getTagValue(e, "code_filiere");
                String dateSupp = getTagValue(e, "date_suppression");
                data.add(new ArchiveRow(code, nom, prenom, niveau, filiere, dateSupp));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tableArchives.setItems(data);
    }

    private String getTagValue(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() > 0) return nl.item(0).getTextContent();
        return "";
    }

    @FXML
    private void handleRestaurer(ActionEvent event) {
        ArchiveRow selected = tableArchives.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Veuillez sélectionner un étudiant à restaurer.");
            return;
        }
        // Re-insert student into DB
        Eleve eleve = new Eleve(selected.getCode(), selected.getNom(), selected.getPrenom(), selected.getNiveau(), selected.getFiliere());
        com.mycompany.javafx_mvc.dao.EleveDAO eleveDAO = new com.mycompany.javafx_mvc.dao.EleveDAO();
        if (eleveDAO.AjouterEleve(eleve)) {
            showInfo("Étudiant restauré avec succès.");
            removeFromArchive(selected.getCode());
            loadArchives();
        } else {
            showAlert("Erreur lors de la restauration. L'étudiant existe peut-être déjà.");
        }
    }

    private void removeFromArchive(String code) {
        try {
            File file = new File(ARCHIVE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);
            NodeList eleves = doc.getElementsByTagName("eleve");
            for (int i = 0; i < eleves.getLength(); i++) {
                Element e = (Element) eleves.item(i);
                if (e.getAttribute("code").equals(code)) {
                    e.getParentNode().removeChild(e);
                    break;
                }
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(doc), new StreamResult(file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleExporterPDF(ActionEvent event) {
        ObservableList<ArchiveRow> data = tableArchives.getItems();
        if (data == null || data.isEmpty()) {
            showAlert("Aucune donnée à exporter.");
            return;
        }
        com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
        try {
            String outputPath = "archives_export.pdf";
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, new java.io.FileOutputStream(outputPath));
            pdfDoc.open();
            pdfDoc.add(new com.itextpdf.text.Paragraph("Liste des étudiants archivés"));
            pdfDoc.add(new com.itextpdf.text.Paragraph(" "));
            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(6);
            table.addCell("Code");
            table.addCell("Nom");
            table.addCell("Prénom");
            table.addCell("Niveau");
            table.addCell("Filière");
            table.addCell("Date Suppression");
            for (ArchiveRow row : data) {
                table.addCell(row.getCode());
                table.addCell(row.getNom());
                table.addCell(row.getPrenom());
                table.addCell(row.getNiveau());
                table.addCell(row.getFiliere());
                table.addCell(row.getDateSuppression());
            }
            pdfDoc.add(table);
            pdfDoc.close();
            showInfo("Export PDF réussi : " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'export PDF : " + e.getMessage());
        } finally {
            if (pdfDoc.isOpen()) pdfDoc.close();
        }
    }

    @FXML
    public void retourMain(ActionEvent event) {
        try {
            new com.mycompany.javafx_mvc.dao.LoginDAO().changeScene(event, "view/Main.fxml", "Main", null);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static class ArchiveRow {
        private final String code, nom, prenom, niveau, filiere, dateSuppression;
        public ArchiveRow(String code, String nom, String prenom, String niveau, String filiere, String dateSuppression) {
            this.code = code;
            this.nom = nom;
            this.prenom = prenom;
            this.niveau = niveau;
            this.filiere = filiere;
            this.dateSuppression = dateSuppression;
        }
        public String getCode() { return code; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getNiveau() { return niveau; }
        public String getFiliere() { return filiere; }
        public String getDateSuppression() { return dateSuppression; }
    }
}
