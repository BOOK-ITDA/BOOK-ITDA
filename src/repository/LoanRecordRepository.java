package repository;

import java.sql.SQLException;


public interface LoanRecordRepository {
    //대출 기록 조회(회원)
    void findRecord(int userId) throws SQLException;
}
