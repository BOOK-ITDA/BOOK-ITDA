package service;

import dao.LoanRecordDao;
import repository.LoanRecordRepository;

import java.sql.SQLException;

public class LoanRecordService {
    private final LoanRecordRepository LoanRecord = new LoanRecordDao();

    public void getMyLoanRecord(int userId) throws SQLException {
        LoanRecord.findRecord(userId);
    }
}
