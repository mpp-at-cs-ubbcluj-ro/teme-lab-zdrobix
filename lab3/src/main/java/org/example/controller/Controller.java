package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.domain.Child;
import org.example.domain.Event;
import org.example.domain.Signup;
import org.example.service.ServiceChild;
import org.example.service.ServiceEvent;
import org.example.service.ServiceSignup;
import org.example.service.ServiceLogin;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Controller {
    private static Controller Instance;

    private static ServiceChild ServiceChild;
    private static ServiceSignup ServiceSignup;
    private static ServiceLogin ServiceLogin;
    private static ServiceEvent ServiceEvent;

    private static int SelectedChildId = -1;
    private static int SelectedEventId = -1;

    @FXML
    private static TextField childField = new TextField(), eventField = new TextField();

    private static ObservableList<Child> modelChild;
    private static ObservableList<Event> modelEvent;
    private static ObservableList<Signup> modelSignup;

    private Controller() {};

    public static Controller GetInstance() {
        if (Instance == null)
            Instance = new Controller();
        return Instance;
    }

    public static void SetController(ServiceChild serviceChild,
                                     ServiceSignup serviceSignup,
                                     ServiceLogin serviceLogin,
                                     ServiceEvent serviceEvent) {
        ServiceChild = serviceChild;
        ServiceSignup = serviceSignup;
        ServiceLogin = serviceLogin;
        ServiceEvent = serviceEvent;
    }

    public static void StartApp () {
        VBox vbox = CreateLoginVBox();

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vbox);

        Stage loginStage = new Stage();
        Scene MainScene = new Scene(stackPane, 800, 600);
        MainScene.getStylesheets().add(Controller.class.getResource("/css/styles.css").toExternalForm());
        loginStage.setTitle("Login Page");
        loginStage.setScene(MainScene);
        loginStage.show();
    }

    public static VBox CreateLoginVBox () {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button buttonLogin = new Button("Login");
        buttonLogin.setMaxWidth(200);
        buttonLogin.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            var loginInfo = ServiceLogin.GetByUsername(username);
            if (loginInfo == null) {
                showAlert("Invalid username.", Alert.AlertType.ERROR);
                return;
            }
            if (Objects.equals(loginInfo.GetPassword(), password)) {
                showMenuPage();
                Stage stage = (Stage) buttonLogin.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Invalid password.", Alert.AlertType.ERROR);
            }
        });

        vbox.getChildren().addAll(usernameField, passwordField, buttonLogin);
        return vbox;
    }

    private static void showMenuPage() {
        VBox menuVBox = createMenuVBox();

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(menuVBox);

        Stage menuStage = new Stage();
        Scene menuScene = new Scene(stackPane, 800, 600);
        menuScene.getStylesheets().add(Controller.class.getResource("/css/styles.css").toExternalForm());
        menuStage.setTitle("Menu Page");
        menuStage.setScene(menuScene);
        menuStage.show();
    }

    private static VBox createMenuVBox() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        var childMenu = createChildMenu();
        var eventMenu = createEventMenu();
        var signupMenu = createSignupMenu();
        var refresh = new Button("Refresh");
        refresh.setPrefWidth(200);
        refresh.setOnAction(e -> {
            showMenuPage();
            Stage stage = (Stage) vbox.getScene().getWindow();
            stage.close();
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(childMenu,eventMenu, signupMenu);
        vbox.getChildren().addAll(hbox, refresh);

        return vbox;
    }

    private static VBox createChildMenu() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        modelChild = FXCollections.observableArrayList();
        modelChild.setAll((Collection<? extends Child>) ServiceChild.GetAll());

        TableView<Child> childTable = new TableView<>();
        TableColumn<Child, String> childIdColumn = new TableColumn<>("Id");
        TableColumn<Child, String> childNameColumn = new TableColumn<>("Name");
        TableColumn<Child, String> childCnpColumn = new TableColumn<>("CNP");

        childIdColumn.setPrefWidth(20);
        childIdColumn.setVisible(false);
        childNameColumn.setPrefWidth(100);
        childCnpColumn.setPrefWidth(100);
        childTable.setMaxWidth(200);
        childTable.setMaxHeight(200);

        childIdColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("" + cellData.getValue().GetId());
        });
        childNameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().GetName());
        });
        childCnpColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().GetCNP());
        });

        childTable.getColumns().addAll(childIdColumn, childNameColumn, childCnpColumn);
        childTable.setItems(modelChild);

        HBox searchName = new HBox(10);

        TextField searchNameField = new TextField();
        searchNameField.setPromptText("Name");
        searchNameField.setMaxWidth(100);

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(90);
        searchButton.setOnAction(e -> {
            if (searchNameField.getText().isEmpty()) {
                modelChild.setAll((Collection<Child>)ServiceChild.GetAll());
                return;
            }
            var searchResult = ((Collection<Child>) ServiceChild.GetAll())
                    .stream()
                    .filter(
                            x ->
                                    x.GetName()
                                            .toLowerCase()
                                            .contains(
                                                    searchNameField
                                                            .getText()
                                                            .toLowerCase()
                                                            .strip()
                                            )
                    )
                    .collect(Collectors.toList());
            modelChild.setAll(
                    searchResult
            );
        });

        searchName.getChildren().addAll(searchNameField, searchButton);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setMaxWidth(200);

        TextField cnpField = new TextField();
        cnpField.setPromptText("Cnp");
        cnpField.setMaxWidth(200);

        childTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedChildId = newValue.GetId();
                childField.setText("" + SelectedChildId);
                nameField.setText(newValue.GetName());
                cnpField.setText(newValue.GetCNP());
            }
        });

        Button addChildButton = new Button("Add");
        addChildButton.setPrefWidth(200);
        addChildButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || cnpField.getText().isEmpty() || cnpField.getText().length() < 6) {
                showAlert("Invalid name or cnp.", Alert.AlertType.ERROR);
                return;
            }
            ServiceChild.AddChild(nameField.getText(), cnpField.getText());
            nameField.clear();
            cnpField.clear();
        });

        Button updateChild = new Button("Update");
        updateChild.setPrefWidth(200);
        updateChild.setOnAction(e -> {
            if (nameField.getText().isEmpty() || cnpField.getText().isEmpty() || cnpField.getText().length() < 6) {
                showAlert("Invalid name or cnp.", Alert.AlertType.ERROR);
                return;
            }
            if (SelectedChildId == -1) {
                showAlert("Child not selected.", Alert.AlertType.ERROR);
                return;
            }
            var oldChild = ServiceChild.GetById(SelectedChildId);
            var newChild = ServiceChild.UpdateChild((Child)new Child(nameField.getText(), cnpField.getText()).SetId(SelectedChildId));
            nameField.clear();
            cnpField.clear();
            //modelChild.set(modelChild.indexOf(oldChild), newChild);
        });

        vbox.getChildren().addAll(searchName, childTable, nameField, cnpField, addChildButton, updateChild);

        return vbox;
    }

    private static VBox createEventMenu() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        modelEvent = FXCollections.observableArrayList();
        modelEvent.setAll((Collection<? extends Event>) ServiceEvent.GetAll());

        TableView<Event> eventTable = new TableView<>();
        TableColumn<Event, String> eventIdColumn = new TableColumn<>("Id");
        TableColumn<Event, String> eventNameColumn = new TableColumn<>("Name");
        TableColumn<Event, String> eventAgeColumn = new TableColumn<>("Age Interval");

        eventIdColumn.setPrefWidth(20);
        eventIdColumn.setVisible(false);
        eventNameColumn.setPrefWidth(100);
        eventAgeColumn.setPrefWidth(100);
        eventTable.setMaxWidth(200);
        eventTable.setMaxHeight(200);

        eventIdColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("" + cellData.getValue().GetId());
        });
        eventNameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().GetName());
        });
        eventAgeColumn.setCellValueFactory(cellData -> {
            var data = cellData.getValue();
            return new SimpleStringProperty("" + data.GetMinAge() + "-" + data.GetMaxAge());
        });

        eventTable.getColumns().addAll(eventIdColumn, eventNameColumn, eventAgeColumn);
        eventTable.setItems(modelEvent);

        HBox searchName = new HBox(10);

        TextField searchNameField = new TextField();
        searchNameField.setPromptText("Name");
        searchNameField.setMaxWidth(100);

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(90);
        searchButton.setOnAction(e -> {
            if (searchNameField.getText().isEmpty()) {
                modelEvent.setAll((Collection<Event>) ServiceEvent.GetAll());
                return;
            }
            var searchResult = ((Collection<Event>) ServiceEvent.GetAll())
                    .stream()
                    .filter(
                            x ->
                                    x.GetName()
                                            .toLowerCase()
                                            .contains(
                                                    searchNameField
                                                            .getText()
                                                            .toLowerCase()
                                                            .strip()
                                            )
                    )
                    .collect(Collectors.toList());
            modelEvent.setAll(
                    searchResult
            );
        });

        searchName.getChildren().addAll(searchNameField, searchButton);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setMaxWidth(200);

        TextField minAgeField = new TextField();
        minAgeField.setPromptText("Min");
        minAgeField.setMaxWidth(90);

        TextField maxAgeField = new TextField();
        maxAgeField.setPromptText("Max");
        maxAgeField.setMaxWidth(90);

        eventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedEventId = newValue.GetId();
                eventField.setText("" + SelectedEventId);
                nameField.setText(newValue.GetName());
                minAgeField.setText("" + newValue.GetMinAge());
                maxAgeField.setText("" + newValue.GetMaxAge());
            }
        });

        HBox ageHBox = new HBox(20);
        ageHBox.setAlignment(Pos.CENTER);
        ageHBox.getChildren().addAll(minAgeField, maxAgeField);

        Button addEventButton = new Button("Add");
        addEventButton.setPrefWidth(200);
        addEventButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || minAgeField.getText().isEmpty() || maxAgeField.getText().isEmpty()) {
                showAlert("Empty name or age.", Alert.AlertType.ERROR);
                return;
            }
            int minAge = Integer.parseInt(minAgeField.getText());
            int maxAge = Integer.parseInt(maxAgeField.getText());
            if (minAge > maxAge) {
                showAlert("Invalid age.", Alert.AlertType.ERROR);
                return;
            }
            ServiceEvent.Add(nameField.getText(), minAge, maxAge);
            nameField.clear();
            minAgeField.clear();
            maxAgeField.clear();
        });

        Button updateEventButton = new Button("Update");
        updateEventButton.setPrefWidth(200);
        updateEventButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || minAgeField.getText().isEmpty() || maxAgeField.getText().isEmpty()) {
                showAlert("Invalid name or age.", Alert.AlertType.ERROR);
                return;
            }
            if (SelectedEventId == -1) {
                showAlert("Event not selected.", Alert.AlertType.ERROR);
                return;
            }
            int minAge, maxAge;
            try {
                minAge = Integer.parseInt(minAgeField.getText());
                maxAge = Integer.parseInt(maxAgeField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid age.", Alert.AlertType.ERROR);
                return;
            }
            Event oldEvent = ServiceEvent.GetById(SelectedEventId);
            Event newEvent = ServiceEvent.UpdateEvent((Event) new Event(nameField.getText(), minAge, maxAge).SetId(SelectedEventId));
            nameField.clear();
            minAgeField.clear();
            maxAgeField.clear();
        });

        vbox.getChildren().addAll(searchName, eventTable, nameField, ageHBox, addEventButton, updateEventButton);

        return vbox;
    }

    private static VBox createSignupMenu() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        var signups = (Collection<? extends Signup>) ServiceSignup.GetAllMapped(ServiceChild, ServiceEvent);

        modelSignup = FXCollections.observableArrayList();
        modelSignup.setAll(
                signups
        );

        TableView<Signup> signupTable = new TableView<>();
        TableColumn<Signup, String> childColumn = new TableColumn<>("Child");
        TableColumn<Signup, String> eventColumn = new TableColumn<>("Event");

        childColumn.setPrefWidth(100);
        eventColumn.setPrefWidth(100);
        signupTable.setMaxWidth(200);
        signupTable.setMaxHeight(200);

        childColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(ServiceChild.GetById(cellData.getValue().GetId().GetFirst()).GetName());
        });
        eventColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(ServiceEvent.GetById(cellData.getValue().GetId().GetSecond()).GetName());
        });

        signupTable.getColumns().addAll(childColumn, eventColumn);
        signupTable.setItems(modelSignup);

        HBox searchName = new HBox(10);

        TextField searchIdField = new TextField();
        searchIdField.setPromptText("Id");
        searchIdField.setMaxWidth(40);

        Button searchChildIdButton = new Button("Child");
        searchChildIdButton.setPrefWidth(70);
        searchChildIdButton.setOnAction(e -> {
            if (searchIdField.getText().isEmpty()) {
                modelSignup.setAll();
                return;
            }
            int id;
            try {
                id = Integer.parseInt(searchIdField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid id format.", Alert.AlertType.ERROR);
                return;
            }
            var searchResult = signups
                    .stream()
                    .filter(
                            x ->
                                    x.GetChild().GetId() == id
                    )
                    .collect(Collectors.toList());
            modelSignup.setAll(
                    searchResult
            );
        });

        Button searchEventIdButton = new Button("Event");
        searchEventIdButton.setPrefWidth(70);
        searchEventIdButton.setOnAction(e -> {
            if (searchIdField.getText().isEmpty()) {
                modelSignup.setAll(signups);
                return;
            }
            int id;
            try {
                id = Integer.parseInt(searchIdField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid id format.", Alert.AlertType.ERROR);
                return;
            }
            var searchResult = signups
                    .stream()
                    .filter(
                            x ->
                                    x.GetEvent().GetId() == id
                    )
                    .collect(Collectors.toList());
            modelSignup.setAll(
                    searchResult
            );
        });

        searchName.getChildren().addAll(searchIdField, searchChildIdButton, searchEventIdButton);

        childField.setEditable(false);
        childField.setMaxWidth(200);

        eventField.setEditable(false);
        eventField.setMaxWidth(200);

        Button addSignupButton = new Button("Add");
        addSignupButton.setPrefWidth(200);
        addSignupButton.setOnAction(e -> {
            if (childField.getText().isEmpty() || eventField.getText().isEmpty()) {
                showAlert("Invalid child or event.", Alert.AlertType.ERROR);
                return;
            }
            ServiceSignup.AddSignup(
                    ServiceChild.GetById(Integer.parseInt(childField.getText())),
                    ServiceEvent.GetById(Integer.parseInt(eventField.getText()))
            );
            childField.clear();
            eventField.clear();
        });

        Button deleteSignupButton = new Button("Delete");
        deleteSignupButton.setPrefWidth(200);
        deleteSignupButton.setOnAction(e -> {
            if (SelectedEventId == -1 || SelectedChildId == -1) {
                showAlert("Invalid child or event.", Alert.AlertType.ERROR);
                return;
            }
            Signup deleted = ServiceSignup.Delete(SelectedChildId, SelectedEventId);
        });

        vbox.getChildren().addAll(searchName, signupTable, childField, eventField, addSignupButton, deleteSignupButton);

        return vbox;
    }

    private static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
