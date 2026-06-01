package service;

import database.DatabaseConnector;
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

    public List<LoanRecordDto> getActiveLoans(int user_id) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return loanRecordRepository.findActiveByUserId(conn, user_id);
        } catch (SQLException e) {
            throw new RuntimeException("현재 대출 중인 목록을 불러오는 중 데이터베이스 에러 발생", e);
        }
    }
}