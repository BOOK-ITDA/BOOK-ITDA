package repository;

import java.sql.SQLException;
import dto.LoanRecordDto;
import java.util.List;
import java.sql.Connection;

public interface LoanRecordRepository {
    // 전체 대출 기록 조회 (반납 완료 포함)
    List<LoanRecordDto> findAllByUserId(Connection conn, int user_id);
    // 현재 대출 중인 기록만 조회 (return_date가 NULL인 것만)
    List<LoanRecordDto> findActiveByUserId(Connection conn, int user_id);

    // 대출 기록 데이터 삽입 - 일반 대출
    int insertLoanRecord(Connection conn, LoanRecordDto loanRecordDto);
    // 도서 연장 횟수 변경 - 연장하기
    void increaseExtendCount(Connection conn, int loan_id);
    // 예정 반납 일자 변경 - 연장하기
    void updateDueDate(Connection conn, int loan_id);
    // 도서 반납하기(회원 -> 트랜잭션)
    public boolean returnBook(int loanId, int userId);

    public List<LoanRecordDto> findRecord(int userId) throws SQLException;

    // 대출 기록 번호와 일치하는 대출 기록의 회원 ID 가져오기 - 연장하기
    int findUserIdByLoanId(Connection conn, int loan_id);
    // 도서 연장 횟수 조회 - 연장하기
    int getExtensionCount(Connection conn, int loan_id);
}
