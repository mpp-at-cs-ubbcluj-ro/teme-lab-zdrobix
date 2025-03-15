package org.example;

import org.example.repository.db.ChildDbRepository;
import org.example.domain.Child;

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

        //ChildDbRepo.Save(new Child("Andrei Mitica", "5101010____"));
        //ChildDbRepo.Save(new Child("Florian Matei", "5110409____"));
        //ChildDbRepo.Save(new Child("Anghel Razvan", "5081111____"));
        for (var ch : ChildDbRepo.FindAll())
            System.out.println(ch);
    }
}