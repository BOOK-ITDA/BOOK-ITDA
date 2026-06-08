package ui;

import java.sql.SQLException;
import java.util.Scanner;

import dao.UserDao;
import service.UserService;

public class MainUi {
    private final Scanner scanner = new Scanner(System.in); // 스캐너

    // 메인 화면
    public void showMainScreen() throws SQLException {
        while (true) {
            printMainMenu(); // 메뉴 출력
            System.out.print("메뉴 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("프로그램을 종료합니다. 이용해주셔서 감사합니다.");
                    System.exit(0);
                case 1:
                    System.out.println("회원가입 기능을 선택하셨습니다.");
                    UserService userService = new UserService(new UserDao());
                    SignupUi signupUi = new SignupUi(userService); // 회원가입 화면 호출
                    signupUi.showSignupScreen();
                    System.out.println("시작 화면으로 복귀했습니다.");
                    break;
                case 2:
                    System.out.println("회원 로그인 기능을 선택하셨습니다.");
                    int userId = new UserLoginUi().login(); //로그인
                    if (userId != -1) { //로그인 성공하면
                        new LibraryUi().showLibraryScreen();
                    } /*else {
                        showMainScreen(); //로그인 실패하면 지금 메뉴 다시 보여줘서 기능 선택하도록 함
                    } */
                    break;
                case 3:
                    System.out.println("사서 로그인 기능을 선택하셨습니다.");
                    boolean staffLoginResult = new StaffLoginUi().showStaffLoginScreen();
                    /*
                    if (!staffLoginResult) {
                        showMainScreen(); // 실패 시에만 메인 메뉴 복귀
                    }
                     */
                    break;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    break;
            }
        }
    }
    // 선택지 출력 함수
    public void printMainMenu(){
        System.out.println("구립 도서관 대출 관리 시스템 책잇다에 오신 것을 환영합니다.");
        System.out.println("아래의 기능들 중 원하시는 메뉴의 번호를 입력해주세요.");
        System.out.println("====================================================");
        System.out.println("[0] 프로그램 종료");
        System.out.println("[1] 회원 가입");
        System.out.println("[2] 회원 로그인");
        System.out.println("[3] 사서 로그인");
        System.out.println("====================================================");
    }
}
