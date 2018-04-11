package schedule.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CourseList {

    private static CourseList ourInstance = new CourseList();

    private ObservableList<Course> courseList;

    public static CourseList getInstance() {
        return ourInstance;
    }

    private CourseList() {
    }

    public ObservableList<Course> getCourseList() throws IOException {

        courseList = FXCollections.observableArrayList();

        InputStream in = this.getClass().getResourceAsStream("all_courses.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] itemPieces = line.split("\t");

                String department = itemPieces[0];
                String courseID = itemPieces[1];
                String courseName = itemPieces[2];

                Course course = new Course(department, courseID, courseName);

                courseList.add(course);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return courseList;
    }

}
