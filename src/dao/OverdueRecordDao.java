package dao;
import repository.OverdueRecordRepository;
import java.sql.*;

public class OverdueRecordDao implements OverdueRecordRepository {
    @Override
    public boolean hasUnpaidOverdue(Connection conn, int user_id) {
        String sql = "SELECT COUNT(*) FROM overdue_record o " +
                "JOIN loan_record l ON o.loan_id = l.loan_id " +
                "WHERE l.user_id = ? AND o.is_paid = 0";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user_id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("미납 연체 정보 조회 중 DB 오류 발생",e);
        }
        return false;
    }
}
