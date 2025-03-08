package org.example.domain;

public class Entity<ID> {
    private ID Id;
    public Entity() {};

    public ID GetId() {
        return this.Id;
    }

    public Entity<ID> SetId(ID Id) {
        this.Id = Id;
        return this;
    }
}
