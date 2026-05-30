package ui;

import dto.*;
import service.StaffService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StaffUi {

    private final StaffService staffService = new StaffService();
    private final Scanner scanner = new Scanner(System.in);

    public void showStaffScreen() {
        System.out.println("\n=============================");
        System.out.println("       사서 관리 메뉴");
        System.out.println("=============================");
        System.out.println("[1] 전체 회원 조회");
        System.out.println("[2] 분관 대출 신청 관리");
        System.out.println("[3] 스마트 도서관 신청 관리");
        System.out.println("[4] 연체 기록 관리");
        System.out.println("[0] 종료");
        System.out.println("=============================");
        System.out.print("기능 선택 : ");

        int select = scanner.nextInt();
        scanner.nextLine();

        switch (select) {
            case 1: handleAllUsers();       break;
            case 2: handleBranchTransfer(); break;
            case 3: handleSmartLibReq();    break;
            case 4: handleOverdue();        break;
            case 0: System.exit(0);         break;
            default:
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                showStaffScreen();
                break;
        }
    }

    // ─────────────────────────────────────────
    // 기능별 핸들러
    // ─────────────────────────────────────────
    private void handleAllUsers() {
        System.out.println("\n===== 전체 회원 목록 =====");
        try {
            List<UserDto> users = staffService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("등록된 회원이 없습니다.");
            } else {
                System.out.printf("%-8s %-10s %-12s %-15s %-8s%n",
                        "회원ID", "이름", "생년월일", "전화번호", "대출수");
                System.out.println("-".repeat(58));
                for (UserDto u : users) {
                    System.out.printf("%-8d %-10s %-12s %-15s %-8d%n",
                            u.getUser_id(),
                            u.getName(),
                            u.getBirthdate(),
                            u.getPhone_number(),
                            u.getLoan_count());
                }
            }
        } catch (SQLException e) {
            System.out.println("[오류] 회원 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        showStaffScreen();
    }

    private void handleBranchTransfer() {
        // 진입하자마자 목록 바로 출력
        System.out.println("\n===== 분관 대출 신청 목록 =====");
        try {
            List<BranchTransferDto> list = staffService.getBranchTransfer();
            if (list.isEmpty()) {
                System.out.println("분관 대출 신청 내역이 없습니다.");
                showStaffScreen();
                return;
            }
            System.out.printf("%-8s %-8s %-15s %-12s %-12s %-12s%n",
                    "신청ID", "회원ID", "도서명", "소장도서관", "수령도서관", "처리상태");
            System.out.println("-".repeat(72));
            for (BranchTransferDto b : list) {
                System.out.printf("%-8d %-8d %-15s %-12s %-12s %-12s%n",
                        b.getTransf_req_id(),
                        b.getUser_id(),
                        b.getBook_name(),
                        b.getHolding_lib_name(),
                        b.getPickup_lib_name(),
                        b.getStatus());
            }

            System.out.println("====================================================");
            System.out.println("[1] 처리 상태 변경 (PROCESSING → AVAILABLE)");
            System.out.println("[0] 뒤로가기");
            System.out.println("====================================================");
            System.out.print("기능 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("상태를 변경할 신청ID를 입력하세요 : ");
                    int transferId = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        staffService.updateBranchTransferStatus(transferId);
                        System.out.println("분관 대출 신청 [ID: " + transferId + "] 상태가 변경되었습니다.");
                    } catch (SQLException e) {
                        System.out.println("[오류] 상태 변경 중 오류가 발생했습니다.");
                        e.printStackTrace();
                    }
                    handleBranchTransfer();
                    break;
                case 0:
                    showStaffScreen();
                    break;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    handleBranchTransfer();
                    break;
            }

        } catch (SQLException e) {
            System.out.println("[오류] 분관 대출 신청 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    private void handleSmartLibReq() {
        // 진입하자마자 목록 바로 출력
        System.out.println("\n===== 스마트 도서관 신청 목록 =====");
        try {
            List<SmartLibReqDto> list = staffService.getSmartReq();
            if (list.isEmpty()) {
                System.out.println("스마트 도서관 신청 내역이 없습니다.");
                showStaffScreen();
                return;
            }
            System.out.printf("%-8s %-8s %-15s %-12s %-15s %-12s%n",
                    "신청ID", "회원ID", "도서명", "소장도서관", "스마트도서관", "처리상태");
            System.out.println("-".repeat(75));
            for (SmartLibReqDto s : list) {
                System.out.printf("%-8d %-8d %-15s %-12s %-15s %-12s%n",
                        s.getSmt_req_id(),
                        s.getUser_id(),
                        s.getBook_name(),
                        s.getLibrary_name(),
                        s.getSmart_lib_name(),
                        s.getStatus());
            }

            System.out.println("====================================================");
            System.out.println("[1] 처리 상태 변경 (PROCESSING → AVAILABLE)");
            System.out.println("[0] 뒤로가기");
            System.out.println("====================================================");
            System.out.print("기능 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("상태를 변경할 신청ID를 입력하세요 : ");
                    int smartReqId = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        staffService.updateSmartLibReqStatus(smartReqId);
                        System.out.println("스마트 도서관 신청 [ID: " + smartReqId + "] 상태가 변경되었습니다.");
                    } catch (SQLException e) {
                        System.out.println("[오류] 상태 변경 중 오류가 발생했습니다.");
                        e.printStackTrace();
                    }
                    handleSmartLibReq();
                    break;
                case 0:
                    showStaffScreen();
                    break;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    handleSmartLibReq();
                    break;
            }

        } catch (SQLException e) {
            System.out.println("[오류] 스마트 도서관 신청 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    private void handleOverdue() {
        // 진입하자마자 목록 바로 출력
        System.out.println("\n===== 연체 기록 목록 =====");
        try {
            List<OverdueRecordDto> list = staffService.getOverdue();
            if (list.isEmpty()) {
                System.out.println("연체 기록이 없습니다.");
                showStaffScreen();
                return;
            }
            System.out.printf("%-10s %-10s %-15s %-12s %-10s %-10s%n",
                    "연체기록ID", "회원명", "도서명", "예정반납일", "연체료(원)", "납부여부");
            System.out.println("-".repeat(70));
            for (OverdueRecordDto o : list) {
                System.out.printf("%-10d %-10s %-15s %-12s %-10d %-10s%n",
                        o.getOverdue_id(),
                        o.getUser_name(),
                        o.getBook_name(),
                        o.getDue_date(),
                        o.getFine_amount(),
                        o.isIs_paid() ? "납부완료" : "미납");
            }

            System.out.println("====================================================");
            System.out.println("[1] 연체료 납부 처리");
            System.out.println("[0] 뒤로가기");
            System.out.println("====================================================");
            System.out.print("기능 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("납부 처리할 연체기록ID를 입력하세요 : ");
                    int overdueId = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        staffService.updateOverdueStatus(overdueId);
                        System.out.println("연체 기록 [ID: " + overdueId + "] 납부 처리가 완료되었습니다.");
                    } catch (SQLException e) {
                        System.out.println("[오류] 납부 처리 중 오류가 발생했습니다.");
                        e.printStackTrace();
                    }
                    handleOverdue();
                    break;
                case 0:
                    showStaffScreen();
                    break;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    handleOverdue();
                    break;
            }

        } catch (SQLException e) {
            System.out.println("[오류] 연체 기록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}