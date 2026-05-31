package ui;

import dao.LoanRecordDao;
import dto.LoanRecordDto;
import service.ReturnBookService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ReturnUi {

    private final ReturnBookService returnBookService = new ReturnBookService(
            new LoanRecordDao()
    );
    private final Scanner scanner = new Scanner(System.in);

    public void showReturnBookScreen(int userId) {
        System.out.println("\n========================= 현재 대출 중인 목록 ========================="); //바로 회원의 대출 목록을 띄워줌
        try {
            List<LoanRecordDto> list = returnBookService.getActiveLoanList(userId);
            if (list.isEmpty()) {
                System.out.println("현재 대출 중인 도서가 없습니다.");
                return;
            }
            System.out.printf("%-10s %-15s %-12s %-12s %-8s%n",
                    "대출기록ID", "도서명", "대출일", "반납예정일", "연장횟수");
            System.out.println("-".repeat(70));
            for (LoanRecordDto l : list) {
                System.out.printf("%-10d %-15s %-12s %-22s %-15d%n",
                        l.getLoan_id(),
                        l.getBook_name(),
                        l.getLoan_date(),
                        l.getDue_date(),
                        l.getExtension_count());
            }

            System.out.println("===================================================================");
            System.out.println("[1] 반납하기");
            System.out.println("[0] 뒤로가기");
            System.out.println("===================================================================");
            System.out.print("기능 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) { //반납을 희망하는 대출기록 아이디 입력
                case 1:
                    System.out.print("반납할 대출기록ID를 입력하세요 : ");
                    int loanId = scanner.nextInt();
                    scanner.nextLine();
                    boolean result = returnBookService.returnBook(loanId, userId); //반납하기 서비스
                    if (result) {
                        System.out.println("반납이 완료되었습니다.");
                    } else {
                        System.out.println("반납에 실패했습니다. 대출기록ID를 확인해주세요.");
                    }
                    showReturnBookScreen(userId);
                    break;
                case 0:
                    System.out.println("이전 화면으로 돌아갑니다.");
                    return;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    showReturnBookScreen(userId);
                    break;
            }

        } catch (SQLException e) {
            System.out.println("[오류] 대출 목록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
