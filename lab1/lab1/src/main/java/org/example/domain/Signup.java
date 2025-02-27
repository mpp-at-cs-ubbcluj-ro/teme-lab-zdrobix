package org.example.domain;

public class Signup {
    private Child Child;
    private Event Event;

    public Signup (Child child, Event event) {
        this.Child = child;
        this.Event = event;
    }

    public Child GetChild() {
        return this.Child;
    }

    public Event GetEvent() {
        return this.Event;
    }
}
