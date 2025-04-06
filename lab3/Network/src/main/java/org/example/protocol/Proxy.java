package org.example.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Child;
import org.example.domain.Event;
import org.example.domain.LoginInfo;
import org.example.domain.Signup;
import org.example.services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Proxy implements IService {
    private String host;
    private int port;

    private static Logger logger = LogManager.getLogger(Proxy.class);

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    @Override
    public Iterable<Child> GetAllChildren() {
        try {
            initializeConnection();
            var request = new Request().setType(RequestType.GET_CHILDREN);
            sendRequest(request);
            var response = readResponse();
            if (response.getType() == ResponseType.CHILDREN_RECEIVED)
                return response.getEntities().stream()
                                        .map(e -> (Child) e)
                                        .collect(Collectors.toList());

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public Child GetChildById(int id) {
        return null;
    }

    @Override
    public Child AddChild(String name, String cnp) {
        try {
            initializeConnection();
            var request = new Request().setType(RequestType.ADD_CHILD).setEntity(new Child(name, cnp));
            sendRequest(request);
            var response = readResponse();
            if (response.getType() == ResponseType.CHILD_ADDED)
                return (Child)response.getEntity();

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public Child UpdateChild(Child child) {
        return null;
    }

    @Override
    public Iterable<Child> GetChildByAge(int age) {
        return null;
    }

    @Override
    public Iterable<Event> GetAllEvents() {
        try {
            initializeConnection();
            var request = new Request().setType(RequestType.GET_EVENTS);
            sendRequest(request);
            var response = readResponse();
            if (response.getType() == ResponseType.EVENTS_RECEIVED)
                return response.getEntities().stream()
                        .map(e -> (Event) e)
                        .collect(Collectors.toList());

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public Event GetEventById(int id) {
        return null;
    }

    @Override
    public Event AddEvent(String name, int minAge, int maxAge) {
        try {
            initializeConnection();
            var request = new Request().setType(RequestType.ADD_EVENT).setEntity(new Event(name, minAge, maxAge));
            sendRequest(request);
            var response = readResponse();
            if (response.getType() == ResponseType.EVENT_ADDED)
                return (Event) response.getEntity();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public Event UpdateEvent(Event event) {
        return null;
    }

    @Override
    public LoginInfo GetByUsername(String username) {
        return null;
    }

    @Override
    public Iterable<Signup> GetAllSignups() {
        try {
            initializeConnection();
            var request = new Request().setType(RequestType.GET_SIGNUPS);
            sendRequest(request);
            var response = readResponse();
            if (response.getType() == ResponseType.SIGNUPS_RECEIVED)
                return response.getEntities().stream()
                        .map(e -> (Signup) e)
                        .collect(Collectors.toList());

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public Signup AddSignup(Child child, Event event) {
        return null;
    }

    @Override
    public Signup GetSignupById(int childId, int eventId) {
        return null;
    }

    @Override
    public Iterable<Signup> GetSignupByEvent(int eventId) {
        return null;
    }

    @Override
    public Iterable<Signup> GetSignupByChild(int childId) {
        return null;
    }

    @Override
    public Iterable<Signup> GetAllSignupsMapped(IServiceChild serviceChild, IServiceEvent serviceEvent) {
        return null;
    }

    @Override
    public Signup DeleteSignup(int childId, int eventId) {
        return null;
    }

    @Override
    public IServiceChild getServiceChild() {
        return null;
    }

    @Override
    public IServiceEvent getServiceEvent() {
        return null;
    }

    @Override
    public IServiceSignup getServiceSignup() {
        return null;
    }

    @Override
    public IServiceLogin getServiceLogin() {
        return null;
    }

    @Override
    public LoginInfo login(LoginInfo login, IObserver client) {
        return null;
    }

    private void closeConnection() {
        logger.debug("Closing connection");
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

    }

    private void sendRequest(Request request)throws Exception {
        logger.debug("Sending request {} ",request);
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Response readResponse() throws Exception {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }
    private void initializeConnection() throws Exception {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            logger.error("Error initializing connection {}",e);
            logger.error(e.getStackTrace());
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){

    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.debug("response received {}", response);
                    try {
                        qresponses.put((Response)response);
                    } catch (InterruptedException e) {
                        logger.error(e);
                        logger.error(e.getStackTrace());
                    }
                } catch (IOException|ClassNotFoundException e) {
                    logger.error("Reading error {}", e);
                }
            }
        }
    }
}
