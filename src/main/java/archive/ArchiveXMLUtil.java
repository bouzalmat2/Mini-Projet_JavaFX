package archive;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.mycompany.javafx_mvc.models.Note;

public class ArchiveXMLUtil {

    private static final String FILE_PATH = "D:\\ENSIT.xml";

   
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
}


