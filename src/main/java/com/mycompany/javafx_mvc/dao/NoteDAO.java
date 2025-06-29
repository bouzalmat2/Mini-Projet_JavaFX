
package com.mycompany.javafx_mvc.dao;
import com.mycompany.javafx_mvc.models.Note;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;

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
    
    
    
    public List<Note> getNotesParEleve(String codeEleve) {
        List<Note> liste = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE code_eleve = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codeEleve);
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

    
}
