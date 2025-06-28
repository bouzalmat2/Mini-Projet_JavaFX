package com.mycompany.javafx_mvc.dao;

import com.mycompany.javafx_mvc.controllers.MainController;
import com.mycompany.javafx_mvc.db.Connexion;
import com.mycompany.javafx_mvc.dao.LoginDAO;
import java.io.*;
import java.util.*;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginDAO {
    private final Connection con;
    
    public LoginDAO(){
        con=Connexion.getConnection();
    }
    
    
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name){
        Parent root = null; 
        
        if(name != null){
            try{
                FXMLLoader loader = new FXMLLoader(LoginDAO.class.getResource(fxmlFile));
                root = loader.load();
                MainController mncntrl = loader.getController();
                mncntrl.set_name(name);
            }catch(IOException ex){
                System.err.println("Error : "+ ex.getMessage());
            }
        }else{
            try{
                root = FXMLLoader.load(LoginDAO.class.getResource(fxmlFile));
            }catch(IOException ex){
                System.err.println("Error : "+ ex.getMessage());
            }
        }
        
        Stage stage = new (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    
    public void login_user(ActionEvent event, String username, String password) throws SQLException{
        PreparedStatement Pstm = null;
        ResultSet rs = null ;
        
        try{
            Pstm = con.prepareStatement("SELECT name, username, password FROM user WHERE username = ?");
            Pstm.setString(2, username);
            rs = Pstm.executeQuery();
            
            if(!rs.isBeforeFirst()){
                System.out.println("User not found !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username's incorrect!");
                alert.show();
            }else{
                while(rs.next()){
                    String nm = rs.getString("name");
                    String usnm = rs.getString("username");
                    String pwd = rs.getString("password");
                    if(pwd.equals(password)){
                        LoginDAO.changeScene(event, "Main.fxml", "Main", nm);
                    }
                }
            }
            
        }catch(Exception ex){
            System.err.println("Error : "+ ex.getMessage());
        }
    }
    
}
