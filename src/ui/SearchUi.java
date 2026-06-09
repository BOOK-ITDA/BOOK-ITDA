package ui;

import dao.CollectionDao;
import dao.LoanRecordDao;
import dao.OverdueRecordDao;
import dao.UserDao;
import dto.BookDto;
import repository.CollectionRepository;
import repository.OverdueRecordRepository;
import repository.ReservationRecordRepository;
import service.BookService;
import service.LoanService;
import service.ReserveService;
import dao.ReservationRecordDao;
import session.Session;
import ui.BTRUi;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SearchUi {
    private final Scanner scanner = new Scanner(System.in);
    private final BookService bookService = new BookService();

    int userId = Session.getUserId(); //회원 아이디 가져오는 부분입니당

    // case문 내용은 삭제하고 해당 기능 넣으시면 됩니당
    public void showSearchScreen(){
        while (true) {
            // 검색 서비스 (기능)
            System.out.print("\n검색어를 입력하세요 (뒤로가기: 0) : ");
            String keyword = scanner.nextLine().trim();

            if (keyword.equals("0")) return; // LibraryUi로 복귀

            // 검색 실행
            List<BookDto> searchList;
            try {
                searchList = bookService.search(keyword);
            } catch (SQLException e) {
                System.out.println("검색 중 오류가 발생했습니다: " + e.getMessage());
                continue;
            }

            // 검색 결과 없음
            if (searchList.isEmpty()) {
                System.out.println("해당하는 도서 정보가 없습니다.");
                continue;
            }

        while (true) {
            // 검색 결과 출력
            printSearchResult(searchList);

            // 도서 번호 선택
            System.out.print("대출/예약/신청하고 싶은 도서를 입력하세요.(뒤로가기: 0) : ");
            int select;
            try {
                select = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("올바른 번호를 입력해주세요.");
                continue;
            }

            if (select == 0) break; // LibraryUi로 복귀

            if (select < 1 || select > searchList.size()) {
                System.out.println("없는 도서입니다. 검색 화면으로 돌아갑니다.");
                continue;
            }

            // 선택한 도서 DTO (번호는 1부터 시작, 리스트 인덱스는 0부터라 -1)
            BookDto selected = searchList.get(select - 1);

            // 도서 상태에 따라 분기
            switch (selected.getStatus()) {
                case "AVAILABLE":
                    if (handleAvailableBook(selected)){
                        return;
                    }
                    break;
                case "BORROWED":
                    if (handleBorrowedBook(selected)){
                        return;
                    }
                    break;
                case "RESERVED":
                    if (handleReservedBook()){
                        return;
                    };
                    break;
                default:
                    System.out.println("없는 도서입니다. 다시 선택해 주세요");
                    break;
            }
        }
        }
    }

    // ── 검색 결과 테이블 출력 ──
    private void printSearchResult(List<BookDto> list) {
        System.out.println("\n====================================================================");
        System.out.printf("%-6s %-20s %-12s %-12s %-8s %-16s %-8s%n",
                "번호", "제목", "저자", "출판사", "장르", "소장 도서관", "상태");
        System.out.println("====================================================================");
        for (int i = 0; i < list.size(); i++) {
            BookDto b = list.get(i);
            System.out.printf("%-4d %-20s %-12s %-10s %-10s %-12s %-8s%n",
                    i + 1,
                    b.getName(),
                    b.getAuthor(),
                    b.getPublisher(),
                    b.getGenre(),
                    b.getLibrary_name(),
                    translateStatus(b.getStatus()));
        }
        System.out.println("====================================================================");
    }

    // 상태값 한글 변환
    private String translateStatus(String status) {
        switch (status) {
            case "AVAILABLE": return "대출 가능";
            case "BORROWED":  return "대출 중";
            case "RESERVED":  return "예약 중";
            default:          return status;
        }
    }

    // ===== 도서 상태별 선택지 함수 =====
    // ── AVAILABLE: 메뉴 출력 + 선택 → 각 기능은 담당자 연결 예정 ──
    private boolean handleAvailableBook(BookDto selected) {
        while (true) {
            printAvailableMenu();
            System.out.print("기능 선택 (취소: 0) : ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("올바른 번호를 입력해주세요.");
                continue;
            }
            if (choice==0){
                System.out.println("도서 선택 화면으로 돌아갑니다.");
                return false;
            }

            switch (choice) {
                case 1:
                    // 대출 기능은 코드가 짧아서 우선 따로 UI 파일을 만들지는 않았습니다.
                    System.out.println("대출하기 기능을 선택하셨습니다.");
                    LoanService loanService = new LoanService(new CollectionDao(), new OverdueRecordDao(), new UserDao(), new LoanRecordDao());
                    int loan_num = loanService.loanProcess(userId, selected.getBook_id(), selected.getLibrary_id());
                    System.out.println("대출이 완료되었습니다. (대출 기록 번호 : " + loan_num + " )");
                    return true;
                case 2:
                    System.out.println("분관 대출 신청하기 기능을 선택하셨습니다.");
                    BTRUi branchTransferUI = new BTRUi();
                    boolean success = branchTransferUI.showBranchTransferScreen(
                            userId,
                            selected.getBook_id(),          // selected에서 꺼내기
                            selected.getLibrary_id()        // selected에서 꺼내기
                    );
                    if (success) return true;
                    break;
                case 3:
                    System.out.println("스마트 도서관 대출 신청하기 기능을 선택하셨습니다.");
                    SmartLibraryUi smartLibraryUi = new SmartLibraryUi();
                    smartLibraryUi.showSmartLibraryScreen(
                            Session.getUserId(),
                            selected.getBook_id(),
                            selected.getLibrary_id()
                    );
                    return true;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    break;
            }
        }
    }

    // ── BORROWED: 메뉴 출력 + 선택 → 예약 기능은 담당자 연결 예정 ──
    private boolean handleBorrowedBook(BookDto selected) {
        while (true) {
            printBorrowedMenu();
            System.out.print("기능 선택 (취소 : 0) : ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("올바른 번호를 입력해주세요.");
                continue;
            }

            if (choice==0){
                System.out.println("도서 선택 화면으로 돌아갑니다.");
                return false;
            }

            switch (choice) {
                case 1:
                    System.out.println("예약하기 기능을 선택하셨습니다.");
                    ReserveService reserveService = new ReserveService(new CollectionDao(), new OverdueRecordDao(), new ReservationRecordDao());
                    int reserve_num = reserveService.reserveProcess(userId, selected.getBook_id(),          // selected에서 꺼내기
                            selected.getLibrary_id());
                    System.out.println("예약이 완료되었습니다. (예약 기록 번호 : " + reserve_num + " )");
                    return true;
                default:
                    System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                    break;
            }
        }
    }

    // ── RESERVED: 대출/신청/예약 불가 안내 ──
    private boolean handleReservedBook() {
        System.out.println("선택한 도서는 현재 '예약 중' 상태입니다.");
        System.out.println("대출/신청/예약이 불가합니다. 다음에 다시 확인해주세요.");
        return true;
    }

    // 선택지 출력 함수
    private void printAvailableMenu(){
        System.out.println("선택한 도서는 현재 '대출 가능' 상태입니다.");
        System.out.println("====================================================");
        System.out.println("[1] 대출하기");
        System.out.println("[2] 분관 대출 신청하기");
        System.out.println("[3] 스마트 도서관 대출 신청하기");
        System.out.println("====================================================");
    }
    private void printBorrowedMenu(){
        System.out.println("선택한 도서는 현재 '대출 중' 상태입니다.");
        System.out.println("====================================================");
        System.out.println("[1] 예약하기");
        System.out.println("====================================================");
    }
}
