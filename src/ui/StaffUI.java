package ui;

import service.StaffService;
import java.util.Scanner;

public class StaffUI {

    private final StaffService staffService = new StaffService();
    private final Scanner sc;

    public StaffUI(Scanner sc) {
        this.sc = sc;
    }

    // 직원 로그인
    public boolean login() {
        System.out.print("\n직원 비밀번호 입력: ");
        String password = sc.nextLine().trim();

        if (staffService.verifyPassword(password)) {
            System.out.println("직원으로 로그인되었습니다.");
            return true;
        } else {
            System.out.println("비밀번호가 틀렸습니다.");
            return false;
        }
    }
}