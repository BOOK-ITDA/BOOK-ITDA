package service;
import dto.LoanRecordDto;
import repository.*;
import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import dto.CollectionDto.BookStatus;

public class LoanService {
    // 인터페이스에 의존
    private final CollectionRepository collectionRepository;
    private final OverdueRecordRepository overdueRecordRepository;
    private final UserRepository userRepository;
    private final LoanRecordRepository loanRecordRepository;

    // 생성자를 통한 의존성 주입
    public LoanService(CollectionRepository collectionRepository, OverdueRecordRepository overdueRecordRepository, UserRepository userRepository, LoanRecordRepository loanRecordRepository) {
        this.collectionRepository = collectionRepository;
        this.overdueRecordRepository = overdueRecordRepository;
        this.userRepository = userRepository;
        this.loanRecordRepository = loanRecordRepository;
    }

    public int loanProcess(int user_id, int book_id, int library_id){
        try (Connection conn = DatabaseConnector.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // 도서 상태 확인
                BookStatus bookStatus = collectionRepository.getStatus(conn, book_id, library_id);
                if (bookStatus != BookStatus.AVAILABLE){
                    throw new IllegalStateException("대출 불가 : 해당 도서는 대출 가능하지 않습니다.");
                }

                // 연체료 미납 여부 확인
                boolean hasOverdue = overdueRecordRepository.hasUnpaidOverdue(conn, user_id);
                if(hasOverdue){
                    throw new IllegalStateException("대출 불가 : 미납된 연체료가 존재하는 회원입니다.");
                }

                // 회원 현재 대출 권수 확인
                int loanCount = userRepository.getLoanCount(conn, user_id);
                if (loanCount > 5){
                    throw new IllegalStateException("대출 불가 : 최대 대출 가능 권수를 초과했습니다.");
                }

                LocalDate loan_date = LocalDate.now();
                LocalDate due_date = loan_date.plusDays(14);

                LoanRecordDto newLoan = new LoanRecordDto(user_id, book_id, library_id, loan_date, due_date, null, 0);
                int generatedLoanId = loanRecordRepository.insertLoanRecord(conn, newLoan);

                collectionRepository.updateStatus(conn, book_id, library_id, BookStatus.BORROWED);
                userRepository.increaseLoanCount(conn, user_id);

                conn.commit();
                return generatedLoanId;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("롤백 중 에러 발생", ex);
                }
                    if ( e instanceof IllegalStateException ) {
                        throw (IllegalStateException) e;
                    }
                    throw new RuntimeException("대출 처리 중 데이터베이스 오류 발생: " + e.getMessage(),e);

            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패", e);
        }
    } // loanProcess 메서드 종료
} // LoanService 클래스 종료
