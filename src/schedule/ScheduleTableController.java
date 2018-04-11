package schedule;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedule.datamodel.ScheduleData;
import schedule.datamodel.ScheduleItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ScheduleTableController {


    private static ScheduleTableController instance;

    public ScheduleTableController() {
        instance = this;
    }

    public static ScheduleTableController getInstance() {
        return instance;
    }

    @FXML
    private TableView<ScheduleItem> courseTableView;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ContextMenu courseTableContextMenu;

    public void initialize() throws IOException {

        // context menus for table view
        courseTableContextMenu = new ContextMenu();
        MenuItem editCourseItem = new MenuItem("Edit");
        MenuItem deleteCourseItem = new MenuItem("Delete");
        MenuItem duplicateCourseItem = new MenuItem("Duplicate");
        editCourseItem.setOnAction(event -> {
            try {
                showEditCourseDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        deleteCourseItem.setOnAction(event -> {
            try {
                showDeleteCourseDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        duplicateCourseItem.setOnAction(event -> {
            try {
                showEditCourseDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        courseTableContextMenu.getItems().setAll(editCourseItem, deleteCourseItem, duplicateCourseItem);

        // load local data for tableview
        ScheduleData.getInstance().loadScheduleItems();
        courseTableView.getItems().setAll(ScheduleData.getInstance().getScheduleItems());
        courseTableView.getSelectionModel().selectFirst();

        // add context menus for tableview
        courseTableView.setContextMenu(courseTableContextMenu);
//        courseTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        courseTableView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)){
                try {
                    showDeleteCourseDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    // handle menu item exit
    public void handleExit() { Platform.exit(); }


    // open file from another place
    @FXML
    public void handleOpenFile() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open...");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text", "Course_Schedule_*.txt")
        );

        List<File> files = chooser.showOpenMultipleDialog(borderPane.getScene().getWindow());

        if (files != null) {
            ScheduleData.getInstance().loadCourseItemsFromMultipleFiles(files);
            ScheduleData.getInstance().storeScheduleItems();
            courseTableView.getItems().setAll(ScheduleData.getInstance().getScheduleItems());
        }
    }

    @FXML
    // save file to another place
    public void handleSaveAs(ActionEvent actionEvent) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save as...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));

        File file = chooser.showSaveDialog(borderPane.getScene().getWindow());
        if (file != null) {
            saveFile(courseTableView.getItems(), file);
        }
    }

    @FXML
    // save tableview data to local
    public void saveFile(ObservableList<ScheduleItem> scheduleItems, File file) throws IOException {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (ScheduleItem scheduleItem : scheduleItems) {
                bw.write(scheduleItem.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS!", ButtonType.OK);
            alert.setContentText("Sorry. An error has occurred.");
            alert.showAndWait();
            return;
        }
    }


    // show search dialog to search for a new course
    @FXML
    public void showSearchCourseDialog(ActionEvent actionEvent) throws IOException {

        Stage searchDialog = new Stage();
        searchDialog.initModality(Modality.APPLICATION_MODAL);
        searchDialog.initOwner(borderPane.getScene().getWindow());

        Parent searchDialogParent = FXMLLoader.load(getClass().getResource("Search.fxml"));
        Scene searchDialogScene = new Scene(searchDialogParent, 800, 400);

        searchDialog.setTitle("Course Search");
        searchDialog.setResizable(false);
        searchDialog.setScene(searchDialogScene);
        searchDialog.show();

    } // close show course dialog


    // add new schedule item to table data
    public void addNewScheduleItem(ScheduleItem newScheduleItem) throws IOException {
        ScheduleData.getInstance().addCourseItem(newScheduleItem);
        ScheduleData.getInstance().storeScheduleItems();
        courseTableView.getItems().setAll(ScheduleData.getInstance().getScheduleItems());
        courseTableView.getSelectionModel().select(newScheduleItem);
    }


    // update schedule item
    public void updateScheduleItem(ScheduleItem oldItem, ScheduleItem newItem) throws IOException {
        ScheduleData.getInstance().deleteCourseItem(oldItem);
        ScheduleData.getInstance().addCourseItem(newItem);
        ScheduleData.getInstance().storeScheduleItems();
        courseTableView.getItems().setAll(ScheduleData.getInstance().getScheduleItems());
        courseTableView.getSelectionModel().select(newItem);
    }


    public void gotoEditScheduleDialog() throws IOException {
        Stage courseDialog = new Stage();
        courseDialog.initModality(Modality.APPLICATION_MODAL);
        courseDialog.initOwner(borderPane.getScene().getWindow());

        Parent courseDialogParent = FXMLLoader.load(getClass().getResource("EditScheduleDialog.fxml"));
        Scene courseDialogScene = new Scene(courseDialogParent);

        courseDialog.setTitle("Edit Course");
        courseDialog.setResizable(false);
        courseDialog.setScene(courseDialogScene);
        courseDialog.show();
    }


    public void gotoDuplicateScheduleDialog() throws IOException {
        Stage courseDialog = new Stage();
        courseDialog.initModality(Modality.APPLICATION_MODAL);
        courseDialog.initOwner(borderPane.getScene().getWindow());

        Parent courseDialogParent = FXMLLoader.load(getClass().getResource("DuplicateScheduleDialog.fxml"));
        Scene courseDialogScene = new Scene(courseDialogParent);

        courseDialog.setTitle("Add New Course");
        courseDialog.setResizable(false);
        courseDialog.setScene(courseDialogScene);
        courseDialog.show();
    }


    @FXML
    // edit selected course
    public void showEditCourseDialog() throws IOException {

        ScheduleItem selectedScheduleItem = courseTableView.getSelectionModel().getSelectedItem();

        if (selectedScheduleItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Course Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course you want to edit.");
            alert.showAndWait();
            return;
        } else {
            gotoEditScheduleDialog();
        }

    }

    // add a new course quickly
    public void showDuplicateCourseDialog() throws IOException {

        ScheduleItem selectedScheduleItem = courseTableView.getSelectionModel().getSelectedItem();

        if (selectedScheduleItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Course Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course you want to duplicate.");
            alert.showAndWait();
            return;
        } else {
            gotoDuplicateScheduleDialog();
        }
    }


    // delete selected course
    @FXML
    public void showDeleteCourseDialog() throws IOException {

        ScheduleItem selectedScheduleItem = courseTableView.getSelectionModel().getSelectedItem();
        if (selectedScheduleItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Course Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course you want to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected course: \n" +
                selectedScheduleItem.getDepartment() + " " +
                selectedScheduleItem.getCourseID() + " " +
                selectedScheduleItem.getSection());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScheduleData.getInstance().deleteCourseItem(selectedScheduleItem);
            ScheduleData.getInstance().storeScheduleItems();
            courseTableView.getItems().setAll(ScheduleData.getInstance().getScheduleItems());
        }
    }

    // double click a course to edit
    public void handleDoubleClick(MouseEvent mouseEvent) throws IOException {

        ScheduleItem selectedScheduleItem = courseTableView.getSelectionModel().getSelectedItem();
        if (selectedScheduleItem != null
                && mouseEvent.getButton().equals(MouseButton.PRIMARY)
                && mouseEvent.getClickCount() == 2) {
            showEditCourseDialog();
        }
    }

    public ScheduleItem getSelectedScheduleItem() {

        return courseTableView.getSelectionModel().getSelectedItem();
    }
}
