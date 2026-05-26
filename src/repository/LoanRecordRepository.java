package repository;
import dto.LoanRecordDto;
import java.sql.Connection;

public interface LoanRecordRepository {
    // 대출 기록 데이터 삽입 - 일반 대출
    int insertLoanRecord(Connection conn, LoanRecordDto loanRecordDto);
}