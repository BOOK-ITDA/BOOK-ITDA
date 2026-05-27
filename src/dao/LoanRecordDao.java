package dao;
import dto.LoanRecordDto;
import repository.LoanRecordRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRecordDao implements LoanRecordRepository {

    // ──────────────────────────────────────────────
    // 공통 메서드: ResultSet → LoanRecordDto 변환
    // ──────────────────────────────────────────────
    private LoanRecordDto mapRow(ResultSet rs) throws SQLException {
        return new LoanRecordDto(
                rs.getInt("loan_id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getInt("library_id"),
                rs.getDate("loan_date") != null
                        ? rs.getDate("loan_date").toLocalDate() : null,
                rs.getDate("due_date") != null
                        ? rs.getDate("due_date").toLocalDate() : null,
                rs.getDate("return_date") != null
                        ? rs.getDate("return_date").toLocalDate() : null,
                rs.getInt("extension_count")
        );
    }

    // ──────────────────────────────────────────────
    // 1. 전체 대출 기록 조회 (반납 완료 포함)
    // ──────────────────────────────────────────────
    @Override
    public List<LoanRecordDto> findAllByUserId(int user_id) {
        String sql =
                "SELECT loan_id, user_id, book_id, library_id, " +
                "       loan_date, due_date, return_date, extension_count " +
                "FROM LOAN_RECORD " +
                "WHERE user_id = ? " +
                "ORDER BY loan_date DESC";

        List<LoanRecordDto> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ──────────────────────────────────────────────
    // 2. 현재 대출 중인 기록만 조회 (return_date = NULL)
    // ──────────────────────────────────────────────
    @Override
    public List<LoanRecordDto> findActiveByUserId(int user_id) {
        String sql =
                "SELECT loan_id, user_id, book_id, library_id, " +
                "       loan_date, due_date, return_date, extension_count " +
                "FROM LOAN_RECORD " +
                "WHERE user_id = ? " +
                "  AND return_date IS NULL " +
                "ORDER BY due_date ASC";

        List<LoanRecordDto> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}