package service;

import database.DatabaseConnector;
import dto.LoanRecordDto;
import repository.CollectionRepository;
import repository.LoanRecordRepository;
import repository.OverdueRecordRepository;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExtendService {
    // 인터페이스에 의존
    private final CollectionRepository collectionRepository;
    private final OverdueRecordRepository overdueRecordRepository;
    private final LoanRecordRepository loanRecordRepository;

    // 생성자를 통한 의존성 주입
    public ExtendService(CollectionRepository collectionRepository, OverdueRecordRepository overdueRecordRepository, LoanRecordRepository loanRecordRepository) {
        this.collectionRepository = collectionRepository;
        this.overdueRecordRepository = overdueRecordRepository;
        this.loanRecordRepository = loanRecordRepository;
    }

    public int extendProcess(int user_id, int book_id, int library_id, int loan_id){
        try (Connection conn = DatabaseConnector.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // 대출한 회원과 로그인한 회원 일치 여부 확인
                int loaningUser = loanRecordRepository.findUserIdByLoanId(conn, loan_id);
                if (loaningUser != user_id){
                    throw new IllegalStateException("연장 불가 : 해당 도서를 대여하고 있는 회원이 아닙니다.");
                }

                // 도서 상태 확인
                String bookStatus = collectionRepository.getStatus(conn, book_id, library_id);
                if (!"BORROWED".equals(bookStatus)){
                    throw new IllegalStateException("연장 불가 : 해당 도서를 대여하고 있지 않습니다.");
                }

                // 연체료 미납 여부 확인
                boolean hasOverdue = overdueRecordRepository.hasUnpaidOverdue(conn, user_id);
                if(hasOverdue){
                    throw new IllegalStateException("연장 불가 : 미납된 연체료가 존재하는 회원입니다.");
                }

                // 현재 연장 횟수 확인
                int extensionCount = loanRecordRepository.getExtensionCount(conn, loan_id);
                if (extensionCount >= 2){
                    throw new IllegalStateException("연장 불가 : 최대 연장 횟수를 초과했습니다.");
                }

                loanRecordRepository.increaseExtendCount(conn, loan_id);
                loanRecordRepository.updateDueDate(conn, loan_id);
                conn.commit();
                return extensionCount + 1;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("롤백 중 에러 발생", ex);
                }
                if ( e instanceof IllegalStateException ) {
                    throw (IllegalStateException) e;
                }
                throw new RuntimeException("연장 처리 중 데이터베이스 오류 발생: " + e.getMessage(),e);

            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패", e);
        }
    }
}
