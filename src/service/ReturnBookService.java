package service;

import database.DatabaseConnector;
import dto.LoanRecordDto;
import repository.LoanRecordRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReturnBookService {
    private final LoanRecordRepository loanRecordDao;

    public ReturnBookService(LoanRecordRepository loanRecordDao) {
        this.loanRecordDao = loanRecordDao;
    }

    //회원 아이디 -> 반납하지 않은 대출 기록을 조회하게 함
    public List<LoanRecordDto> getActiveLoanList(int userId) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return loanRecordDao.findActiveByUserId(conn, userId);
        }
    }
    //반납(회원이 반납하려는 대출기록 번호를 입력하면 -> 해당 대출 기록에서  도서관&도서 id 가져옴(셀렉) -> 반납일 업데이트 -> 소장 테이블 확인 -> 1. 단순 대출이면 그냥 반납, 2. 예약중 -> 2-1. 예약 테이블 확인 및 상태 변경, 2-2. 다른 기록 테이블 상태 확인 및 유지 또는 변경
    public boolean returnBook(int loanId, int userId) {
        return loanRecordDao.returnBook(loanId, userId);
    }
}