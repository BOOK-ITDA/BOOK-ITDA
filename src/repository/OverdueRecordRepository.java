package repository;
import dto.OverdueRecordDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OverdueRecordRepository {
    // 연체자인지 확인 (납부하지 않은 연체료 존재 여부) - 일반 대출
    boolean hasUnpaidOverdue(Connection conn, int user_id);
    List<OverdueRecordDto> getOverdue() throws SQLException;
    void updateStatus(int overdueId) throws SQLException;
}
