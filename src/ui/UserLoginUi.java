package ui;
import dao.UserDao;
import service.UserService;
import session.Session;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLoginUi {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService(new UserDao());

    public int login() throws SQLException {
        System.out.println("로그인을 취소하려면 0을 입력하세요.");
        while (true) { //아이디나 비밀번호 틀렸을 때 계속 입력하게 해주기 위해서 while문
            System.out.print("회원 번호 입력: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            if (id == 0) {
                System.out.println("로그인을 취소합니다.");
                return -1;
            }

            System.out.print("비밀번호 입력: ");
            String pw = scanner.nextLine();

            int userId = userService.login(id, pw);
            if (userId != -1) { //로그인 성공하면
                Session.login(userId); //세션에 아이디 저장
                System.out.println("로그인 성공! 환영합니다.");
                return userId;
            }
            System.out.println("회원 번호 또는 비밀번호가 올바르지 않습니다. 다시 시도해주세요.");
        }
    }


}
