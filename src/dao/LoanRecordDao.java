package dao;
import dto.LoanRecordDto;
import repository.LoanRecordRepository;
import java.sql.*;

public class LoanRecordDao implements LoanRecordRepository {
    @Override // 대출기록 삽입 (실제 반납일자, 연장 횟수는 데이터베이스 기본값으로 저장)
    public int insertLoanRecord(Connection conn, LoanRecordDto dto) {
        String sql = "INSERT INTO loan_record (user_id, book_id, library_id, loan_date, due_date) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, dto.getUser_id());
            pstmt.setInt(2, dto.getBook_id());
            pstmt.setInt(3, dto.getLibrary_id());
            pstmt.setDate(4, java.sql.Date.valueOf(dto.getLoan_date()));
            pstmt.setDate(5, java.sql.Date.valueOf(dto.getDue_date()));

            pstmt.executeUpdate();
            try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int loan_id = generatedKeys.getInt(1);
                    dto.setLoan_id(loan_id);
                    return loan_id;
                } else throw new SQLException("생성된 ID를 가져올 수 없습니다.");
            }
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("대출 기록 생성 중 DB 오류 발생",e);
        }
    }
}
