package ui;

import dao.BranchTransferDao;
import dao.CollectionDao;
import dao.OverdueRecordDao;
import dto.BranchTransferDto;
import service.BranchTransferService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ViewBTRUi {
    //회원 전용 분관대출기록 조회 ui입니당

    private final BranchTransferService branchTransferService = new BranchTransferService(
            new BranchTransferDao(),
            new OverdueRecordDao(),
            new CollectionDao()
    );
    private final Scanner scanner = new Scanner(System.in);

    public void showBranchTransferRecord(int userId) {
        System.out.println("\n======================================== 분관 대출 신청 기록 ==================================================");
        try {
            List<BranchTransferDto> list = branchTransferService.findByUserId(userId);
            if (list.isEmpty()) {
                System.out.println("분관 대출 신청 기록이 없습니다.");
                return;
                //회원 대시보드로 return하는 부분 추가 예정!!!
            }
            System.out.printf("%-8s %-15s %-25s %-25s %-25s%n",
                    "신청ID", "도서명", "소장도서관", "수령도서관", "처리상태");
            System.out.println("-".repeat(100));
            for (BranchTransferDto b : list) {
                System.out.printf("%-8d %-15s %-20s %-20s %-18s%n",
                        b.getTransf_req_id(),
                        b.getBook_name(),
                        b.getHolding_lib_name(),
                        b.getPickup_lib_name(),
                        b.getStatus());
            }

            System.out.println("======================================================================================================");
            System.out.println("[0] 뒤로가기");
            System.out.println("======================================================================================================");
            System.out.print("기능 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("이전 화면으로 돌아갑니다.");
                //return 회원 대시보드!!!!! 추가!!!!!
            } else {
                System.out.println("없는 메뉴입니다. 다시 선택해 주세요.");
                showBranchTransferRecord(userId);
            }

        } catch (SQLException e) {
            System.out.println("[오류] 분관 대출 신청 기록 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}