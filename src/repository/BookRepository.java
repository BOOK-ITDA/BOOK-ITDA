package repository;

import dto.BookDto;
import java.util.List;
import java.sql.Connection;

public interface BookRepository {
    //제목
    List<BookDto> findByTitle(Connection conn, String keyword);
    //저자
    List<BookDto> findByAuthor(Connection conn, String keyword);
    //장르
    List<BookDto> findByGenre(Connection conn, String keyword);
}