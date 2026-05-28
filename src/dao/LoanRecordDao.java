package dao;
import database.DatabaseConnector;
import dto.LoanRecordDto;
import repository.LoanRecordRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;

public class LoanRecordDao implements LoanRecordRepository {
    @Override
    public void findRecord(int userId) throws SQLException {
        String sql = "SELECT lr.loan_id, lr.loan_date, lr.due_date, lr.return_date, lr.extension, " +
                "b.name AS book_title, " +
                "lib.name AS library_name " +
                "FROM LOAN_RECORD lr " +
                "JOIN BOOK b ON lr.book_id = b.book_id " +
                "JOIN LIBRARY lib ON lr.library_id = lib.library_id " +
                "WHERE lr.user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("대출ID: " + rs.getInt("loan_id"));
                    System.out.println("책 이름: " + rs.getString("book_title"));
                    System.out.println("도서관: " + rs.getString("library_name"));
                    System.out.println("대출일: " + rs.getString("loan_date"));
                    System.out.println("반납기한: " + rs.getString("due_date"));
                    System.out.println("반납일: " + rs.getString("return_date"));
                    System.out.println("연장횟수: " + rs.getInt("extension"));
                    System.out.println("----------------------");
                }
            }
        }
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

    public boolean returnBook(int loanId, int userId) {
        //반납하기(회원)
        //서비스 -> 대출 기록 조회 -> scanner로 반납할 대출기록번호 입력 기능 추가
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            //대출기록 -> 도서관&도서 id 가져오기
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
                    return false;
                }
                bookId = rs.getInt("book_id");
                libraryId = rs.getInt("library_id");
            }

            // 반납일 기록
            String updateLoanSql =
                    "UPDATE LOAN_RECORD SET return_date = CURDATE() WHERE loan_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateLoanSql)) {
                pstmt.setInt(1, loanId);
                pstmt.executeUpdate();
            }

            // PROCESSING + 예약자 확인
            String findReserveSql =
                    "SELECT reserve_id FROM RESERVATION_RECORD " +
                            "WHERE book_id = ? AND library_id = ? AND status = 'PROCESSING' ";

            int reserveId = -1; //예약자가 없을 때
            try (PreparedStatement pstmt = conn.prepareStatement(findReserveSql)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, libraryId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    reserveId = rs.getInt("reserve_id");
                }
            }

            // 예약 테이블 상태 변경(소장 테이블은 그대로 예약중)
            if (reserveId != -1) {
                String updateReserveSql =
                        "UPDATE RESERVATION_RECORD SET status = 'AVAILABLE' WHERE reserve_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateReserveSql)) {
                    ps.setInt(1, reserveId);
                    ps.executeUpdate();
                }
            } else {
                // 예약자 없으면 소장 테이블: 예약중 -> 대출 가능
                String updateCollectionSql =
                        "UPDATE COLLECTION SET status = 'AVAILABLE' " +
                                "WHERE book_id = ? AND library_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateCollectionSql)) {
                    ps.setInt(1, bookId);
                    ps.setInt(2, libraryId);
                    ps.executeUpdate();
                }
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
}
