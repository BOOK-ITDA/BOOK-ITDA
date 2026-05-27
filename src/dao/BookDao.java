package dao;

import dto.BookDto;
import dto.BookSearchResultDto;
import repository.BookRepository;
import database.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao implements BookRepository {

    // ─────────────────────────────────────────────
    // 공통: ResultSet → BookSearchResultDto 변환
    // 3개 함수 모두 SELECT 컬럼이 동일하므로 중복 제거용 private 메서드
    // ─────────────────────────────────────────────
    private List<BookSearchResultDto> executeSearch(String sql, String keyword) {
        List<BookSearchResultDto> results = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";  // LIKE 패턴 조립

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, likeKeyword);  // WHERE 조건 1개만 바인딩

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new BookSearchResultDto(
                            rs.getInt("book_id"),
                            rs.getString("name"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("genre"),
                            rs.getString("library_name"),
                            rs.getString("status")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // 예외 발생 시 빈 리스트 반환 (호출부에서 빈 리스트 처리)
        }

        return results;
    }

    // ─────────────────────────────────────────────
    // 1. 제목으로 검색
    // WHERE b.name LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookSearchResultDto> findByTitle(String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "       l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.name LIKE ?";

        return executeSearch(sql, keyword);
    }

    // ─────────────────────────────────────────────
    // 2. 저자로 검색
    // WHERE b.author LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookSearchResultDto> findByAuthor(String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "       l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.author LIKE ?";

        return executeSearch(sql, keyword);
    }

    // ─────────────────────────────────────────────
    // 3. 장르로 검색
    // WHERE b.genre LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookSearchResultDto> findByGenre(String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "       l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.genre LIKE ?";

        return executeSearch(sql, keyword);
    }
}
