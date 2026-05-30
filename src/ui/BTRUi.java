package ui;

import dto.LibraryDto;
import service.BranchTransferService;
import service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BTRUi {

    private final BranchTransferService branchTransferService = new BranchTransferService();
    private final LibraryService libraryService = new LibraryService();
    private final Scanner scanner = new Scanner(System.in);

    public void showBranchTransferScreen(int userId, int bookId, int holdingLibId) {
        System.out.println("\n=============================");
        System.out.println("     분관 대출 신청");
        System.out.println("=============================");
        System.out.println("[1] 수령 도서관 목록 보기");
        System.out.println("[0] 뒤로가기");
        System.out.println("=============================");
        System.out.print("기능 선택 : ");

        int select = scanner.nextInt();
        scanner.nextLine();

        switch (select) {
            case 1:
                handleBranchTransferRequest(userId, bookId, holdingLibId);
                break;
            case 0:
                System.out.println("이전 화면으로 돌아갑니다.");
                return;
            default:
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                showBranchTransferScreen(userId, bookId, holdingLibId);
                break;
        }
    }

    private void handleBranchTransferRequest(int userId, int bookId, int holdingLibId) {
        // 도서관 목록 출력
        System.out.println("\n===== 수령 가능 도서관 목록 =====");
        try {
            List<LibraryDto> libList = libraryService.getLibList();
            if (libList.isEmpty()) {
                System.out.println("조회 가능한 도서관이 없습니다.");
                showBranchTransferScreen(userId, bookId, holdingLibId);
                return;
            }
            System.out.printf("%-8s %-15s %-30s%n", "도서관ID", "도서관명", "주소");
            System.out.println("-".repeat(55));
            for (LibraryDto lib : libList) {
                System.out.printf("%-8d %-15s %-30s%n",
                        lib.getLibrary_id(),
                        lib.getName(),
                        lib.getAddress());
            }

            // 수령 도서관 선택
            System.out.print("\n수령할 도서관ID를 입력하세요 (취소: 0) : ");
            int pickupLibId = scanner.nextInt();
            scanner.nextLine();

            if (pickupLibId == 0) {
                System.out.println("이전 화면으로 돌아갑니다.");
                showBranchTransferScreen(userId, bookId, holdingLibId);
                return;
            }

            // 입력한 ID가 목록에 있는지 확인
            boolean valid = false;
            for (LibraryDto lib : libList) {
                if (lib.getLibrary_id() == pickupLibId) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("없는 도서관ID입니다. 다시 선택해 주세요.");
                handleBranchTransferRequest(userId, bookId, holdingLibId);
                return;
            }

            // 분관대출 신청
            branchTransferService.requestBranchTransfer(userId, bookId, holdingLibId, pickupLibId);

        } catch (SQLException e) {
            System.out.println("[오류] 분관 대출 신청 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}