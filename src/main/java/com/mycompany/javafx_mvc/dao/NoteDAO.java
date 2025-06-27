
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
    
}
