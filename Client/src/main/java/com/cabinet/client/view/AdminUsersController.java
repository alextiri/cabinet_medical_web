package com.cabinet.client.view;

import com.cabinet.client.dto.CreateUserRequest;
import com.cabinet.client.dto.UpdateUserRequest;
import com.cabinet.client.model.User;
import com.cabinet.client.service.UserService;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.UserRole;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminUsersController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<UserRole> roleFilterCombo;
    @FXML
    private ComboBox<UserRole> roleCombo;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private List<User> allUsers;
    private String getTranslatedRole(UserRole role) {
        return switch (role) {
            case ALL -> LanguageManager.getBundle().getString("role.all");
            case ADMIN -> LanguageManager.getBundle().getString("role.admin");
            case DOCTOR -> LanguageManager.getBundle().getString("role.doctor");
            case ASSISTANT -> LanguageManager.getBundle().getString("role.assistant");
        };
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(cellData -> {
            UserRole role = cellData.getValue().getRole();

            return new SimpleStringProperty(
                    getTranslatedRole(role)
            );
        });

        roleCombo.setItems(
                FXCollections.observableArrayList(
                        UserRole.ADMIN,
                        UserRole.DOCTOR,
                        UserRole.ASSISTANT
                )
        );

        roleCombo.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(UserRole item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(getTranslatedRole(item));
                        }
                    }
                }
        );

        roleCombo.setButtonCell(
                roleCombo.getCellFactory().call(null)
        );

        roleFilterCombo.setItems(
                FXCollections.observableArrayList(
                        UserRole.ALL,
                        UserRole.ADMIN,
                        UserRole.DOCTOR,
                        UserRole.ASSISTANT
                )
        );
        roleFilterCombo.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(UserRole item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(getTranslatedRole(item));
                        }
                    }
                }
        );

        roleFilterCombo.setButtonCell(roleFilterCombo.getCellFactory().call(null));
        roleFilterCombo.setValue(UserRole.ALL);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterUsers();
        });

        roleFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUsers();
        });

        userTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldUser, selectedUser) -> {
                    if (selectedUser == null) {
                        return;
                    }

                    firstNameField.setText(selectedUser.getFirstName());
                    lastNameField.setText(selectedUser.getLastName());
                    usernameField.setText(selectedUser.getUsername());
                    passwordField.clear();
                    roleCombo.setValue(selectedUser.getRole());
                });

        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        loadUsers();
    }

    private void loadUsers() {
        UserService userService = new UserService();
        userService.getAllUsers()
                .thenAccept(users -> {
                    Platform.runLater(() -> {
                        allUsers = users;
                        userTable.setItems(FXCollections.observableArrayList(users));
                    });
                });
    }

    private void filterUsers() {
        if (allUsers == null) {
            return;
        }

        String search = searchField.getText().toLowerCase();
        UserRole selectedRole = roleFilterCombo.getValue();

        List<User> filtered = allUsers.stream()
                .filter(user -> {
                    boolean matchesSearch = user.getUsername().toLowerCase().contains(search);
                    boolean matchesRole = selectedRole == UserRole.ALL || user.getRole().equals(selectedRole);
                    return matchesSearch && matchesRole;
                })
                .toList();

        userTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    protected void onResetFiltersClick() {
        searchField.clear();
        roleFilterCombo.setValue(UserRole.ALL);
    }

    @FXML
    protected void onResetFieldsClick() {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        passwordField.clear();
        roleCombo.setValue(null);

        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    protected void onAddUserClick() {
        try {
            CreateUserRequest request = new CreateUserRequest();

            request.setFirstName(firstNameField.getText());
            request.setLastName(lastNameField.getText());
            request.setUsername(usernameField.getText());
            request.setPassword(passwordField.getText());
            request.setRole(roleCombo.getValue().name());

            UserService userService = new UserService();

            userService.createUser(request)
                    .thenAccept(createdUser -> {
                        Platform.runLater(() -> {
                            loadUsers();
                            onResetFieldsClick();
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUpdateUserClick() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            return;
        }

        try {
            UpdateUserRequest request = new UpdateUserRequest();

            request.setFirstName(firstNameField.getText());
            request.setLastName(lastNameField.getText());
            request.setUsername(usernameField.getText());
            request.setPassword(passwordField.getText());
            request.setRole(roleCombo.getValue().name());

            UserService userService = new UserService();

            userService.updateUser(selectedUser.getId(), request)
                    .thenAccept(updatedUser -> {
                        Platform.runLater(() -> {
                            loadUsers();
                            onResetFieldsClick();
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteUserClick() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete selected user?");
        alert.setContentText(selectedUser.getUsername());

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                UserService userService = new UserService();

                userService.deleteUser(selectedUser.getId()).thenRun(() -> {
                    Platform.runLater(() -> {
                        loadUsers();
                        onResetFieldsClick();
                    });
                });
            }
        });
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("admin-dashboard-view.fxml");
    }
}