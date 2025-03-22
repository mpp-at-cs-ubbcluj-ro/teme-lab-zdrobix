package org.example.service;

import org.example.domain.LoginInfo;
import org.example.repository.db.LoginDbRepository;

public class ServiceLogin {
    private LoginDbRepository Repo;

    public ServiceLogin(LoginDbRepository repo) {
        this.Repo = repo;
    }

    public LoginInfo GetByUsername (String username) {
        return this.Repo.GetByUsername(username);
    }
}
