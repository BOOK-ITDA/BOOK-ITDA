package repository;
import dto.LoanRecordDto;
import dto.ReservationRecordDto;
import java.sql.Connection;

public interface ReservationRecordRepository {
    // 예약 기록 데이터 삽입 - 일반 예약
    int insertReservationRecord(Connection conn, ReservationRecordDto reservationRecordDto);
}
