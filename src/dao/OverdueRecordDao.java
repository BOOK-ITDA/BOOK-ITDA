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

    @Override
    public void insertOverdueRecord(Connection conn) throws SQLException {
        // 연체 기록 저장 (연체료, 연체료 납부 여부 기본 값 처리
        // -> 바로 연체료 업데이트 메서드 사용할 예정이라 삽입할 때는 연체료 0 이어도 괜찮아요.
        // 새 연체 기록이 여러 건 생성될 수 있음 + 사용자 관여 X 로직이기 때문에 DTO 사용 X
        String sql = "INSERT INTO overdue_record (loan_id) " +
                "SELECT loan_id FROM loan_record " +
                "WHERE return_date IS NULL AND due_date < CURDATE() " +
                "AND loan_id NOT IN (SELECT loan_id FROM overdue_record)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            int affectedRow = pstmt.executeUpdate();
            System.out.println("금일 신규 등록된 연체 기록 개수 : " + affectedRow);
        }
    }

    @Override // 연체료 업데이트 (일간 업데이트)
    public void updateDailyFineAmount(Connection conn) throws SQLException {
        String sql = "UPDATE overdue_record SET fine_amount = fine_amount + 100 " +
                "WHERE is_paid = 0";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            int affectedRow = pstmt.executeUpdate();
            System.out.println("누적 연체료 갱신 완료 : " + affectedRow);
        }
    }
}
