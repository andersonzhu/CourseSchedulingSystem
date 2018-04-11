package schedule;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import schedule.datamodel.ScheduleItem;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class DuplicateScheduleDialogController {

    // singleton to return new item
    private static DuplicateScheduleDialogController instance;

    public DuplicateScheduleDialogController() {
        instance = this;
    }

    public static DuplicateScheduleDialogController getInstance() {
        return instance;
    }


    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    @FXML
    private AnchorPane anchorPane;

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
    private JFXButton okButton;


    private ScheduleItem oldScheduleItem;

    public void initialize() {

        oldScheduleItem = ScheduleTableController.getInstance().getSelectedScheduleItem();

        // type: LEC, LAB, PRT, INR, CLN
        String[] typeChoices = new String[] {"LEC", "LAB", "PRT", "INR", "CLN"};
        typeComboBox.setItems(FXCollections.observableArrayList(typeChoices));

        // Section
        String[] sectionLetterChoices = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
        sectionLetterComboBox.setItems(FXCollections.observableArrayList(sectionLetterChoices));

        String[] sectionTypeChoices = new String[] {"", "HY", "NT"};
        sectionTypeComboBox.setItems(FXCollections.observableArrayList(sectionTypeChoices));

        // status combo box
        String[] statusChoices = new String[] {"Open", "Closed"};
        statusComboBox.setItems(FXCollections.observableArrayList(statusChoices));

        // campus combo box
        String[] campusChoices = new String[] {"Main", "South", "Internet", "Clinical", "Hospital", "Honors",
                "Austin College", "HS Academic", "HS Bells", "HS Bonham", "HS Broadcast", "HS Denison",
                "HS Gunter", "HS Howe", "HS Leonard", "HS S&S", "HS Technical", "HS Tioga", "HS Tom Bean",
                "HS Trenton", "HS Van Alstyne", "HS Whitesboro", "HS Whitewright"};
        for (int i = 0; i < campusChoices.length; i++) {
            campusChoices[i] = campusChoices[i].toUpperCase();
        }
        campusComboBox.setItems(FXCollections.observableArrayList(campusChoices));

        // room building
        String[] roomBuildingChoices = new String[] {"AC", "AT", "CA", "CIS", "CJ", "CLINICAL", "CTC", "HOSPITAL",
                "HS", "INTERNET", "LA", "S", "SC", "SCM", "SRC", "STC", "VIT"};
        roomBLDGComboBox.setItems(FXCollections.observableArrayList(roomBuildingChoices));

        editScheduleItem(oldScheduleItem);

    }

    // get new schedule item from search window


    // get new ScheduleItem from user inputs
    public ScheduleItem getNewScheduleItem() {

        String department = deptLabel.getText(); //deptComboBox.getSelectionModel().getSelectedItem().toString();
        String courseID = courseIDLabel.getText();//courseIDTextField.getText().trim();
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        String section = sectionLetterComboBox.getSelectionModel().getSelectedItem().toString() +
                sectionNumberTextField.getText().trim() +
                sectionTypeComboBox.getSelectionModel().getSelectedItem().toString();
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


    // right click or double click to edit a course
    public void editScheduleItem(ScheduleItem scheduleItem) {

        deptLabel.setText(scheduleItem.getDepartment());
        courseIDLabel.setText(scheduleItem.getCourseID());
        typeComboBox.getSelectionModel().select(scheduleItem.getType());

        String section = scheduleItem.getSection();
        String sectionLetter = section.substring(0, 1);
        String sectionNumber = section.substring(1,3);
        sectionLetterComboBox.getSelectionModel().select(sectionLetter);
        sectionNumberTextField.setText(sectionNumber);
        if (section.length() > 3){
            String sectionType = section.substring(3);
            sectionTypeComboBox.getSelectionModel().select(sectionType);
        } else {
            sectionTypeComboBox.getSelectionModel().select("");
        }

        courseNameLabel.setText(scheduleItem.getCourseName());
        instructorTextField.setText(scheduleItem.getInstructor()); // in case of using staff or tbd
        statusComboBox.getSelectionModel().select(scheduleItem.getStatus());
        campusComboBox.getSelectionModel().select(scheduleItem.getCampus());

        String[] room = scheduleItem.getRoom().split("-");
        String roomBuilding = room[0];
        String roomNumber;
        if (roomBuilding.equals("INTERNET")) {
            roomNumber = "";
        } else {
            roomNumber = room[1];
        }
        roomBLDGComboBox.getSelectionModel().select(roomBuilding);
        roomNumberTextField.setText(roomNumber);



        String[] days = scheduleItem.getDays().split("");
        ArrayList<String> daysArrayList = new ArrayList<>(Arrays.asList(days));
        mCheckBox.setSelected(daysArrayList.contains("M"));
        tCheckBox.setSelected(daysArrayList.contains("T"));
        wCheckBox.setSelected(daysArrayList.contains("W"));
        rCheckBox.setSelected(daysArrayList.contains("R"));
        fCheckBox.setSelected(daysArrayList.contains("F"));

        maxTextField.setText(Integer.toString(scheduleItem.getMax()));
        startTimePicker.setValue(LocalTime.parse(scheduleItem.getStartTime(), dateTimeFormatter));
        endTimePicker.setValue(LocalTime.parse(scheduleItem.getEndTime(), dateTimeFormatter));

    } // close editScheduleItem

    // update the course edited
/*    public void updateScheduleItem(ScheduleItem scheduleItem) {
//        scheduleItem.setDepartment(deptComboBox.getSelectionModel().getSelectedItem().toString());
//        scheduleItem.setCourseID(courseIDTextField.getText().trim());
        scheduleItem.setType(typeComboBox.getSelectionModel().getSelectedItem().toString());
        scheduleItem.setSection(sectionLetterComboBox.getSelectionModel().getSelectedItem().toString() +
                sectionNumberTextField.getText().trim() +
                sectionTypeComboBox.getSelectionModel().getSelectedItem().toString());
//        scheduleItem.setCourseName(courseNameTextField.getText().trim());
        scheduleItem.setInstructor(instructorTextField.getText().trim());
        scheduleItem.setStatus(statusComboBox.getSelectionModel().getSelectedItem().toString());
//        scheduleItem.setCredits(Integer.parseInt(creditsTextField.getText().trim()));
        scheduleItem.setCampus(campusComboBox.getSelectionModel().getSelectedItem().toString());

        String room;
        String roomBuilding = roomBLDGComboBox.getSelectionModel().getSelectedItem().toString();
        if (roomBuilding.equals("INTERNET")) {
            room = roomBuilding;
        } else {
            room = roomBuilding + "-" + roomNumberTextField.getText().trim();
        }
        scheduleItem.setRoom(room);

        scheduleItem.setDays(getMeetingDays());
        scheduleItem.setMax(Integer.parseInt(maxTextField.getText()));
        scheduleItem.setStartTime(startTimePicker.getValue());
        scheduleItem.setEndTime(endTimePicker.getValue());
    } // close update ScheduleItem*/


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
