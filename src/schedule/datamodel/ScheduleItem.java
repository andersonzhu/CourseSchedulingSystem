package schedule.datamodel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.MINUTES;

public class ScheduleItem {

    private String department, courseID, type, section, courseName, status, instructor, campus, room, days;
    private int credits, max;
    private LocalTime startTime, endTime;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    // empty constructor
    public ScheduleItem() {
    }

    /**
     * Constructor with parameters
     *
     * @param department: four uppercase letters. Ex. MATH
     * @param courseID
     * @param type
     * @param section
     * @param courseName: capitalize first letter of each words. ex. College Algebra
     * @param status
     * @param instructor
     * @param campus
     * @param room
     * @param days
     * @param credits
     * @param max
     * @param startTime
     * @param endTime
     */

    public ScheduleItem(String department, String courseID,
                        String type, String section,
                        String courseName, String status,
                        String instructor, String campus,
                        String room, String days,
                        int credits, int max,
                        LocalTime startTime, LocalTime endTime) {
        this.department = department;
        this.courseID = courseID;
        this.type = type;
        this.section = section;
        this.courseName = courseName;
        this.status = status;
        this.instructor = instructor;
        this.campus = campus;
        this.room = room;
        this.days = days;
        this.credits = credits;
        this.max = max;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // get department
    public String getDepartment() {
        return department.toUpperCase();
    }

    public void setDepartment(String department) {
        this.department = department.toUpperCase();
    }

    // get courseID, 1314 1342 0123
    public String getCourseID() {
        return courseID.toUpperCase();
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID.toUpperCase();
    }

    // LEC, LAB...
    public String getType() {
        return type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    // A01, A63 ...
    public String getSection() {
        return section.toUpperCase();
    }

    public void setSection(String section) {
        this.section = section.toUpperCase();
    }

    // college algebra --> College Algebra
    private String capitalizeFirstLetter(String word) {
        String wordWithFirstCapitalization = "";
        String[] wordPieces = word.split(" ");
        for (String s : wordPieces) {
            wordWithFirstCapitalization += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            wordWithFirstCapitalization += " ";
        }
        return wordWithFirstCapitalization.trim();
    }

    public String getCourseName() {
        return capitalizeFirstLetter(courseName);
    }

    public void setCourseName(String courseName) {
        this.courseName = capitalizeFirstLetter(courseName);
    }

    // open or close
    public String getStatus() {
        return capitalizeFirstLetter(status);
    }

    public void setStatus(String status) {
        this.status = capitalizeFirstLetter(status);
    }

    public String getInstructor() {
        return capitalizeFirstLetter(instructor);
    }

    public void setInstructor(String instructor) {
        this.instructor = capitalizeFirstLetter(instructor);
    }

    public String getCampus() {
        return campus.toUpperCase();
    }

    public void setCampus(String campus) {
        this.campus = campus.toUpperCase();
    }


    // LA208
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    // can only contain MTWRF
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    // 0 - 6
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    // max enrollment
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // course start time
    public String getStartTime() {
        return dateTimeFormatter.format(startTime);
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    // course end time
    public String getEndTime() {
        return dateTimeFormatter.format(endTime);
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;

    }

    // is the courseItem a valid one
    public boolean isValid() {

        HashMap<LocalTime, LocalTime> timeBlockOptionOne = new HashMap<>();
        timeBlockOptionOne.put(LocalTime.of(8, 0), LocalTime.of(9, 15));
        timeBlockOptionOne.put(LocalTime.of(9, 30), LocalTime.of(10, 45));
        timeBlockOptionOne.put(LocalTime.of(11, 0), LocalTime.of(12, 15));
        timeBlockOptionOne.put(LocalTime.of(13, 0), LocalTime.of(14, 15));
        timeBlockOptionOne.put(LocalTime.of(14, 30), LocalTime.of(15, 45));
        timeBlockOptionOne.put(LocalTime.of(18, 0), LocalTime.of(19, 15));
        timeBlockOptionOne.put(LocalTime.of(19, 30), LocalTime.of(20, 45));

        HashMap<LocalTime, LocalTime> timeBlockOptionTwo = new HashMap<>();
        timeBlockOptionTwo.put(LocalTime.of(8, 0), LocalTime.of(10, 45));
        timeBlockOptionTwo.put(LocalTime.of(11, 0), LocalTime.of(12, 15));
        timeBlockOptionTwo.put(LocalTime.of(13, 0), LocalTime.of(15, 45));
        timeBlockOptionTwo.put(LocalTime.of(18, 0), LocalTime.of(20, 45));

        // courseID should start with 0 or 1 or 2.
        if (courseID == null || courseID.equals("") || Integer.parseInt(courseID.substring(0, 1)) > 2) {
            return false;
        }

        // section number should be two-digit
        if (section == null || section.equals("") || section.replaceAll("[^0-9]", "").length() > 2) {
            return false;
        }

        // if sectionType is chosen as NT campus and room must be Internet
        if (section == null || section.equals("") ) {
            return false;
        } else {
            if(section.contains("NT") && (!campus.equals("INTERNET") || !room.equals("INTERNET"))){
                return false;
            }
        }

        if (instructor == null || instructor.equals("")) {
            return false;
        }

        // credits equals second digit of courseID
        if (Integer.parseInt(courseID.substring(1, 2)) != credits) {
            return false;
        }

        // max cannot be 0 or negative
        if (max <= 0) {
            return false;
        }

        // days
        if (days.length() < 1 || days.equals("")) {
            return false;
        }

        // start time must be later than 8:00 am and less than 8:00 pm?
        if (startTime.compareTo(LocalTime.of(8, 0)) < 0) {
            return false;
        }

        if (endTime.compareTo(LocalTime.of(22, 0)) > 0) {
            return false;
        }

        if (startTime.isAfter(endTime)) {
            return false;
        }

        if (startTime.until(endTime, MINUTES) < 50) {
            return false;
        }

        // for main campus only
        if (campus == "MAIN") {
            if (!timeBlockOptionOne.containsKey(startTime) || !timeBlockOptionTwo.containsKey(startTime)) {
                return false;
            } else {

            }
        }

        return true;
    }

    public String errorMessageOfUserInputs() {

        ArrayList<String> messages = new ArrayList<>();

        // first digit of courseID should be less than or equal to 2
        // courseID should be less than 3000
        // courseID should start with 0 or 1 or 2.
        if (courseID == null || courseID.equals("")) {
            messages.add("\u2022 CourseID cannot be blank.\n");
        } else {
            String courseIDFirstDigit = courseID.substring(0, 1);

            if (Integer.parseInt(courseIDFirstDigit) > 2) {
                messages.add("\u2022 CourseID cannot start with " + courseIDFirstDigit + ".\n");
            }

            if (courseID.length() < 4) {
                messages.add("\u2022 CourseID should be 4 digits. \n");
            }
        }

        // section number should be less than 99
        if (section == null || section.equals("")) {
            messages.add("\u2022 Section cannot be blank.\n");
        } else {
            // if sectionType is chosen as NT campus and room must be Internet
            if (section.contains("NT") && !campus.equals("INTERNET")) {
                messages.add("\u2022 The Campus of an Internet course should be INTERNET.\n");
            }

            if (section.contains("NT") && !room.equals("INTERNET")) {
                messages.add("\u2022 The Room of an Internet course should be INTERNET.\n");
            }
        }

        if (section != null && section.length() >= 3) {
            if (section.length() >= 3 && Integer.parseInt(section.replaceAll("[^0-9]", "")) > 99) {
                messages.add("\u2022 Section number should be two digits only.\n");
            } else if (section.length() == 1) {
                messages.add("\u2022 Section number cannot be blank.\n");
            } else if (section != null && section.length() < 3) {
                messages.add("\u2022 Section number should be 2 digits.\n");
            }
        }

        // coursename
        if (courseName == null || courseName.equals("")) {
            messages.add("\u2022 Course Name cannot be blank.\n");
        }


        // instructor
        if (instructor == null || instructor.equals("")) {
            messages.add("\u2022 Instructor cannot be blank.\n");
        }

        // credits
        // credits equals second digit of courseID
        if (courseID != null && courseID.length() == 4) {
            if (credits != Integer.parseInt(courseID.substring(1, 2))) {
                messages.add("\u2022 Credit should match second digit of courseID.\n");
            }
        }

        if (credits == 0) {
            messages.add("\u2022 Credits cannot be blank. \n");
        }

        // days
        if (days.length() < 1 || days.equals("")) {
            messages.add("\u2022 Days cannot be blank. \n");
        }

        // max
        if (max == 0) {
            messages.add("\u2022 Max cannot be blank. \n");
        }

        // room
        if (room.equals("INTERNET") || room.equals("CLINICAL") || room.equals("HOSPITAL")) {
            String roomNumber = room.replaceAll("[^0-9]", "");
            if (roomNumber.length() < 1) {
                messages.add("\u2022 Room number cannot be blank. \n");
            }
        }

        // for internet course, we don't care about the start and end time

        // start time must be later than 8:00 am and less than 8:00 pm?
        if (startTime.compareTo(LocalTime.of(7, 0)) < 0) {

            messages.add("\u2022 Start time should after 8:00 AM.\n");
        }

        if (endTime.compareTo(LocalTime.of(22, 0)) > 0) {

            messages.add("\u2022 End time should before 10:00 PM.\n");
        }

        if (startTime.isAfter(endTime)) {
            messages.add("\u2022 End time should be after start time.\n");
        }

        if (startTime.until(endTime, MINUTES) < 40) {
            messages.add("\u2022 Course meeting time is too short.\n");
        }

        String result = "";
        for (String message : messages) {
            result += message;
        }

        return result;
    }


    // is this course a duplicate with that course
    private boolean isDuplicateWith(ScheduleItem that) {
        if (this.getDepartment().equals(that.getDepartment()) &&
                this.getCourseID().equals(that.getCourseID()) &&
                this.getSection().equals(that.getSection())) {
            return true;
        }
        return false;
    }

    // is this course a duplicate in the course items
    public boolean isUnique() {

        for (ScheduleItem scheduleItem : ScheduleData.getInstance().getScheduleItems()) {
            if (this.isDuplicateWith(scheduleItem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return getDepartment() + "\t" +
                getCourseID() + "\t" +
                getType() + "\t" +
                getSection() + "\t" +
                getCourseName() + "\t" +
                getStatus() + "\t" +
                getInstructor() + "\t" +
                getCampus() + "\t" +
                getRoom() + "\t" +
                getDays() + "\t" +
                getCredits() + "\t" +
                getMax() + "\t" +
                getStartTime() + "\t" +
                getEndTime();
    }

}
