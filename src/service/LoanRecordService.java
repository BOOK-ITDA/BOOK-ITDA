package service;

import repository.LoanRecordRepository;

import java.sql.SQLException;

public class LoanRecordService {
    private final LoanRecordRepository loanRecordDao;

    public LoanRecordService(LoanRecordRepository loanRecordDao) {
        this.loanRecordDao = loanRecordDao;
    }

    public void getMyLoanRecord(int userId) throws SQLException {
        loanRecordDao.findRecord(userId);
    }
}