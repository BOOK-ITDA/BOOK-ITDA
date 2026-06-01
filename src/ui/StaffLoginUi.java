package ui;

import dao.*;
import service.StaffService;
import java.util.Scanner;

public class StaffLoginUi {

    private final StaffService staffService = new StaffService(
            new UserDao(),
            new BranchTransferDao(),
            new SmartLibReqDao(),
            new ReservationRecordDao(),
            new OverdueRecordDao(),
            new StaffDao()
    );
    private final Scanner scanner = new Scanner(System.in);

    public void showStaffLoginScreen() {
        System.out.println("\n====================================================");
        System.out.println("                  사서 로그인");
        System.out.println("====================================================");
        System.out.print("관리자 비밀번호를 입력하세요 : ");
        String inputPassword = scanner.nextLine();

        boolean loginSuccess = staffService.staffLogin(inputPassword);

        if (loginSuccess) {
            System.out.println("로그인 성공! 사서 관리 메뉴로 이동합니다.");
            new StaffUi().showStaffScreen();
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
    }
}