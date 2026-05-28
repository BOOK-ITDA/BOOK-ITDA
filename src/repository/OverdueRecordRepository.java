package repository;

import dto.OverdueRecordDto;
import java.sql.Connection;
import java.util.List;

public interface OverdueRecordRepository {

    // 기존 메서드 — 보현 작성, 그대로 유지
    boolean hasUnpaidOverdue(Connection conn, int user_id);

    // 미납 연체 기록만 조회 (is_paid = 0) — 회원 대시보드용
    List<OverdueRecordDto> findUnpaidByUserId(Connection conn, int user_id);

    // 전체 연체 기록 조회 (납부 완료 포함) — 회원 대시보드 전체 이력용
    List<OverdueRecordDto> findAllByUserId(Connection conn, int user_id);
}