package dao;

import dto.OverdueRecordDto;
import database.DatabaseConnector;
import dto.OverdueRecordDto;
import repository.OverdueRecordRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueRecordDao implements OverdueRecordRepository {

    @Override
    public boolean hasUnpaidOverdue(Connection conn, int user_id) {
        String sql = "SELECT COUNT(*) FROM overdue_record o " +
                "JOIN loan_record l ON o.loan_id = l.loan_id " +
                "WHERE l.user_id = ? AND o.is_paid = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("미납 연체 정보 조회 중 DB 오류 발생", e);
        }
        return false;
    }

    // 미납 연체 기록만 조회
    @Override
    public List<OverdueRecordDto> findUnpaidByUserId(Connection conn, int user_id) {
        String sql = buildSelectSql(false); // is_paid 조건 포함
        return executeQuery(conn, sql, user_id);
    }

    // 전체 연체 기록 조회 (납부 완료 포함)
    @Override
    public List<OverdueRecordDto> findAllByUserId(Connection conn, int user_id) {
        String sql = buildSelectSql(true); // is_paid 조건 없음
        return executeQuery(conn, sql, user_id);
    }

    // SQL 문자열 생성 — unpaidOnly에 따라 WHERE 조건 분기
    private String buildSelectSql(boolean includeAll) {
        String base = "SELECT o.overdue_id, o.loan_id, o.fine_amount, o.is_paid, " +
                "b.name AS book_name, " +
                "l.loan_date, l.due_date " +
                "FROM overdue_record o " +
                "JOIN loan_record l ON o.loan_id = l.loan_id " +
                "JOIN book b ON l.book_id = b.book_id " +
                "WHERE l.user_id = ?";
        if (!includeAll) {
            base += " AND o.is_paid = 0"; // 미납만 조회할 경우 조건 추가
        }
        return base;
    }

    // 공통 쿼리 실행 — ResultSet을 OverdueRecordDto 리스트로 변환
    private List<OverdueRecordDto> executeQuery(Connection conn, String sql, int user_id) {
        List<OverdueRecordDto> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new OverdueRecordDto(
                            rs.getInt("overdue_id"),
                            rs.getInt("loan_id"),
                            rs.getInt("fine_amount"),
                            rs.getBoolean("is_paid"),
                            rs.getString("book_name"),
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("연체 기록 조회 중 DB 오류 발생", e);
        }
        return list;
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
        String sql = "UPDATE overdue_record o " +
                "JOIN loan_record lr ON o.loan_id = lr.loan_id " +
                "SET o.fine_amount = fine_amount + 100 " +
                "WHERE is_paid = 0 AND lr.return_date IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int affectedRow = pstmt.executeUpdate();
            System.out.println("누적 연체료 갱신 완료 : " + affectedRow);
        }
    }

    public List<OverdueRecordDto> getOverdue() throws SQLException {
        //사서 연체 기록 조회 기능

        String sql =
                "SELECT od.overdue_id, od.loan_id, od.fine_amount, od.is_paid, u.user_id, u.name AS user_name, b.name AS book_name, lr.due_date " +
                        "FROM OVERDUE_RECORD od " +
                        "JOIN LOAN_RECORD lr ON od.loan_id = lr.loan_id " +
                        "JOIN USER u ON lr.user_id  = u.user_id " +
                        "JOIN BOOK b ON lr.book_id  = b.book_id";

        List<OverdueRecordDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OverdueRecordDto od = new OverdueRecordDto(
                        rs.getInt("overdue_id"),
                        rs.getInt("loan_id"),
                        rs.getInt("fine_amount"),
                        rs.getBoolean("is_paid"),
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("book_name"),
                        rs.getDate("due_date").toLocalDate()
                );
                list.add(od);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("연체 기록 조회 중 오류 발생", e);
        }

        return list;
    }

    @Override
    public void updateStatus(int overdueId) throws SQLException {
        String checkSql = "SELECT is_paid FROM OVERDUE_RECORD WHERE overdue_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, overdueId);
            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getBoolean("is_paid")) {
                        System.out.println("이미 납부 처리된 기록입니다.");
                        return;
                    }
                } else {
                    System.out.println("존재하지 않는 연체기록ID입니다.");
                    return;
                }
            }
        }

        String sql = "UPDATE OVERDUE_RECORD SET is_paid = true WHERE overdue_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, overdueId);
            pstmt.executeUpdate();
            System.out.println("상태 변경 완료");
        }
    }
}
