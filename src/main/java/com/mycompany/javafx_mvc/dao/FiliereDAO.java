
package com.mycompany.javafx_mvc.dao;


import com.mycompany.javafx_mvc.models.Filiere;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;

public class FiliereDAO {
    private final Connection con=Connexion.getConnection();
     public List<Filiere> getAllFilieres() {
        List<Filiere> liste = new ArrayList<>();
        String sql="SELECT * FROM Filiere";
        try (Statement st = con.createStatement();
             ResultSet RS = st.executeQuery(sql)) {
            while (RS.next()) {
                liste.add(new Filiere(
                       RS.getInt("id"),
                       RS.getString("code"),
                       RS.getString("designation")
                ));
            }
        } catch (SQLException ex) {
             System.out.println("Erreur!!!"+ex.getMessage());
        }
        return liste;
    }
     
     
     public List<String> getNiveauxByFiliere(String codeFil) {
        List<String> niveaux = new ArrayList<>();
        String sql = "SELECT DISTINCT niveau FROM Module WHERE code_fil = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codeFil);
            ResultSet RS = ps.executeQuery();
            while (RS.next()) {
                niveaux.add(RS.getString("niveau"));
            }
        } catch (SQLException ex) {
           System.out.println("Erreur!!!"+ex.getMessage());
        }
        return niveaux;
    }
    
    
}
