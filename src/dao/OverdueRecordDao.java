package dao;

import dto.OverdueRecordDto;
import repository.OverdueRecordRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueRecordDao implements OverdueRecordRepository {

    // 기존 메서드 — 보현 작성, 그대로 유지
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
                            rs.getString("loan_date"),
                            rs.getString("due_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("연체 기록 조회 중 DB 오류 발생", e);
        }
        return list;
    }
}
