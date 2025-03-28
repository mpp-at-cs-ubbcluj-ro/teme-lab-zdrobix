package org.example.service;

import org.example.domain.Child;
import org.example.repository.db.ChildDbRepository;
import org.example.repository.interf.IChildRepository;

public class ServiceChild {
    private IChildRepository Repo;

    public ServiceChild(IChildRepository repo) {
        this.Repo = repo;
    }

    public Iterable<Child> GetAll() {
        return this.Repo.FindAll();
    }

    public Child GetById(int id) {
        return this.Repo.FindById(id);
    }

    public void AddChild (String name, String cnp) {
        this.Repo.Save(new Child(name, cnp));
    }

    public Child UpdateChild (Child child) {
        return this.Repo.Update(child);
    }

    public Iterable<Child> GetByAge (int age) {
        return this.Repo.GetAllByAge(age);
    }
}
