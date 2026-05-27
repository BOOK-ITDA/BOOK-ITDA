package repository;
import java.sql.Connection;
import dto.CollectionDto.BookStatus;
public interface CollectionRepository {

    // 도서 상태 받아오기 - 일반 대출
    BookStatus getStatus(Connection conn, int book_id, int library_id);
    // 도서 상태 변경 - 일반 대출
    void updateStatus(Connection conn, int book_id, int library_id, BookStatus status);

}
