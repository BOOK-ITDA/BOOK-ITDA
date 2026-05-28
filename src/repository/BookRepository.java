package repository;

import dto.BookDto;
import dto.BookSearchResultDto;
import java.util.List;
import java.sql.Connection;

public interface BookRepository {
    // 제목으로 검색
    List<BookSearchResultDto> findByTitle(Connection conn, String keyword);
    // 저자로 검색
    List<BookSearchResultDto> findByAuthor(Connection conn, String keyword);
    // 장르로 검색
    List<BookSearchResultDto> findByGenre(Connection conn, String keyword);
}