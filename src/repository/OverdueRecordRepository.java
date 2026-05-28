package repository;
import java.sql.Connection;

import dto.OverdueRecordDto;
import java.sql.Connection;
import java.util.List;

public interface OverdueRecordRepository {
    // 연체자인지 확인 (납부하지 않은 연체료 존재 여부) - 일반 대출
    boolean hasUnpaidOverdue(Connection conn, int user_id);

    // 미납 연체 기록만 조회 (is_paid = 0) — 회원 대시보드용
    List<OverdueRecordDto> findUnpaidByUserId(Connection conn, int user_id);

    // 전체 연체 기록 조회 (납부 완료 포함) — 회원 대시보드 전체 이력용
    List<OverdueRecordDto> findAllByUserId(Connection conn, int user_id);
}