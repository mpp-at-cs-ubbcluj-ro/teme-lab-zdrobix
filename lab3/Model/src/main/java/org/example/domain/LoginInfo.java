package org.example.domain;

public class LoginInfo extends Entity<Integer>{
    private String Username;
    private String Password;

    public LoginInfo(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public String GetUsername() {
        return this.Username;
    }

    public String GetPassword() {
        return this.Password;
    }
}
