package dao;

import dto.LoanRecordDto;
import repository.LoanRecordRepository;
import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public List<LoanRecordDto> findRecord(int userId) throws SQLException { //사서 회원 조회 기능
        String sql = "SELECT lr.loan_id, lr.user_id, lr.book_id, lr.library_id, " +
                "       lr.loan_date, lr.due_date, lr.return_date, lr.extension, " +
                "       b.name AS book_name, lib.name AS library_name " +
                "FROM LOAN_RECORD lr " +
                "JOIN BOOK b ON lr.book_id = b.book_id " +
                "JOIN LIBRARY lib ON lr.library_id = lib.library_id " +
                "WHERE lr.user_id = ?";

        List<LoanRecordDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LoanRecordDto lr = new LoanRecordDto(
                            rs.getInt("loan_id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getInt("library_id"),
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate(),
                            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                            rs.getInt("extension"),
                            rs.getString("book_name"),
                            rs.getString("library_name")
                    );
                    list.add(lr);
                }
            }
        }
        return list;
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

    public boolean returnBook(int loanId, int userId) {
        //반납하기(회원)
        //서비스 -> 대출 기록 조회 -> scanner로 반납할 대출기록번호 입력 기능 추가
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 대출기록 -> 도서관&도서 id 가져오기
            String findLoanSql =
                    "SELECT book_id, library_id FROM LOAN_RECORD " +
                            "WHERE loan_id = ? AND user_id = ? AND return_date IS NULL";

            int bookId = -1;
            int libraryId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(findLoanSql)) {
                pstmt.setInt(1, loanId);
                pstmt.setInt(2, userId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    conn.rollback();
                    return false; // 대출 기록 없거나 이미 반납됨
                }
                bookId = rs.getInt("book_id");
                libraryId = rs.getInt("library_id");
            }

            // 2. 반납일 기록
            String updateLoanSql =
                    "UPDATE LOAN_RECORD SET return_date = CURDATE() WHERE loan_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateLoanSql)) {
                pstmt.setInt(1, loanId);
                pstmt.executeUpdate();
            }
            //3. user -> loan count(대출권수 -1)
            String updateLoanCount =
                    "UPDATE USER SET loan_count = loan_count - 1 WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateLoanCount)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }

            // 4. 소장 테이블 상태 확인
            String findCollectionSql =
                    "SELECT status FROM COLLECTION WHERE book_id = ? AND library_id = ?";

            String collectionStatus = null;
            try (PreparedStatement pstmt = conn.prepareStatement(findCollectionSql)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, libraryId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    collectionStatus = rs.getString("status");
                }
            }

            if ("BORROWED".equals(collectionStatus)) {
                // 5-1. 단순 반납 -> 대출 가능으로 변경
                String updateCollectionSql =
                        "UPDATE COLLECTION SET status = 'AVAILABLE' " +
                                "WHERE book_id = ? AND library_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateCollectionSql)) {
                    pstmt.setInt(1, bookId);
                    pstmt.setInt(2, libraryId);
                    pstmt.executeUpdate();
                }

            } else if ("RESERVED".equals(collectionStatus)) {
                // 5-2. 예약 테이블 확인
                String findReserveSql =
                        "SELECT reserve_id FROM RESERVATION_RECORD " +
                                "WHERE book_id = ? AND library_id = ? AND status = 'PROCESSING'";

                int reserveId = -1; // 예약자 없을 때
                try (PreparedStatement pstmt = conn.prepareStatement(findReserveSql)) {
                    pstmt.setInt(1, bookId);
                    pstmt.setInt(2, libraryId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        reserveId = rs.getInt("reserve_id");
                    }
                }

                if (reserveId != -1) {
                    // 일반 예약 반납 -> 예약 상태만 AVAILABLE, COLLECTION은 RESERVED 유지
                    String updateReserveSql =
                            "UPDATE RESERVATION_RECORD SET status = 'AVAILABLE' WHERE reserve_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateReserveSql)) {
                        pstmt.setInt(1, reserveId);
                        pstmt.executeUpdate();
                    }
                }
                // 예약자 없음 (분관/스마트 신청) -> 아무것도 안 함, COLLECTION RESERVED 유지
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
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

    // 대출 기록 ID를 가진 회원 ID 가져오기
    @Override
    public int findUserIdByLoanId(Connection conn, int loan_id) {
        String sql = "SELECT user_id FROM loan_record WHERE loan_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, loan_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    throw new IllegalArgumentException("해당 대출 기록 정보를 찾을 수 없습니다."); // 대출 기록 정보를 찾을 수 없을 경우 처리
                }
            }
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("회원 ID 읽어오는 중 DB 오류 발생",e);
        }
    }

    @Override
    public int getExtensionCount(Connection conn, int loan_id) {
        String sql = "SELECT extension_count FROM loan_record WHERE loan_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, loan_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("extension_count");
                } else {
                    throw new IllegalArgumentException("해당 대출 기록 정보를 찾을 수 없습니다."); // 대출 기록 정보를 찾을 수 없을 경우 처리
                }
            }
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("대출 기록 연장 횟수 읽어오는 중 DB 오류 발생",e);
        }
    }

    @Override
    public List<LoanRecordDto> findBorrowedListByUserId(Connection conn, int user_id) {
        String sql = "SELECT lr.loan_id, lr.book_id, lr.library_id, " +
                " lr.loan_date, lr.due_date, lr.extension_count, " +
                " b.name AS book_name, l.name AS library_name " +
                " FROM loan_record lr " +
                " JOIN book b on lr.book_id = b.book_id " +
                " JOIN library l on lr.library_id = l.library_id " +
                " WHERE lr.user_id = ? AND lr.return_date IS NULL " +
                " ORDER BY lr.due_date ASC";
        List<LoanRecordDto> list = new ArrayList<>();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    LoanRecordDto dto = new LoanRecordDto();
                    dto.setLoan_id(rs.getInt("loan_id"));
                    dto.setBook_id(rs.getInt("book_id"));
                    dto.setLibrary_id(rs.getInt("library_id"));
                    dto.setBook_name(rs.getString("book_name"));
                    dto.setLibrary_name(rs.getString("library_name"));
                    dto.setLoan_date(rs.getDate("loan_date").toLocalDate());
                    dto.setDue_date(rs.getDate("due_date").toLocalDate());
                    dto.setExtension_count(rs.getInt("extension_count"));

                    list.add(dto);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}