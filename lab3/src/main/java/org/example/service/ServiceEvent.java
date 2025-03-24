package org.example.service;

import org.example.domain.Event;
import org.example.repository.interf.IEventRepository;

public class ServiceEvent {
    private IEventRepository Repo;

    public ServiceEvent(IEventRepository repo) {
        this.Repo = repo;
    }

    public Iterable<Event> GetAll() {
        return this.Repo.FindAll();
    }

    public Event GetById(int id) {
        return this.Repo.FindById(id);
    }

    public void Add(String name, int minAge, int maxAge) {
        this.Repo.Save(new Event(name, minAge, maxAge));
    }

    public Event UpdateEvent(Event event) {
        return this.Repo.Update(event);
    }
}
