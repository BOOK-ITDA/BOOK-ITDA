package ui;

import dao.BranchTransferDao;
import dao.CollectionDao;
import dao.LibraryDao;
import dao.OverdueRecordDao;
import dto.LibraryDto;
import service.BranchTransferService;
import service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BTRUi {

    private final BranchTransferService branchTransferService = new BranchTransferService(
            new BranchTransferDao(),
            new OverdueRecordDao(),
            new CollectionDao()
    );
    private final LibraryService libraryService = new LibraryService(new LibraryDao());
    private final Scanner scanner = new Scanner(System.in);
    //전체 로직: (도서 선택 -> 분관신청 선택) -> 수령 원하는 도서관 목록 바로 출력 -> 도서관 ID 입력 -> 연체 여부 확인 -> 분관신청 삽입 -> 소장 테이블 예약중으로 변경
    public void showBranchTransferScreen(int userId, int bookId, int holdingLibId) {
        System.out.println("\n==================== 수령 가능 도서관 목록 ===================="); //바로 도서관 목록 출력
        try {
            List<LibraryDto> libList = libraryService.getLibList();
            if (libList.isEmpty()) {
                System.out.println("조회 가능한 도서관이 없습니다.");
                return;
            }
            System.out.printf("%-8s %-25s %-50s%n", "도서관ID", "도서관명", "주소");
            System.out.println("-".repeat(60));
            for (LibraryDto lib : libList) {
                System.out.printf("%-8d %-15s %-30s%n",
                        lib.getLibrary_id(),
                        lib.getName(),
                        lib.getAddress());
            }

            System.out.print("\n수령할 도서관ID를 입력하세요 (취소: 0) : "); //수령 원하는 도서관 아이디 입력
            int pickupLibId = scanner.nextInt();
            scanner.nextLine();

            if (pickupLibId == 0) {
                System.out.println("이전 화면으로 돌아갑니다.");
                return;
            }

            // 입력한 아이디가 목록에 있는지 확인
            boolean valid = false;
            for (LibraryDto lib : libList) {
                if (lib.getLibrary_id() == pickupLibId) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("없는 도서관ID입니다. 다시 선택해 주세요.");
                showBranchTransferScreen(userId, bookId, holdingLibId);
                return;
            }
            //입력한 아이디가 존재하면 바로 서비스 호출 및 다음 단계 실행(연체 여부 확인 -> 분관신청 삽입 -> 소장 테이블 예약중으로 변경)
            branchTransferService.requestBranchTransfer(userId, bookId, holdingLibId, pickupLibId);

        } catch (SQLException e) {
            System.out.println("[오류] 분관 대출 신청 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}