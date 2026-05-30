package service;

import dao.BranchTransferDao;
import dao.CollectionDao;
import dao.OverdueRecordDao;
import database.DatabaseConnector;
import repository.BranchTransferRepository;
import repository.CollectionRepository;
import repository.OverdueRecordRepository;

import java.sql.Connection;
import java.sql.SQLException;


public class BranchTransferService {
    private BranchTransferRepository branchTransferDao = new BranchTransferDao(); //분관대출신청
    private OverdueRecordRepository overdueRecordDao = new OverdueRecordDao();
    private CollectionRepository collectionDao = new CollectionDao();
    //도서 상태 확인(소장 테이블 dao) -> 연체 여부 확인(연체 기록 테이블 dao) -> 도서관 목록 확인 및 선택(도서관 테이블 dao)
    // -> 분관대출 신청기록 생성 insert(여기서) -> 소장 테이블 상태 변경(reserved, 소장 테이블 dao)

    public void requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) throws SQLException {

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. 연체 여부 확인
                boolean hasOverdue = overdueRecordDao.hasUnpaidOverdue(conn, userId);
                if (hasOverdue) {
                    System.out.println("미납 연체가 있어 신청이 불가합니다.");
                    conn.rollback();
                    return;
                }

                // 2. (앞에서 이미 대출 가능 상태인거 확인하고 넘겨줌) -> 연체 없음 -> 분관대출 가능 -> 분관대출 신청 INSERT
                branchTransferDao.requestBranchTransfer(conn, userId, bookId, holdingLibId, pickupLibId);

                // 3. 소장 테이블 상태 RESERVED로 변경
                collectionDao.updateStatus(conn, bookId, holdingLibId, "RESERVED");

                conn.commit();
                System.out.println("분관대출 신청이 완료되었습니다.");

            } catch (Exception e) {
                conn.rollback();
                throw new SQLException("분관대출 신청 중 오류 발생", e);
            }
        }
    }


}