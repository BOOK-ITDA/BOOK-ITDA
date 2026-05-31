package dao;

import dto.BookDto;
import repository.BookRepository;
import database.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao implements BookRepository {

    // ─────────────────────────────────────────────
    // 3개 함수 모두 SELECT 컬럼이 동일하므로 중복 제거용 private 메서드
    // ─────────────────────────────────────────────
    private List<BookDto> executeSearch(Connection conn, String sql, String keyword) {
        List<BookDto> results = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, likeKeyword);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new BookDto(
                            rs.getInt("book_id"),
                            rs.getString("name"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("genre"),
                            rs.getInt("library_id"),
                            rs.getString("library_name"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // ─────────────────────────────────────────────
    // 1. 제목으로 검색
    // WHERE b.name LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookDto> findByTitle(Connection conn, String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "l.library_id AS library_id, l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.name LIKE ?";
        return executeSearch(conn, sql, keyword);
    }

    // ─────────────────────────────────────────────
    // 2. 저자로 검색
    // WHERE b.author LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookDto> findByAuthor(Connection conn, String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "l.library_id AS library_id, l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.author LIKE ?";
        return executeSearch(conn, sql, keyword);
    }

    // ─────────────────────────────────────────────
    // 3. 장르로 검색
    // WHERE b.genre LIKE '%keyword%'
    // ─────────────────────────────────────────────
    @Override
    public List<BookDto> findByGenre(Connection conn, String keyword) {
        String sql =
                "SELECT b.book_id, b.name, b.author, b.publisher, b.genre, " +
                        "l.library_id AS library_id, l.name AS library_name, c.status " +
                        "FROM BOOK b " +
                        "JOIN COLLECTION c ON b.book_id = c.book_id " +
                        "JOIN LIBRARY l    ON c.library_id = l.library_id " +
                        "WHERE b.genre LIKE ?";
        return executeSearch(conn, sql, keyword);
    }
}
