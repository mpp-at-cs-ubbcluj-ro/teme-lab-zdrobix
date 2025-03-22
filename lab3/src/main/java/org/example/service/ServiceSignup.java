package org.example.service;

import org.example.domain.Child;
import org.example.domain.Event;
import org.example.domain.Signup;
import org.example.domain.Tuple;
import org.example.repository.db.SignupDbRepository;

public class ServiceSignup {
    private SignupDbRepository Repo;

    public ServiceSignup(SignupDbRepository repo) {
        this.Repo = repo;
    }

    public Iterable<Signup> GetAll() {
        return this.Repo.FindAll();
    }

    public void AddSignup (Child child, Event event) {
        this.Repo.Save(new Signup(child, event));
    }

    public Signup GetById (int childId, int eventId) {
        return this.Repo.FindById(new Tuple(childId, eventId));
    }

    public Iterable<Signup> GetByEvent (int eventId) {
        return this.Repo.GetAllByEventId(eventId);
    }

    public Iterable<Signup> GetByChild (int childId) {
        return this.Repo.GetAllByChildId(childId);
    }

    public Signup Delete (int childId, int eventId) {
        return this.Repo.Delete(new Tuple(childId, eventId));
    }
}
