package com.mycompany.javafx_mvc.controllers;

import com.mycompany.javafx_mvc.dao.LoginDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


public class MainController implements Initializable {

    @FXML
    private Label lbl_welcome;
    
    @FXML
    private Button btn_logout;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_logout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                LoginDAO.changeScene(event,"Login.fxml","Main", null)
            }
        });
    } 
    
    public void set_name(String name){
        lbl_welcome.setText("Welcome "+ name + " !");
    }
    
}
