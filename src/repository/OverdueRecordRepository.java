package repository;

import java.sql.Connection;
import java.sql.SQLException;
import dto.OverdueRecordDto;
import java.util.List;

public interface OverdueRecordRepository {
    // 연체자인지 확인 (납부하지 않은 연체료 존재 여부) - 일반 대출
    boolean hasUnpaidOverdue(Connection conn, int user_id);
    // 새 연체기록 저장 - 연체 처리
    void insertOverdueRecord(Connection conn) throws SQLException;
    // 연체료 업데이트 - 연체 처리 (하루에 한 번 실행되도록 함)
    void updateDailyFineAmount(Connection conn) throws SQLException;
    List<OverdueRecordDto> getOverdue() throws SQLException;
    void updateStatus(int overdueId) throws SQLException;
}
