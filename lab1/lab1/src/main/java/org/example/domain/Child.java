package org.example.domain;

public class Child {
    private String Name;
    private String CNP;

    public Child (String name, String cnp) {
        Name = name;
        CNP = cnp;
    }

    public String GetName() {
        return this.Name;
    }

    public String GetCNP() {
        return this.CNP;
    }
}
