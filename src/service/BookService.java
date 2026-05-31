package service;

import dao.BookDao;
import dto.BookDto;
import repository.BookRepository;
import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    private final BookRepository bookDao = new BookDao();

    public List<BookDto> search(String keyword) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {

            List<BookDto> byTitle  = bookDao.findByTitle(conn, keyword);
            List<BookDto> byAuthor = bookDao.findByAuthor(conn, keyword);
            List<BookDto> byGenre  = bookDao.findByGenre(conn, keyword);

            List<BookDto> merged = new ArrayList<>();
            List<String> seen = new ArrayList<>(); // book_id로 중복 체크

            for (BookDto dto : byTitle) {
                String key = String.valueOf(dto.getBook_id());
                if (!seen.contains(key)) { merged.add(dto); seen.add(key); }
            }
            for (BookDto dto : byAuthor) {
                String key = String.valueOf(dto.getBook_id());
                if (!seen.contains(key)) { merged.add(dto); seen.add(key); }
            }
            for (BookDto dto : byGenre) {
                String key = String.valueOf(dto.getBook_id());
                if (!seen.contains(key)) { merged.add(dto); seen.add(key); }
            }

            return merged;
        }
    }
}