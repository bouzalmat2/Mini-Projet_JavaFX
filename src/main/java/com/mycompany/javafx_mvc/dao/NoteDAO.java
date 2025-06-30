
package com.mycompany.javafx_mvc.dao;
import com.mycompany.javafx_mvc.models.Note;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;
import archive.ArchiveXMLUtil;


public class NoteDAO {
    
     private final Connection con = Connexion.getConnection();

    public boolean ajouterNote(Note n) {
        String sql = "CALL inserer_note(?, ?, ?)";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, n.getCodeEleve());
            cs.setString(2, n.getCodeMatiere());
            cs.setDouble(3, n.getNote());
            return cs.executeUpdate() > 0;
        } catch (SQLException ex) {
             System.out.println("Erreur!!!"+ex.getMessage());
            return false;
        }
    }
    
    
    
    public boolean modifier(Note note) {
    String sql = "{CALL Modifier_Note(?, ?, ?)}";
    try (CallableStatement cs = con.prepareCall(sql)) {
        cs.setString(1, note.getCodeEleve());
        cs.setString(2, note.getCodeMatiere());
        cs.setFloat(3, (float) note.getNote());
        return cs.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Erreur appel procedure update_note : " + e.getMessage());
        return false;
    }
}
    
    
    
   
    
    
    
    public List<Note> getNotesParEleve(String codeEleve,String matiere) {
        List<Note> liste = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE code_eleve = ? AND code_mat= ? ";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codeEleve);
            ps.setString(2,matiere);
            ResultSet RS = ps.executeQuery();
            while (RS.next()) {
                liste.add(new Note(
                    RS.getInt("id"),
                    RS.getString("code_eleve"),
                    RS.getString("code_mat"),
                    RS.getDouble("note")
                ));
            }
        } catch (SQLException ex) {
              System.out.println("Erreur!!!"+ex.getMessage());
        }
        return liste;
    }
    
    public Note rechercherNote(String codeEleve, String codeMatiere) {
        List<Note> notes = getNotesParEleve(codeEleve, codeMatiere);
        if (notes != null && !notes.isEmpty()) {
            return notes.get(0);
        }
        return null;
    }
    
    
    
public float calculerMoyenne(String codeEleve) {
    String sql = "SELECT AVG(note) AS moyenne FROM notes WHERE code_eleve = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, codeEleve);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getFloat("moyenne");
        }
    } catch (SQLException e) {
        System.out.println("Erreur calcul moyenne : " + e.getMessage());
    }
    return 0.0f;
}


public boolean supprimer(String codeEleve, String codeMatiere) {
       
        Note note = rechercherNote(codeEleve, codeMatiere);
        if (note == null) {
            System.out.println("Note introuvable pour suppression");
            return false;
        }

       
        boolean archiveOk = ArchiveXMLUtil.archiverNote(note);
        if (!archiveOk) {
            System.out.println("Erreur lors de l'archivage, suppression annulee");
            return false;
        }

       
        String sql = "{CALL supprimer_note(?, ?)}"; 
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, codeEleve);
            cs.setString(2, codeMatiere);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression : " + e.getMessage());
            return false;
        }
    }

    
}
