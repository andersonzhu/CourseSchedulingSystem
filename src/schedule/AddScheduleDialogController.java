package schedule;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import schedule.data.Course;
import schedule.datamodel.ScheduleItem;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class AddScheduleDialogController {

    @FXML
    private JFXComboBox typeComboBox, sectionLetterComboBox, sectionTypeComboBox,
            statusComboBox, campusComboBox, roomBLDGComboBox;

    @FXML
    private JFXTextField sectionNumberTextField, instructorTextField, roomNumberTextField, maxTextField;

    @FXML
    private JFXCheckBox mCheckBox, tCheckBox, wCheckBox, rCheckBox, fCheckBox;

    @FXML
    private Label deptLabel, courseIDLabel, courseNameLabel;

    @FXML
    private JFXTimePicker startTimePicker, endTimePicker;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton okButton, cancelButton;

    public void initialize() {

        Course course = SearchController.getInstance().getSelectedCourse();

        deptLabel.setText(course.getDepartment());
        courseIDLabel.setText(course.getCourseID());
        courseNameLabel.setText(course.getCourseName());

        String[] campusChoices = new String[] {"Main", "South", "Internet", "Clinical", "Hospital", "Honors",
                "Austin College", "HS Academic", "HS Bells", "HS Bonham", "HS Broadcast", "HS Denison",
                "HS Gunter", "HS Howe", "HS Leonard", "HS S&S", "HS Technical", "HS Tioga", "HS Tom Bean",
                "HS Trenton", "HS Van Alstyne", "HS Whitesboro", "HS Whitewright"};



        List campuses = new ArrayList();
        Collections.addAll(campuses, campusChoices);

        // type: LEC, LAB, PRT, INR, CLN
        String[] typeChoices = new String[] {"LEC", "LAB", "PRT", "INR", "CLN"};
        typeComboBox.setItems(FXCollections.observableArrayList(typeChoices));
        typeComboBox.getSelectionModel().selectFirst();

        // Section
        String[] sectionLetterChoices = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
        sectionLetterComboBox.setItems(FXCollections.observableArrayList(sectionLetterChoices));
        sectionLetterComboBox.getSelectionModel().selectFirst();

        String[] sectionTypeChoices = new String[] {"F2F", "HY", "NT"};
        sectionTypeComboBox.setItems(FXCollections.observableArrayList(sectionTypeChoices));
//        sectionTypeComboBox.getSelectionModel().selectFirst();




        Map<String, String[]> camp = new HashMap<>();
        camp.put("NT", new String[] {"Internet"});
        camp.put("HY", campusChoices);
        camp.put("F2F", campusChoices);

        sectionTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                campusComboBox.setItems(FXCollections.observableArrayList(camp.get(newValue)));
            }
        });

        // status combo box
        String[] statusChoices = new String[] {"Open", "Closed"};
        statusComboBox.setItems(FXCollections.observableArrayList(statusChoices));
        statusComboBox.getSelectionModel().selectFirst();

        // campus combo box


        for (int i = 0; i < campusChoices.length; i++) {
            campusChoices[i] = campusChoices[i].toUpperCase();
        }

//        campusComboBox.setItems(FXCollections.observableArrayList(campusChoices));
//        campusComboBox.getSelectionModel().selectFirst();

        Map<String, String[]> rooms = new HashMap<>();

//        rooms.put()



        // room building
        String[] roomBuildingChoices = new String[] {"AC", "AT", "CA", "CIS", "CJ", "CLINICAL", "CTC", "HOSPITAL",
                "HS", "INTERNET", "LA", "S", "SC", "SCM SA", "SCM SB", "SRC", "STC", "VIT"};
        roomBLDGComboBox.setItems(FXCollections.observableArrayList(roomBuildingChoices));
        roomBLDGComboBox.getSelectionModel().selectFirst();




        // start time and end time picker
        startTimePicker.setValue(LocalTime.of(8, 0));
        endTimePicker.setValue(LocalTime.of(8, 0));

    }

    // get new ScheduleItem from user inputs
    public ScheduleItem getNewScheduleItem() {

        String department = deptLabel.getText(); //deptComboBox.getSelectionModel().getSelectedItem().toString();
        String courseID = courseIDLabel.getText();//courseIDTextField.getText().trim();
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();

        String sectionType = sectionTypeComboBox.getSelectionModel().getSelectedItem().toString();
        if (sectionType.equals("F2F")) {
            sectionType = "";
        }
        String section = sectionLetterComboBox.getSelectionModel().getSelectedItem().toString() +
                sectionNumberTextField.getText().trim() +
                sectionType;
        String courseName = courseNameLabel.getText();
        String instructor = instructorTextField.getText().trim();
        String status = statusComboBox.getSelectionModel().getSelectedItem().toString();

        int credit = Integer.parseInt(courseID.substring(1,2));

        String campus = campusComboBox.getSelectionModel().getSelectedItem().toString();

        String room;
        String roomBuilding = roomBLDGComboBox.getSelectionModel().getSelectedItem().toString();
        if (roomBuilding == "INTERNET") {
            room = roomBuilding;
        } else {
            room = roomBuilding + "-" + roomNumberTextField.getText().trim();
        }

        String days = getMeetingDays();

        String maxText = maxTextField.getText().trim();
        int max;
        if (maxText == null || maxText.equals("") ){
            max = 0;
        } else {
            max = Integer.parseInt(maxText);
        }

        // internet course set time as 8am-5pm
        // todo: double check
        LocalTime startTime;
        LocalTime endTime;
        if (section.contains("NT")) {
            startTime = LocalTime.of(8, 0);
            endTime = LocalTime.of(17, 0);
        } else {
            startTime = startTimePicker.getValue();
            endTime = endTimePicker.getValue();
        }

        ScheduleItem newScheduleItem = new ScheduleItem(department, courseID, type, section, courseName, status,
                instructor, campus, room, days, credit, max, startTime, endTime);

        return newScheduleItem;

    } // close getNewScheduleItem

    // handle meeting days selection
    public String getMeetingDays(){

        String meetingDays = "";
        ArrayList<JFXCheckBox> boxes = new ArrayList<>();

        boxes.add(mCheckBox);
        boxes.add(tCheckBox);
        boxes.add(wCheckBox);
        boxes.add(rCheckBox);
        boxes.add(fCheckBox);

        for (JFXCheckBox box : boxes) {
            if (box.isSelected()){
                meetingDays += box.getText();
            }
        }
        return meetingDays;
    } // close getmeetingdays

    // OK button click to add new schedule item
    public void handleOKButtonClick(ActionEvent actionEvent) throws IOException {

        ScheduleItem newScheduleItem = getNewScheduleItem();

        if (!newScheduleItem.isValid()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Course Item Is Not Valid");
            alert.setHeaderText(null);
            alert.setContentText(newScheduleItem.errorMessageOfUserInputs());
            alert.showAndWait();
            return;

        } else if (!newScheduleItem.isUnique()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Course Detected");
            alert.setHeaderText(null);
            alert.setContentText("The course you are scheduling below already exists. \n" +
                    newScheduleItem.getDepartment() + " " +
                    newScheduleItem.getCourseID() + " " +
                    newScheduleItem.getSection());
            alert.showAndWait();
            return;
        } else {
            okButton.getScene().getWindow().hide();
            ScheduleTableController.getInstance().addNewScheduleItem(newScheduleItem);
        }

    }

    // cancel button click to cancel schedule
    public void handleCancelButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

}


