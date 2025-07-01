package com.mycompany.javafx_mvc.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.javafx_mvc.dao.LoginDAO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField tf_username;
    
    @FXML
    private TextField tf_password;
    
    @FXML
    private Button btn_login;
    
    @FXML
    private Button btn_contact;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("LoginController initialized");
        
        btn_login.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Login button clicked");
                System.out.println("Username: " + tf_username.getText());
                System.out.println("Password: " + tf_password.getText());
                
                new LoginDAO().loginUser(event, tf_username.getText(), tf_password.getText());
            }
        });
        
        btn_contact.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Contact Administration");
                alert.setHeaderText(null);
                alert.setContentText("+212 6xxxx-xxxx\nensi@ensi.ma");
                alert.show();
            }
        });    
    }
}