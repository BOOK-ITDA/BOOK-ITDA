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
                rs.getInt("library_id"),
                rs.getString("book_name"),
                rs.getString("library_name"),
                rs.getDate("loan_date") != null
                        ? rs.getDate("loan_date").toLocalDate() : null,
                rs.getDate("due_date") != null
                        ? rs.getDate("due_date").toLocalDate() : null,
                rs.getDate("return_date") != null
                        ? rs.getDate("return_date").toLocalDate() : null,
                rs.getInt("extension_count")
        );
    }

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

    @Override
    public void increaseExtendCount(Connection conn, int loan_id) {
        String sql = "UPDATE loan_record SET extension_count = extension_count+1 WHERE loan_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, loan_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0)
                throw new SQLException("해당 대출 기록 정보를 찾을 수 없습니다."); // 대출 기록 정보를 찾을 수 없을 경우 처리
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("연장 횟수 업데이트 중 DB 오류 발생",e);
        }
    }

    @Override
    public void updateDueDate(Connection conn, int loan_id) {
        String sql = "UPDATE loan_record SET due_date = DATE_ADD(due_date, INTERVAL 7 DAY) WHERE loan_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, loan_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0)
                throw new SQLException("해당 대출 기록 정보를 찾을 수 없습니다."); // 대출 기록 정보를 찾을 수 없을 경우 처리
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("예정 반납 일자 업데이트 중 DB 오류 발생",e);
        }
    }

    // ──────────────────────────────────────────────
    // 1. 전체 대출 기록 조회 (반납 완료 포함)
    // ──────────────────────────────────────────────
    @Override
    public List<LoanRecordDto> findAllByUserId(Connection conn, int user_id) {
        String sql =
                "SELECT lr.loan_id, lr.library_id, " +
                        "       lr.loan_date, lr.due_date, lr.return_date, lr.extension_count, " +
                        "       b.name AS book_name, " +
                        "       l.name AS library_name " +
                        "FROM LOAN_RECORD lr " +
                        "JOIN BOOK b ON lr.book_id = b.book_id " +
                        "JOIN LIBRARY l ON lr.library_id = l.library_id " +
                        "WHERE lr.user_id = ? " +
                        "ORDER BY lr.loan_date DESC";

        List<LoanRecordDto> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public List<LoanRecordDto> findActiveByUserId(Connection conn, int user_id) {
        String sql =
                "SELECT lr.loan_id, lr.library_id, " +
                        "       lr.loan_date, lr.due_date, lr.return_date, lr.extension_count, " +
                        "       b.name AS book_name, " +
                        "       l.name AS library_name " +
                        "FROM LOAN_RECORD lr " +
                        "JOIN BOOK b ON lr.book_id = b.book_id " +
                        "JOIN LIBRARY l ON lr.library_id = l.library_id " +
                        "WHERE lr.user_id = ? " +
                        "  AND lr.return_date IS NULL " +
                        "ORDER BY lr.due_date ASC";

        List<LoanRecordDto> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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