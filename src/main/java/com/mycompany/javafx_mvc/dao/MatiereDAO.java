
package com.mycompany.javafx_mvc.dao;
import com.mycompany.javafx_mvc.models.Matiere;
import com.mycompany.javafx_mvc.db.Connexion;
import java.util.*;
import java.sql.*;

public class MatiereDAO {
    private final Connection con;
    
    public MatiereDAO(){
        con=Connexion.getConnection();
    }
    
    
    
    public List<Matiere>  getMatier_Par_Filier_Et_Niveauet(String code_fil,String niveau){
        List<Matiere> liste=new ArrayList<>();
        String sql = " SELECT *FROM Matier m "+
            "JOIN Module mo ON  m.code_module=mo.code"+ 
            "where mo.code_fil=? AND mo.niveau=?";
        
        try(PreparedStatement ps =con.prepareStatement(sql))      
        {
             ps.setString(1,code_fil);
             ps.setString(2,niveau);
             ResultSet RS=ps.executeQuery();
             
            while(RS.next()){
                liste.add(new Matiere(
                    RS.getInt("id"),
                    RS.getString("code"),
                    RS.getString("designation"),
                    RS.getInt("VH"),
                    RS.getString("code_module")  
                
                ));
            }      
    }catch(SQLException ex){
        System.out.println("Erreur!!!"+ex.getMessage());
    
    }
        return liste;
    }
 

    
    
}
