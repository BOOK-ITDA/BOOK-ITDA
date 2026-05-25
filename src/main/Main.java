package main;

import ui.StaffUI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== BOOK-ITDA ===");
            System.out.println("[1] 회원 가입");
            System.out.println("[2] 회원 로그인");
            System.out.println("[3] 직원 로그인");
            System.out.println("[0] 종료");
            System.out.print("선택: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> System.out.println("회원 기능입니다.");  //기능 구현시 변경
                case "2" -> System.out.println("도서 검색 기능입니다.");  //기능 구현시 변경
                case "3" -> {
                    StaffUI staffUI = new StaffUI(sc);
                    staffUI.login();
                }
                case "0" -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}