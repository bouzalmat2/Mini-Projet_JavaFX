package archive;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mycompany.javafx_mvc.models.Eleve;
import com.mycompany.javafx_mvc.models.Note;

public class ArchiveXMLUtil {

    private static final String FILE_PATH = "src/main/resources/ENSIT.xml";

   
   public static boolean archiverNote(Note note) {
    try {
        File file = new File(FILE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;

        if (file.exists()) {
            doc = builder.parse(file);
            doc.getDocumentElement().normalize();
        } else {
            doc = builder.newDocument();
            Element root = doc.createElement("archives");
            doc.appendChild(root);
        }

        Element root = doc.getDocumentElement();

        Element noteElem = doc.createElement("note");
        noteElem.setAttribute("id", String.valueOf(note.getId()));

        Element codeEleve = doc.createElement("code_eleve");
        codeEleve.appendChild(doc.createTextNode(note.getCodeEleve()));
        noteElem.appendChild(codeEleve);

        Element codeMatiere = doc.createElement("code_matiere");
        codeMatiere.appendChild(doc.createTextNode(note.getCodeMatiere()));
        noteElem.appendChild(codeMatiere);

        Element valeur = doc.createElement("valeur");
        valeur.appendChild(doc.createTextNode(String.valueOf(note.getNote())));
        noteElem.appendChild(valeur);

        Element dateSuppression = doc.createElement("date_suppression");
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateSuppression.appendChild(doc.createTextNode(now));
        noteElem.appendChild(dateSuppression);

        root.appendChild(noteElem);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false; 
    }
}

public static boolean archiverEleve(Eleve eleve) {
    try {
        File file = new File(FILE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;

        if (file.exists()) {
            doc = builder.parse(file);
            doc.getDocumentElement().normalize();
        } else {
            doc = builder.newDocument();
            Element root = doc.createElement("archives");
            doc.appendChild(root);
        }

        Element root = doc.getDocumentElement();

        Element eleveElem = doc.createElement("eleve");
        eleveElem.setAttribute("code", eleve.getCode());

        Element nom = doc.createElement("nom");
        nom.appendChild(doc.createTextNode(eleve.getNom()));
        eleveElem.appendChild(nom);

        Element prenom = doc.createElement("prenom");
        prenom.appendChild(doc.createTextNode(eleve.getPrenom()));
        eleveElem.appendChild(prenom);

        Element niveau = doc.createElement("niveau");
        niveau.appendChild(doc.createTextNode(eleve.getNiveau()));
        eleveElem.appendChild(niveau);

        Element codeFiliere = doc.createElement("code_filiere");
        codeFiliere.appendChild(doc.createTextNode(eleve.getCodeFiliere()));
        eleveElem.appendChild(codeFiliere);

        Element dateSuppression = doc.createElement("date_suppression");
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateSuppression.appendChild(doc.createTextNode(now));
        eleveElem.appendChild(dateSuppression);

        root.appendChild(eleveElem);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}


