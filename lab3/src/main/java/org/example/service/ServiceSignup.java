package org.example.service;

import org.example.domain.Child;
import org.example.domain.Event;
import org.example.domain.Signup;
import org.example.domain.Tuple;
import org.example.repository.db.SignupDbRepository;
import org.example.repository.interf.ISignupRepository;

import java.util.ArrayList;

public class ServiceSignup {
    private ISignupRepository Repo;

    public ServiceSignup(ISignupRepository repo) {
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

    public Iterable<Signup> GetAllMapped(ServiceChild serviceChild, ServiceEvent serviceEvent) {
        var result = new ArrayList<Signup>();
        for (var signupNull : this.Repo.FindAll()) {
            result.add(
                    (Signup) new Signup(
                            serviceChild.GetById(signupNull.GetId().GetFirst()),
                            serviceEvent.GetById(signupNull.GetId().GetSecond())
                    ).SetId(
                            signupNull.GetId()
                    )
            );
        }
        return result;
    }
}
