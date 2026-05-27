package repository;

import dto.BookDto;
import dto.BookSearchResultDto;
import java.util.List;

public interface BookRepository {
    // 제목으로 검색
    List<BookSearchResultDto> findByTitle(String keyword);

    // 저자로 검색
    List<BookSearchResultDto> findByAuthor(String keyword);

    // 장르로 검색
    List<BookSearchResultDto> findByGenre(String keyword);
}