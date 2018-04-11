package schedule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedule.data.Course;
import schedule.data.CourseList;

import java.io.IOException;

public class SearchController {

    private static SearchController instance;

    public SearchController() {

        instance = this;
    };

    public static SearchController getInstance() {

        return instance;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView<Course> allCoursesTable;

    @FXML
    private JFXTextField deptTextField;

    @FXML
    private JFXTextField courseIDTextField;

    @FXML
    private JFXTextField courseNameTextField;

    @FXML
    private JFXButton scheduleButton;

    private Course selectedCourse;

    @FXML
    public void initialize() throws IOException {

        // get departmemt user can search
        String loggedUser = LoginController.getInstance().getUsername();
        String[] userDepts = User.getInstance().getSubjects(loggedUser);


        // filter for department from the whole list
        ObservableList<Course> masterList = CourseList.getInstance().getCourseList();

        FilteredList<Course> userFilteredList = new FilteredList<>(masterList, p -> true);

        userFilteredList.setPredicate(course -> {
            for (String userDept : userDepts) {
                if (course.getDepartment().contains(userDept)) {
                    return true;
                }
            }
            return false;
        });

        SortedList<Course> userSortedList = new SortedList<>(userFilteredList);
        userSortedList.comparatorProperty().bind(allCoursesTable.comparatorProperty());
        allCoursesTable.setItems(userSortedList);

        // filter for Department
        FilteredList<Course> deptFilteredList = new FilteredList<>(userFilteredList, p -> true);

        deptTextField.setOnKeyPressed( event -> {
            deptTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                deptFilteredList.setPredicate(course -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    if (course.getDepartment().contains(newValue.toUpperCase())) {
                        return true;
                    }

                    return false;
                });
            });

        });
        SortedList<Course> deptSortedList = new SortedList<>(deptFilteredList);
        deptSortedList.comparatorProperty().bind(allCoursesTable.comparatorProperty());
        allCoursesTable.setItems(deptSortedList);


        // filter for course ID
        FilteredList<Course> courseIDFilterList = new FilteredList<>(deptFilteredList, p -> true);

        courseIDTextField.setOnKeyPressed( event -> {
            courseIDTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                courseIDFilterList.setPredicate(course -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    if (course.getCourseID().contains(newValue.toUpperCase())) {
                        return true;
                    }

                    return false;
                });
            });

        });

        SortedList<Course> courseIDSortedList = new SortedList<>(courseIDFilterList);
        courseIDSortedList.comparatorProperty().bind(allCoursesTable.comparatorProperty());
        allCoursesTable.setItems(courseIDSortedList);


        // filter for course name
        FilteredList<Course> courseNameFilterList = new FilteredList<>(courseIDFilterList, p -> true);

        courseNameTextField.setOnKeyPressed( event -> {
            courseNameTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                courseNameFilterList.setPredicate(course -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    if (course.getCourseName().contains(newValue.toUpperCase())) {
                        return true;
                    }

                    return false;
                });
            });

        });

        SortedList<Course> courseNameSortedList = new SortedList<>(courseNameFilterList);
        courseNameSortedList.comparatorProperty().bind(allCoursesTable.comparatorProperty());
        allCoursesTable.setItems(courseNameSortedList);
    }


    public void handleClickButton() throws IOException {
        selectedCourse = allCoursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            showAddCourseDialog();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a course to schedule.");
            alert.showAndWait();
            return;
        }

    }


    private void showAddCourseDialog() throws IOException {

        scheduleButton.getScene().getWindow().hide();

        Stage courseDialog = new Stage();
        courseDialog.initModality(Modality.APPLICATION_MODAL);
        courseDialog.initOwner(borderPane.getScene().getWindow());

        Parent courseDialogParent = FXMLLoader.load(getClass().getResource("AddScheduleDialog.fxml"));
        Scene courseDialogScene = new Scene(courseDialogParent);

        courseDialog.setTitle("Add New Course");
        courseDialog.setResizable(false);
        courseDialog.setScene(courseDialogScene);
        courseDialog.show();

    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

}
