package org.example.services;

import org.example.domain.Child;
import org.example.domain.Event;
import org.example.domain.LoginInfo;
import org.example.domain.Signup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service implements IService {
    private IServiceChild ServiceChild;
    private IServiceEvent ServiceEvent;
    private IServiceSignup ServiceSignup;
    private IServiceLogin ServiceLogin;
    private Map<Integer, IObserver> loggedUsers;

    public Service(IServiceChild serviceChild, IServiceEvent serviceEvent, IServiceSignup serviceSignup, IServiceLogin serviceLogin) {
        ServiceChild = serviceChild;
        ServiceEvent = serviceEvent;
        ServiceSignup = serviceSignup;
        ServiceLogin = serviceLogin;
        this.loggedUsers = new HashMap<>();
    }

    public IServiceChild getServiceChild() {
        return ServiceChild;
    }

    public IServiceEvent getServiceEvent() {
        return ServiceEvent;
    }

    public IServiceSignup getServiceSignup() {
        return ServiceSignup;
    }

    public IServiceLogin getServiceLogin() {
        return ServiceLogin;
    }

    @Override
    public synchronized Iterable<Child> GetAllChildren() {
        return ServiceChild.GetAll();
    }

    @Override
    public synchronized Child GetChildById(int id) {
        return ServiceChild.GetById(id);
    }

    @Override
    public synchronized Child AddChild(String name, String cnp) {
        return ServiceChild.AddChild(name, cnp);
    }

    @Override
    public synchronized Child UpdateChild(Child child) {
        return ServiceChild.UpdateChild(child);
    }

    @Override
    public synchronized Iterable<Child> GetChildByAge(int age) {
        return ServiceChild.GetByAge(age);
    }

    @Override
    public synchronized Iterable<Event> GetAllEvents() {
        return ServiceEvent.GetAll();
    }

    @Override
    public synchronized Event GetEventById(int id) {
        return ServiceEvent.GetById(id);
    }

    @Override
    public synchronized Event AddEvent(String name, int minAge, int maxAge) {
        return ServiceEvent.Add(name, minAge, maxAge);
    }

    @Override
    public synchronized Event UpdateEvent(Event event) {
        return ServiceEvent.UpdateEvent(event);
    }

    @Override
    public synchronized LoginInfo GetByUsername(String username) {
        return ServiceLogin.GetByUsername(username);
    }

    @Override
    public synchronized Iterable<Signup> GetAllSignups() {
        return ServiceSignup.GetAll();
    }

    @Override
    public synchronized Signup AddSignup(Child child, Event event) {
        return ServiceSignup.AddSignup(child, event);
    }

    @Override
    public synchronized Signup GetSignupById(int childId, int eventId) {
        return ServiceSignup.GetById(childId, eventId);
    }

    @Override
    public synchronized Iterable<Signup> GetSignupByEvent(int eventId) {
        return ServiceSignup.GetByEvent(eventId);
    }

    @Override
    public synchronized Iterable<Signup> GetSignupByChild(int childId) {
        return ServiceSignup.GetByChild(childId);
    }

    @Override
    public synchronized Iterable<Signup> GetAllSignupsMapped(IServiceChild serviceChild, IServiceEvent serviceEvent) {
        return ServiceSignup.GetAllMapped(serviceChild, serviceEvent);
    }

    @Override
    public synchronized Signup DeleteSignup(int childId, int eventId) {
        return null;
    }

    @Override
    public synchronized LoginInfo login(LoginInfo login, IObserver client) {
        this.loggedUsers.put(login.GetId(), client);
        return login;
    }
}
