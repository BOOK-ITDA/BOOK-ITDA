package repository;
import dto.LoanRecordDto;
import java.util.List;

public interface LoanRecordRepository {
    // 전체 대출 기록 조회 (반납 완료 포함)
    List<LoanRecordDto> findAllByUserId(int user_id);

    // 현재 대출 중인 기록만 조회 (return_date가 NULL인 것만)
    List<LoanRecordDto> findActiveByUserId(int user_id);
}