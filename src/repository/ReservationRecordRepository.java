package repository;

import dto.ReservationRecordDto;
import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;

public interface ReservationRecordRepository {
    // 예약 기록 데이터 삽입 - 일반 예약
    int insertReservationRecord(Connection conn, ReservationRecordDto reservationRecordDto);

    // 예약 기록 조회 - 회원 대시보드용
    List<ReservationRecordDto> findByUserId(Connection conn, int user_id);

    // 예약자 본인 여부 확인 - SearchUi RESERVED 분기용
    // 해당 회원의 이 도서에 대한 AVAILABLE 예약 기록이 있으면 true 반환
    boolean hasAvailableReservation(Connection conn, int userId, int bookId, int libraryId);

}
