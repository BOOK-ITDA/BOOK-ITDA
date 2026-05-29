package ui;

import java.util.Scanner;

public class SearchUi {
    private final Scanner scanner = new Scanner(System.in);
    // case문 내용은 삭제하고 해당 기능 넣으시면 됩니당
    public void showSearchScreen(){
        // 검색 서비스 (기능) 붙이기

        System.out.print("대출/예약/신청하고 싶은 도서를 입력하세요.(종료: 0) : ");
        int select = scanner.nextInt();
        // 도서 정보를 가져오는 코드가 있어야 하는데 아직 없어서 임의 변수로 세팅해놓겠습니다.
        if (select == 0)
            System.exit(0);
        else if (select >= 1 && select <= 10) { // 10 대신 검색 결과 개수 들어갈 예정!
            String status = "AVAILABLE"; // 선택한 도서의 도서 상태가 들어갈 예정!
            switch (status) {
                case "AVAILABLE": handleAvailableBook(); break;
                case "BORROWED":   handleBorrowedBook(); break;
                case "RESERVED":   handleReservedBook(); break;
                default: break;
            }
        } else {
            System.out.println("없는 도서입니다. 검색 화면으로 돌아갑니다.");
            showSearchScreen();
        }
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
    // 도서 상태별 선택지 함수
    private void handleAvailableBook(){
        printAvailableMenu();
        System.out.print("기능 선택 : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("대출하기 기능을 선택하셨습니다.");
                break;
            case 2:
                System.out.println("분관 대출 신청하기 기능을 선택하셨습니다.");
                break;
            case 3:
                System.out.println("스마트 도서관 대출 신청하기 기능을 선택하셨습니다.");
                break;
            default:
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                handleAvailableBook();
                break;
        }
    }
    private void handleBorrowedBook(){
        printBorrowedMenu();
        System.out.print("기능 선택 : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("예약하기 기능을 선택하셨습니다.");
                break;
            default:
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                handleBorrowedBook();
                break;
        }
    }
    private void handleReservedBook(){
        System.out.println("선택한 도서는 현재 '예약 중' 상태입니다.");
        System.out.println("대출/신청/예약이 불가합니다. 다음에 다시 확인해주세요.");
        LibraryUi library = new LibraryUi();
        library.showLibraryScreen(); // 기능 선택 화면으로 돌아가기
    }
}
