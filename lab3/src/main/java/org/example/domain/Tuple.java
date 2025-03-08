package org.example.domain;

public class Tuple<T1, T2> {
    private T1 First;
    private T2 Second;

    public Tuple(T1 First, T2 Second) {
        this.First = First;
        this.Second = Second;
    }

    public T1 GetFirst() {
        return this.First;
    }

    public T2 GetSecond() {
        return this.Second;
    }
}
