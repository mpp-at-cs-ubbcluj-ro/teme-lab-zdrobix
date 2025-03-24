package org.example;

import org.example.controller.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.example.repository.db.ChildDbRepository;
import org.example.repository.db.EventDbRepository;
import org.example.repository.db.LoginDbRepository;
import org.example.repository.db.SignupDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.service.ServiceChild;
import org.example.service.ServiceEvent;
import org.example.service.ServiceLogin;
import org.example.service.ServiceSignup;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/org/example/views/main_view.fxml"));

        Properties properties = new Properties(System.getProperties());
        try {
            properties.load(new FileReader("bd.config"));
            System.setProperties(properties);
            System.getProperties().list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        var ChildDbRepo = new ChildDbRepository(properties);
        var EventDbRepo = new EventDbRepository(properties);
        var SignupDbRepo = new SignupDbRepository(properties);
        var LoginDbRepo = new LoginDbRepository(properties);

        Controller.SetController(
                new ServiceChild(ChildDbRepo),
                new ServiceSignup(SignupDbRepo),
                new ServiceLogin(LoginDbRepo),
                new ServiceEvent(EventDbRepo)
        );

        loader.setController(Controller.GetInstance());
        Controller.StartApp();
    }

    public static void main(String[] args) {
        launch(args);
    }
}