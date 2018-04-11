package schedule;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class User {

    private static final String pwd = "grayson";
    private static User instance;

    private static final HashMap<String, String[]> map = new HashMap<>();

    // private constructor
    private User() { }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }


    static {


        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("user_subjects.txt")));

            ArrayList<String> allSubjects = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] itemPieces = line.split(", ");

                String department = itemPieces[0].trim();
                String[] subjects = Arrays.copyOfRange(itemPieces, 1, itemPieces.length-1);

                Collections.addAll(allSubjects, subjects); // for admin

                map.put(department, subjects);
            }
            map.put("admin", allSubjects.toArray(new String[0]));

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Not Found");
            alert.setContentText("File department_subjects.txt does not exist.");
            alert.showAndWait();
        }
    }

    // get all subjects from specific user
    public String[] getSubjects(String username) {
        return map.get(username);
    }


//    public List<String> getAllDepartments() {
//        return new ArrayList<>(map.keySet());
//    }

    public static boolean validate(String username, String password) {

        return map.containsKey(username) && password.equals(pwd);
    }

}