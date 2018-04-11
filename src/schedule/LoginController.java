package schedule;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

//    private static String filename = "CourseList.txt";

    // want to communicate between two scenes;
    private static LoginController instance;

    public LoginController() {
        instance = this;
    }

    public static LoginController getInstance() {
        return instance;
    }


    @FXML
    public void initialize() {
        loginButton.setDisable(true);
    }


    /**
     * press enter to log in
     *
     * @param ke
     * @throws IOException
     */
    @FXML
    public void onEnter(KeyEvent ke) throws IOException, InterruptedException {

        String usernameText = username.getText().trim();
        String passwordText = password.getText();

        boolean disableButton = usernameText.isEmpty() || passwordText.isEmpty();
        loginButton.setDisable(disableButton);

        if (ke.getCode() == KeyCode.ENTER) {
            boolean result = User.validate(usernameText.toLowerCase(), passwordText);
            if (result) {
                createEmptyFile();
                gotoScheduleScene();
            } else {
                showAlertAndCleanFields();
            }
        }
    }

    /**
     * Click button to log in
     */

    @FXML
    public void onLoginButtonClicked(ActionEvent e) throws IOException, InterruptedException {
        boolean result = User.validate(username.getText().trim().toLowerCase(), password.getText());
        if (result) {
            createEmptyFile();
            gotoScheduleScene();

        } else {
            showAlertAndCleanFields();
        }
    }

    /**
     * show alert when failing to log in
     */

    public void showAlertAndCleanFields() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Incorrect username or password. Please try it again.");
        alert.showAndWait();
        password.setText("");
    }


    /**
     * go to schedule window when logging in successfully
     *
     * @throws IOException
     */
    public void gotoScheduleScene() throws IOException {


        loginButton.getScene().getWindow().hide();

        Stage tableView = new Stage();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("ScheduleTable.fxml"));
        tableView.setTitle("Course Scheduling Assistant");
        Scene tableViewScene = new Scene(tableViewParent);

        tableView.setResizable(true);
        tableView.setScene(tableViewScene);
        tableView.setMaximized(true);
        tableView.show();

    }

    //  get username when logging in
    public String getUsername() {

        if (User.validate(username.getText().trim().toLowerCase(), password.getText())) {
            return username.getText().trim().toLowerCase();
        }
        return null;
    } // close getUsername


    private void createDesiredFile() throws IOException {

        String usernameText = getUsername();



    }


    // create a CourseList file when logging in
    private void createEmptyFile() throws IOException, InterruptedException {

        String usernameText = getUsername();
        File file = new File("Course_Schedule_" + usernameText + ".txt");

        if (!file.exists()) {
            file.createNewFile();
//            Process p = Runtime.getRuntime().exec("attrib +h " + file.getPath()); // make the file hidden
//            p.waitFor();
        }
    } // close createEmptyFile

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        Platform.exit();
    }
}
