
package com.mycompany.javafx_mvc.dao;
import com.mycompany.javafx_mvc.models.Moyenne;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;

public class MoyenneDAO {
    private final Connection con = Connexion.getConnection();

    public boolean enregistrerMoyenne(Moyenne m) {
        String sql = "CALL enregistrer_moyenne(?, ?, ?, ?)";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, m.getCodeEleve());
            cs.setString(2, m.getCodeFiliere());
            cs.setString(3, m.getNiveau());
            cs.setDouble(4, m.getMoyenne());
            return cs.executeUpdate() > 0;
        } catch (SQLException ex) {
              System.out.println("Erreur!!!"+ex.getMessage());
            return false;
        }
    }

    public Moyenne getMoyenneParEleve(String codeEleve) {
        String sql = "SELECT * FROM Moyennes WHERE code_eleve = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codeEleve);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Moyenne(
                    rs.getInt("id"),
                    rs.getString("code_eleve"),
                    rs.getString("code_fil"),
                    rs.getString("niveau"),
                    rs.getDouble("moyenne")
                );
            }
        } catch (SQLException ex) {
            System.out.println("Erreur!!!"+ex.getMessage());
        }
        return null;
    }
    
}
