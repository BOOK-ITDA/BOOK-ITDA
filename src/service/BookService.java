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

    /**
     * 키워드로 도서 검색 (제목 + 저자 + 장르 동시 검색 후 합쳐서 반환)
     */
    public List<BookDto> search(String keyword) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {

            List<BookDto> byTitle  = bookDao.findByTitle(conn, keyword);
            List<BookDto> byAuthor = bookDao.findByAuthor(conn, keyword);
            List<BookDto> byGenre  = bookDao.findByGenre(conn, keyword);

            // 중복 제거용 합치기
            // book_id + library_id 조합을 키로 사용
            List<BookDto> merged = new ArrayList<>();
            List<String> seen = new ArrayList<>(); // "book_id-library_id" 형태로 저장

            for (BookDto dto : byTitle) {
                String key = dto.getBook_id() + "-" + dto.getLibrary_id();
                if (!seen.contains(key)) {
                    merged.add(dto);
                    seen.add(key);
                }
            }
            for (BookDto dto : byAuthor) {
                String key = dto.getBook_id() + "-" + dto.getLibrary_id();
                if (!seen.contains(key)) {
                    merged.add(dto);
                    seen.add(key);
                }
            }
            for (BookDto dto : byGenre) {
                String key = dto.getBook_id() + "-" + dto.getLibrary_id();
                if (!seen.contains(key)) {
                    merged.add(dto);
                    seen.add(key);
                }
            }

            return merged;
        }
    }
}