package ui;

import dao.CollectionDao;
import dao.OverdueRecordDao;
import dao.SmartLibReqDao;
import dao.SmartLibraryDao;
import database.DatabaseConnector;
import dto.SmartLibraryDto;
import service.SmartLibReqService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SmartLibraryUi {

    private final SmartLibReqService smartLibReqService = new SmartLibReqService(
            new SmartLibReqDao(),
            new SmartLibraryDao(),
            new OverdueRecordDao(),
            new CollectionDao()
    );
    private final Scanner scanner = new Scanner(System.in);

    // SearchUi의 case 3에서 호출
    // userId      : 로그인한 회원 ID (현재는 임시값, 로그인 구현 후 SessionManager로 교체)
    // bookId      : 검색에서 선택한 도서 ID
    // libraryId   : 해당 도서의 소장 도서관 ID
    public void showSmartLibraryScreen(int userId, int bookId, int libraryId) {

        System.out.println("\n===== 신청 가능한 스마트도서관 목록 =====");

        List<SmartLibraryDto> smartLibList;

        try (Connection conn = DatabaseConnector.getConnection()) {
            smartLibList = smartLibReqService.getAvailableSmartLibList(conn);
        } catch (SQLException e) {
            System.out.println("[오류] 스마트도서관 목록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
            return;
        }

        // 신청 가능한 스마트도서관이 없는 경우
        if (smartLibList.isEmpty()) {
            System.out.println("현재 신청 가능한 스마트도서관이 없습니다.");
            return;
        }

        // 목록 출력
        System.out.printf("%-10s %-20s %-30s %-10s %-10s%n",
                "도서관ID", "도서관명", "주소", "최대권수", "현재권수");
        System.out.println("-".repeat(75));
        for (SmartLibraryDto lib : smartLibList) {
            System.out.printf("%-10d %-20s %-30s %-10d %-10d%n",
                    lib.getSmart_lib_id(),
                    lib.getName(),
                    lib.getAddress(),
                    lib.getBook_capacity(),
                    lib.getBook_count());
        }

        System.out.print("\n수령할 스마트도서관ID를 입력하세요 (취소: 0) : ");
        int smartLibId;
        try {
            smartLibId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("올바른 번호를 입력해주세요.");
            showSmartLibraryScreen(userId, bookId, libraryId);
            return;
        }

        // 취소
        if (smartLibId == 0) {
            System.out.println("이전 화면으로 돌아갑니다.");
            return;
        }

        // 입력한 ID가 목록에 있는지 검사
        boolean valid = false;
        for (SmartLibraryDto lib : smartLibList) {
            if (lib.getSmart_lib_id() == smartLibId) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            System.out.println("없는 스마트도서관ID입니다. 다시 선택해 주세요.");
            showSmartLibraryScreen(userId, bookId, libraryId);
            return;
        }

        // 신청 처리
        try {
            smartLibReqService.requestSmartLibReq(userId, bookId, libraryId, smartLibId);
        } catch (SQLException e) {
            System.out.println("[오류] 스마트도서관 대출 신청 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
