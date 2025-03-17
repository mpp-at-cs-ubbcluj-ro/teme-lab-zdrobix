package org.example;

import org.example.domain.Event;
import org.example.domain.Signup;
import org.example.repository.db.ChildDbRepository;
import org.example.domain.Child;
import org.example.repository.db.EventDbRepository;
import org.example.repository.db.SignupDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties(System.getProperties());
        try {
            properties.load(new FileReader("bd.config"));
            System.setProperties(properties);
            System.getProperties().list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        var ChildDbRepo = new ChildDbRepository(properties);
        for (var ch : ChildDbRepo.FindAll())
            System.out.println(ch);

        var EventDbRepo = new EventDbRepository(properties);
        for (var ev : EventDbRepo.FindAll())
            System.out.println(ev);

        var SignupDbRepo = new SignupDbRepository(properties);
        for (var sg : SignupDbRepo.FindAll())
            System.out.println(sg);
    }
}