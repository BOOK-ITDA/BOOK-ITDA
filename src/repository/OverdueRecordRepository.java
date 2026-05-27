package repository;
import java.sql.Connection;

public interface OverdueRecordRepository {
    // 연체자인지 확인 (납부하지 않은 연체료 존재 여부) - 일반 대출
    boolean hasUnpaidOverdue(Connection conn, int user_id);
}
