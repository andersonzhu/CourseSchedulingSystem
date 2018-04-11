package schedule.data;

public class Course {

    private String department;
    private String courseID;
    private String courseName;

    public Course(String department, String courseID, String courseName) {
        this.department = department;
        this.courseID = courseID;
        this.courseName = courseName;
    }

    public String getDepartment() {
        return department;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String toString() {
        return getDepartment() + "\t" +
                getCourseID() + "\t" +
                getCourseName();
    }
}
