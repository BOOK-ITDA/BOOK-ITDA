package ui;

import dao.BranchTransferDao;
import dao.LoanRecordDao;
import dao.OverdueRecordDao;
import dao.ReservationRecordDao;
import dao.SmartLibReqDao;
import database.DatabaseConnector;
import dto.BranchTransferDto;
import dto.LoanRecordDto;
import dto.OverdueRecordDto;
import dto.ReservationRecordDto;
import dto.SmartLibReqDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DashBoardUi {

    private final int userId;
    private final Scanner scanner = new Scanner(System.in);

    private final LoanRecordDao loanRecordDao = new LoanRecordDao();
    private final OverdueRecordDao overdueRecordDao = new OverdueRecordDao();
    private final ReservationRecordDao reservationRecordDao = new ReservationRecordDao();
    private final SmartLibReqDao smartLibReqDao = new SmartLibReqDao();
    private final BranchTransferDao branchTransferDao = new BranchTransferDao();

    // userId: 로그인한 회원의 ID를 생성자로 받음
    public DashBoardUi(int userId) {
        this.userId = userId;
    }

    public void showDashBoardScreen() {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("         대시보드");
            System.out.println("=============================");
            System.out.println("[1] 대출 중인 도서 조회");
            System.out.println("[2] 연체 여부 확인");
            System.out.println("[3] 일반 예약 목록 조회");
            System.out.println("[4] 스마트 도서관 대출 신청 목록 조회");
            System.out.println("[5] 분관 대출 신청 목록 조회");
            System.out.println("[0] 뒤로가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택 : ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: handleActiveLoans(); waitForEnter(); break;
                    case 2: handleOverdueRecords(); waitForEnter(); break;
                    case 3: handleReservations(); waitForEnter(); break;
                    case 4: handleSmartLibReqs(); waitForEnter(); break;
                    case 5: handleBranchTransfers(); waitForEnter(); break;
                    case 0:
                        System.out.println("도서관 메뉴로 돌아갑니다.");
                        return; // LibraryUi로 복귀
                    default:
                        System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("[오류] 숫자만 입력해 주세요.");
                scanner.nextLine();
            }
        }
    }

    // ─────────────────────────────────────────
    // [1] 대출 중인 도서 조회
    // LoanRecordDao.findActiveByUserId() 사용
    // return_date IS NULL인 기록만 조회
    // ─────────────────────────────────────────
    private void handleActiveLoans() {
        System.out.println("\n===== 대출 중인 도서 목록 =====");
        try (Connection conn = DatabaseConnector.getConnection()) {
            List<LoanRecordDto> list = loanRecordDao.findActiveByUserId(conn, userId);

            if (list.isEmpty()) {
                System.out.println("현재 대출 중인 도서가 없습니다.");
            } else {
                System.out.printf("%-8s %-20s %-15s %-12s %-12s %-8s%n",
                        "대출ID", "도서명", "도서관", "대출일", "반납예정일", "연장횟수");
                System.out.println("-".repeat(78));
                for (LoanRecordDto loan : list) {
                    System.out.printf("%-8d %-20s %-15s %-12s %-12s %-8d%n",
                            loan.getLoan_id(),
                            loan.getBook_name(),
                            loan.getLibrary_name(),
                            loan.getLoan_date(),
                            loan.getDue_date(),
                            loan.getExtension_count());
                }
            }
        } catch (SQLException e) {
            System.out.println("[오류] 대출 목록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────
    // [2] 연체 여부 확인
    // OverdueRecordDao.findAllByUserId() 사용
    // 납부 완료 포함 전체 연체 이력 조회
    // ─────────────────────────────────────────
    private void handleOverdueRecords() {
        System.out.println("\n===== 연체 내역 =====");
        try (Connection conn = DatabaseConnector.getConnection()) {
            List<OverdueRecordDto> list = overdueRecordDao.findAllByUserId(conn, userId);

            if (list.isEmpty()) {
                System.out.println("연체 내역이 없습니다.");
            } else {
                System.out.printf("%-10s %-20s %-12s %-12s %-10s%n",
                        "연체기록ID", "도서명", "대출일", "반납예정일", "연체료(원)");
                System.out.println("-".repeat(65));
                for (OverdueRecordDto od : list) {
                    System.out.printf("%-10d %-20s %-12s %-12s %-10d%n",
                            od.getOverdue_id(),
                            od.getBook_name(),
                            od.getLoan_date(),
                            od.getDue_date(),
                            od.getFine_amount());
                }
            }
        } catch (SQLException e) {
            System.out.println("[오류] 연체 내역 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────
    // [3] 일반 예약 목록 조회
    // ReservationRecordDao.findByUserId() 사용
    // ─────────────────────────────────────────
    private void handleReservations() {
        System.out.println("\n===== 일반 예약 목록 =====");
        try (Connection conn = DatabaseConnector.getConnection()) {
            List<ReservationRecordDto> list = reservationRecordDao.findByUserId(conn, userId);

            if (list.isEmpty()) {
                System.out.println("예약 내역이 없습니다.");
            } else {
                System.out.printf("%-8s %-20s %-15s %-12s %-12s%n",
                        "예약ID", "도서명", "도서관", "예약일", "처리상태");
                System.out.println("-".repeat(68));
                for (ReservationRecordDto r : list) {
                    System.out.printf("%-8d %-20s %-15s %-12s %-12s%n",
                            r.getReserve_id(),
                            r.getBook_name(),
                            r.getLibrary_name(),
                            r.getReserve_date(),
                            r.getStatus());
                }
            }
        } catch (SQLException e) {
            System.out.println("[오류] 예약 목록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────
    // [4] 스마트 도서관 대출 신청 목록 조회
    // SmartLibReqDao.findByUserId() 사용
    // ─────────────────────────────────────────
    private void handleSmartLibReqs() {
        System.out.println("\n===== 스마트 도서관 신청 목록 =====");
        try (Connection conn = DatabaseConnector.getConnection()) {
            List<SmartLibReqDto> list = smartLibReqDao.findByUserId(conn, userId);

            if (list.isEmpty()) {
                System.out.println("스마트 도서관 신청 내역이 없습니다.");
            } else {
                System.out.printf("%-8s %-20s %-15s %-12s%n",
                        "신청ID", "도서명", "스마트도서관", "처리상태");
                System.out.println("-".repeat(58));
                for (SmartLibReqDto s : list) {
                    System.out.printf("%-8d %-20s %-15s %-12s%n",
                            s.getSmt_req_id(),
                            s.getBook_name(),
                            s.getSmart_lib_name(),
                            s.getStatus());
                }
            }
        } catch (SQLException e) {
            System.out.println("[오류] 스마트 도서관 신청 목록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────
    // [5] 분관 대출 신청 목록 조회
    // ─────────────────────────────────────────
    private void handleBranchTransfers() {
        new ViewBTRUi().showBranchTransferRecord(userId);
    }

    private void waitForEnter() {
        System.out.print("\n메뉴로 돌아가려면 엔터(Enter) 키를 누르세요.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
