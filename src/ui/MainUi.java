package ui;
import dao.UserDao;

import java.util.Scanner;

public class MainUi {
    private final Scanner scanner = new Scanner(System.in);
    // case문 내용은 삭제하고 해당 기능 넣으시면 됩니당
    public void showMainScreen(){
        printMainMenu();
        System.out.print("메뉴 선택 : ");
        int choice = scanner.nextInt();
        switch (choice){
            case 0:
                System.out.println("프로그램을 종료합니다. 이용해주셔서 감사합니다.");
                break;
            case 1:
                System.out.println("회원가입 기능을 선택하셨습니다.");
                break;
            case 2:
                System.out.println("회원 로그인 기능을 선택하셨습니다.");
                break;
            case 3:
                System.out.println("사서 로그인 기능을 선택하셨습니다.");
                break;
            default :
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                showMainScreen();
                break;


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
