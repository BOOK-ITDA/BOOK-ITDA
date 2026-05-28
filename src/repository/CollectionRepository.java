package repository;
import java.sql.Connection;
public interface CollectionRepository {

    // 도서 상태 받아오기 - 일반 대출
    String getStatus(Connection conn, int book_id, int library_id);
    // 도서 상태 변경 - 일반 대출
    void updateStatus(Connection conn, int book_id, int library_id, String status);

}
