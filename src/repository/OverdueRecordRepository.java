package repository;

import dto.OverdueRecordDto;

import java.sql.Connection;
import java.sql.SQLException;
import dto.OverdueRecordDto;
import java.util.List;

public interface OverdueRecordRepository {
    // 연체자인지 확인 (납부하지 않은 연체료 존재 여부) - 일반 대출
    boolean hasUnpaidOverdue(Connection conn, int user_id);

    // 미납 연체 기록만 조회 (is_paid = 0) — 회원 대시보드용
    List<OverdueRecordDto> findUnpaidByUserId(Connection conn, int user_id);

    // 전체 연체 기록 조회 (납부 완료 포함) — 회원 대시보드 전체 이력용
    List<OverdueRecordDto> findAllByUserId(Connection conn, int user_id);

    // 새 연체기록 저장 - 연체 처리
    void insertOverdueRecord(Connection conn) throws SQLException;

    // 연체료 업데이트 - 연체 처리 (하루에 한 번 실행되도록 함)
    void updateDailyFineAmount(Connection conn) throws SQLException;
    List<OverdueRecordDto> getOverdue() throws SQLException;
    void updateStatus(int overdueId) throws SQLException;
}