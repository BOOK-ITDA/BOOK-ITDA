package service;

import database.DatabaseConnector;
import dto.BranchTransferDto;
import repository.BranchTransferRepository;
import repository.CollectionRepository;
import repository.OverdueRecordRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class BranchTransferService {
    private final BranchTransferRepository branchTransferDao;
    private final OverdueRecordRepository overdueRecordDao;
    private final CollectionRepository collectionDao;

    public BranchTransferService(BranchTransferRepository branchTransferDao,
                                 OverdueRecordRepository overdueRecordDao,
                                 CollectionRepository collectionDao) {
        this.branchTransferDao = branchTransferDao;
        this.overdueRecordDao = overdueRecordDao;
        this.collectionDao = collectionDao;
    }
    //분관대출신청
    //전체 로직: (도서 선택 -> 분관신청 선택) -> 수령 원하는 도서관 목록 바로 출력 -> 도서관 ID 입력 -> 연체 여부 확인 -> 분관신청 삽입 -> 소장 테이블 예약중으로 변경
    public void requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            try {//연체 확인
                boolean hasOverdue = overdueRecordDao.hasUnpaidOverdue(conn, userId);
                if (hasOverdue) {
                    System.out.println("미납 연체가 있어 신청이 불가합니다.");
                    conn.rollback();
                    return;
                }
                //연체가 없으면 소장도서관 번호 입력 -> 분관대출신청기록 삽입 -> 소장 테이블 상태 변경
                branchTransferDao.requestBranchTransfer(conn, userId, bookId, holdingLibId, pickupLibId);
                collectionDao.updateStatus(conn, bookId, holdingLibId, "RESERVED");
                conn.commit();
                System.out.println("분관대출 신청이 완료되었습니다.");
            } catch (Exception e) {
                conn.rollback();
                throw new SQLException("분관대출 신청 중 오류 발생", e);
            }
        }
    }

    //분관대출신청목록 조회(회원용)
    public List<BranchTransferDto> findByUserId(int userId) throws SQLException {
        return branchTransferDao.findByUserId(userId);
    }
}