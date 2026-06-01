package service;

import dto.LoanRecordDto;
import repository.LoanRecordRepository;

import java.sql.SQLException;
import java.util.List;

public class LoanRecordService {
    private final LoanRecordRepository loanRecordDao;

    public LoanRecordService(LoanRecordRepository loanRecordDao) {
        this.loanRecordDao = loanRecordDao;
    }

    public List<LoanRecordDto> getMyLoanRecord(int userId) throws SQLException {
        return loanRecordDao.findRecord(userId);  // return 추가
    }
}