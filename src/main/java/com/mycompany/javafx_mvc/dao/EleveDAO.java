package com.mycompany.javafx_mvc.dao;
import com.mycompany.javafx_mvc.models.Eleve;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;


public class EleveDAO {
    private final Connection con;
    
    
    public EleveDAO(){
        con=Connexion.getConnection();
    }
    
    
    public boolean AjouterEleve(Eleve e){

        try(CallableStatement CS=con.prepareCall("{call inserer_eleve(?,?,?,?,?)}")){
            CS.setString(1, e.getCode());
            CS.setString(2, e.getNom());
            CS.setString(3, e.getPrenom());
            CS.setString(4,e.getNiveau());
            CS.setString(5, e.getCodeFiliere());
            return CS.executeUpdate() > 0;
        }catch(SQLException ex){
             System.out.println("Erreur insertion: " + ex.getMessage());
        }
        return false;
    }
   
    public boolean ModifierEleve(Eleve e){
      
        try(CallableStatement CS=con.prepareCall("{call modifier_eleve(?,?,?,?,?)}")){
            CS.setString(1, e.getCode());
            CS.setString(2, e.getNom());
            CS.setString(3, e.getPrenom());
            CS.setString(4, e.getNiveau());
            CS.setString(5,e.getCodeFiliere());
            return CS.executeUpdate()>0;
        }catch(SQLException ex){
            System.out.println("Erreur modification: "+ex.getMessage());
        }
    return false;
    
    }
    
    
    
    public boolean supprimerEleve(String code){
     
        try(CallableStatement CS=con.prepareCall("{call supprimer_eleve(?)}")){
            CS.setString(1,code);
            
            return CS.executeUpdate()>0;
        }catch(SQLException ex){
            System.out.println("Erreur supprission:"+ex.getMessage());
        }
        return false;
    }
    
    public List<Eleve> getAllEleves(){
        List<Eleve> liste =new ArrayList<>();
        
        String sql="SELECT * FROM Eleve";
        try(Statement stm=con.createStatement();
              ResultSet RS=stm.executeQuery(sql);
                )
        {
            while(RS.next()){
                
                Eleve e=new Eleve(
                RS.getInt("id"),
                RS.getString("code"),
                RS.getString("nom"),
                RS.getString("prenom"),
                RS.getString("niveau"),
                RS.getString("code_fil")
               );
                liste.add(e);
               
            }
    }catch(SQLException ex){
        System.out.println("Erreur!!!"+ex.getMessage());
    
    }
        return liste;
    }
    
   
    
    public boolean existe(String codeEleve) {
    String sql = "SELECT 1 FROM Eleve WHERE code = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, codeEleve);
        ResultSet rs = ps.executeQuery();
        return rs.next(); 
    } catch (SQLException e) {
        System.out.println("Erreur existe: " + e.getMessage());
    }
    return false;
}
 
  public Eleve getEleve_Par_code(String code) {
    String sql = "SELECT * FROM Eleve WHERE code = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, code);
        ResultSet RS = ps.executeQuery();
        if (RS.next()) {
            return new Eleve(
                RS.getInt("id"),
                RS.getString("code"),
                RS.getString("nom"),
                RS.getString("prenom"),
                RS.getString("niveau"),
                RS.getString("code_fil")
            );
        }
    } catch (SQLException ex) {
        System.out.println("Erreur!!!" + ex.getMessage());
    }
    return null;
}
}
