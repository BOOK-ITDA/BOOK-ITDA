package service;

import dao.OverdueRecordDao;
import database.DatabaseConnector;
import dto.BranchTransferDto;
import repository.BranchTransferRepository;
import repository.CollectionRepository;
import repository.OverdueRecordRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class BranchTransferService {
    private final BranchTransferRepository branchTransferRepository;
    private final OverdueRecordRepository overdueRecordRepository;
    private final CollectionRepository collectionRepository;

    public BranchTransferService(BranchTransferRepository branchTransferRepository,
                                 OverdueRecordRepository overdueRecordRepository,
                                 CollectionRepository collectionRepository) {
        this.branchTransferRepository = branchTransferRepository;
        this.overdueRecordRepository = overdueRecordRepository;
        this.collectionRepository = collectionRepository;
    }
    //분관대출신청
    //전체 로직: (도서 선택 -> 분관신청 선택) -> 수령 원하는 도서관 목록 바로 출력 -> 도서관 ID 입력 -> 연체 여부 확인 -> 분관신청 삽입 -> 소장 테이블 예약중으로 변경
    public void requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);


            try {
                // 1. 연체 여부 확인

                boolean hasOverdue = overdueRecordRepository.hasUnpaidOverdue(conn, userId);

                if (hasOverdue) {
                    System.out.println("미납 연체가 있어 신청이 불가합니다.");
                    conn.rollback();
                    return;
                }


                // 2. (앞에서 이미 대출 가능 상태인거 확인하고 넘겨줌) -> 연체 없음 -> 분관대출 가능 -> 분관대출 신청 INSERT
                branchTransferRepository.requestBranchTransfer(conn, userId, bookId, holdingLibId, pickupLibId);

                // 3. 소장 테이블 상태 RESERVED로 변경
                collectionRepository.updateStatus(conn, bookId, holdingLibId, "RESERVED");


                conn.commit();
                System.out.println("분관대출 신청이 완료되었습니다.");
                System.out.println("대출 가능 여부는 대시보드에서 확인하실 수 있습니다.");
            } catch (Exception e) {
                conn.rollback();
                throw new SQLException("분관대출 신청 중 오류 발생", e);
            }
        }
    }

    //분관대출신청목록 조회(회원용)
    public List<BranchTransferDto> findByUserId(int userId) throws SQLException {
        return branchTransferRepository.findByUserId(userId);
    }
}