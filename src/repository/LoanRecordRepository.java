package repository;
import dto.LoanRecordDto;
import java.sql.Connection;

public interface LoanRecordRepository {
    // 대출 기록 데이터 삽입 - 일반 대출
    int insertLoanRecord(Connection conn, LoanRecordDto loanRecordDto);
    // 도서 연장 횟수 변경 - 연장하기
    void increaseExtendCount(Connection conn, int loan_id);
    // 예정 반납 일자 변경 - 연장하기
    void updateDueDate(Connection conn, int loan_id);
}