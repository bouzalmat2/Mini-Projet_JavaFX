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
        btn_login.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                // Create LoginDAO instance instead of static call
                new LoginDAO().loginUser(event, tf_username.getText(), tf_password.getText());
            }
        });
        
        btn_contact.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username's incorrect!");
                alert.show();
            }
        });    
    }
}