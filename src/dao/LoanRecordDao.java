package dao;
import database.DatabaseConnector;
import dto.LoanRecordDto;
import repository.LoanRecordRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
