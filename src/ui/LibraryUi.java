package ui;

import dao.CollectionDao;
import dao.LoanRecordDao;
import dao.OverdueRecordDao;
import repository.CollectionRepository;
import repository.LoanRecordRepository;
import repository.OverdueRecordRepository;
import service.ExtendService;
import service.LoanRecordService;
import session.Session;

import java.util.Scanner;

public class LibraryUi {
    private final Scanner scanner = new Scanner(System.in);

    int userId = Session.getUserId(); //회원 아이디 가져오는 부분이에용 -> 각자 유저 아이디 인풋으로 필요하시면 넣으면 됩니당.

    public void showLibraryScreen(){
        printLibraryMenu();
        System.out.print("메뉴 선택 : ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice){
            case 0:
                System.out.println("프로그램을 종료합니다. 이용해주셔서 감사합니다.");
                System.exit(0);
                break;
            case 1:
                System.out.println("도서 조회 기능을 선택하셨습니다.");
                new SearchUi().showSearchScreen();
                showLibraryScreen();
                break;
            case 2:
                System.out.println("도서 반납 기능을 선택하셨습니다.");
                new ReturnUi().showReturnBookScreen(userId);
                showLibraryScreen();
                break;
            case 3:
                System.out.println("도서 연장 기능을 선택하셨습니다.");
                ExtendUi extendUi = new ExtendUi(new ExtendService(new CollectionDao(), new OverdueRecordDao(), new LoanRecordDao()));
                extendUi.showExtendScreen(userId);
                showLibraryScreen();
                break;
            case 4:
                System.out.println("대시보드 기능을 선택하셨습니다.");
                new DashBoardUi(userId).showDashBoardScreen();
                showLibraryScreen();
                break;
            default :
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                showLibraryScreen();
                return;


        }

    }
    // 선택지 출력 함수
    public void printLibraryMenu(){
        System.out.println("아래의 기능들 중 원하시는 메뉴의 번호를 입력해주세요.");
        System.out.println("====================================================");
        System.out.println("[0] 프로그램 종료");
        System.out.println("[1] 도서 조회 (검색, 대출, 예약, 신청)");
        System.out.println("[2] 도서 반납");
        System.out.println("[3] 도서 연장");
        System.out.println("[4] 대시보드");
        System.out.println("====================================================");
    }
}
