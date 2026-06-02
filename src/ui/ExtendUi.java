package ui;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import dao.LoanRecordDao;
import database.DatabaseConnector;
import dto.LoanRecordDto;
import service.LoanRecordService;
import service.ExtendService;

public class ExtendUi {
    private final Scanner scanner = new Scanner(System.in);
    private final ExtendService extendService;

    public ExtendUi(ExtendService extendService) {
        this.extendService = extendService;
    }

    public void showExtendScreen (int user_id){
        System.out.println("\n=========================================");
        System.out.println("             나의 현재 대출 목록              ");
        System.out.println("=========================================");
        System.out.printf("| %-10s | %-25s | %-25s | %-12s | %-12s | %-5s |\n", "대출기록ID", "도서명", "도서관명", "대출일", "반납예정일", "연장횟수");
        System.out.println("----------------------------------------------");
        List<LoanRecordDto> loaningList = extendService.getActiveLoans(user_id);

        if (loaningList.isEmpty()){
            System.out.println("현재 대출 중인 도서가 없습니다.");
            System.out.println("=========================================");
            return;
        }
        for (LoanRecordDto loanRecordDto : loaningList){
            System.out.printf("| %-10d | %-25s | %-25s | %-12s | %-12s | %-5d |\n", loanRecordDto.getLoan_id(), loanRecordDto.getBook_name(), loanRecordDto.getLibrary_name(), loanRecordDto.getLoan_date().toString(), loanRecordDto.getDue_date().toString(), loanRecordDto.getExtension_count());
            System.out.println("--------------------------------------------");
        }

        System.out.print("연장하고 싶은 대출 기록 ID를 선택하세요 (취소 : 0) : ");
        int choiceLoanId = scanner.nextInt();
        scanner.nextLine();

        if (choiceLoanId == 0){
            System.out.println("연장 기능 실행을 취소하고 이전 메뉴로 돌아갑니다. ");
            return;
        }
        LoanRecordDto selectedLoan = null;
        for (LoanRecordDto loanRecordDto : loaningList){
            if (loanRecordDto.getLoan_id() == choiceLoanId){
                selectedLoan = loanRecordDto;
                break;
            }
        }

        if (selectedLoan == null){
            System.out.println("에러 : 현재 대출 중인 목록에 없는 번호입니다.");
            return;
            // 돌아가는 화면 UI 추가
        }
        try {
            int extensionCount = extendService.extendProcess(
                    user_id, selectedLoan.getBook_id(), selectedLoan.getLibrary_id(), choiceLoanId
            );
            System.out.println("연장이 정상적으로 완료되었습니다. (해당 도서 연장 횟수 : "+extensionCount+" )");
        } catch(IllegalStateException e){
            System.out.println("연장 실패 : " + e.getMessage());
        } catch (Exception e){
            System.out.println("시스템 에러 발생 : " + e.getMessage());
        }
    }
}
