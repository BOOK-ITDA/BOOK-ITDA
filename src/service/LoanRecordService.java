package service;

import dto.LoanRecordDto;
import repository.LoanRecordRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoanRecordService {
    private final LoanRecordRepository loanRecordRepository;

    public LoanRecordService(LoanRecordRepository loanRecordRepository) {
        this.loanRecordRepository = loanRecordRepository;
    }

    public void getMyLoanRecord(int userId) throws SQLException {
        loanRecordRepository.findRecord(userId);
    }
}