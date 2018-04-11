package schedule;

public class UserTest {
    public static void main(String[] args) {
        User user = User.getInstance();
        System.out.println(user.getSubjects("admin"));

    }
}
