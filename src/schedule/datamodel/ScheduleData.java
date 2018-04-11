package schedule.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import schedule.LoginController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScheduleData {

    // simpleton design
    private static ScheduleData instance = new ScheduleData();

    // file to save all course items
//    private static String filename = "Course_Schedule_" + usernameText + ".txt";

    // formatter to format course start and end time
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private ObservableList<ScheduleItem> scheduleItems;

    // empty constructor
    public ScheduleData() { }

    // simpleton design
    public static ScheduleData getInstance() {
        return instance;
    }

    // get all course items from instance
    public List<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    // add new course item to the List
    public void addCourseItem(ScheduleItem item) {
        scheduleItems.add(item);
    }

    // delete selected course item
    public void deleteCourseItem(ScheduleItem item) { scheduleItems.remove(item); }

    // load all course items from local
    public ObservableList<ScheduleItem> loadScheduleItems() throws IOException {

        String usernameText = LoginController.getInstance().getUsername();
        String defaultFileName = "Course_Schedule_" + usernameText + ".txt";
        File file = new File(defaultFileName);
        List<File> listOfFiles = new ArrayList<>();
        listOfFiles.add(file);
        return loadCourseItemsFromMultipleFiles(listOfFiles);
    } // close loadScheduleItems


    public ObservableList<ScheduleItem> loadCourseItemsFromMultipleFiles(List<File> files) throws IOException {

        scheduleItems = FXCollections.observableArrayList();
        for (File file : files) {
//            Path path = Paths.get(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String input;

            try {

                while ((input = br.readLine()) != null) {
                    String[] itemPieces = input.split("\t");

                    if (itemPieces.length != 14) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("File format is incorrect. It should be a tab delimited file.");
                        alert.showAndWait();
                    } else {
                        String department = itemPieces[0];
                        String courseID = itemPieces[1];
                        String type = itemPieces[2];
                        String section = itemPieces[3];
                        String courseName = itemPieces[4];
                        String status = itemPieces[5];
                        String instructor = itemPieces[6];
                        String campus = itemPieces[7];
                        String room = itemPieces[8];
                        String days = itemPieces[9];
                        int credits = Integer.parseInt(itemPieces[10]);
                        int max = Integer.parseInt(itemPieces[11]);
                        LocalTime startTime = LocalTime.parse(itemPieces[12], dateTimeFormatter);
                        LocalTime endTime = LocalTime.parse(itemPieces[13], dateTimeFormatter);

                        ScheduleItem scheduleItem = new ScheduleItem(department, courseID,
                                type, section,
                                courseName, status,
                                instructor, campus,
                                room, days,
                                credits, max,
                                startTime, endTime);
                        scheduleItems.add(scheduleItem);
                    }
                }

            } finally {
                if (br != null) {
                    br.close();
                }
            }
        }
        return scheduleItems;
    } // close loadCourseItemsFromMultipleFiles

    // save to local if any change were made for course items
    public void storeScheduleItems() throws IOException {

        String usernameText = LoginController.getInstance().getUsername();
        Path path = Paths.get("Course_Schedule_" + usernameText + ".txt");
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<ScheduleItem> iterator = scheduleItems.iterator();
            while (iterator.hasNext()) {
                ScheduleItem item = iterator.next();
                bw.write(item.toString());
                bw.newLine();
            }

        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    } // close storeScheduleItems

}
