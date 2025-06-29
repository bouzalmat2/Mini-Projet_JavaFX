package com.mycompany.javafx_mvc.controllers;

import com.mycompany.javafx_mvc.dao.LoginDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;

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
                
                // Create LoginDAO instance instead of static call
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