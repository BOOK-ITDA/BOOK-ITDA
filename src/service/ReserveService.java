package service;
import dto.ReservationRecordDto;
import repository.*;
import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReserveService {
    // 인터페이스에 의존
    private final CollectionRepository collectionRepository;
    private final OverdueRecordRepository overdueRecordRepository;
    private final ReservationRecordRepository reservationRecordRepository;

    // 생성자를 통한 의존성 주입
    public ReserveService(CollectionRepository collectionRepository, OverdueRecordRepository overdueRecordRepository, ReservationRecordRepository reservationRecordRepository) {
        this.collectionRepository = collectionRepository;
        this.overdueRecordRepository = overdueRecordRepository;
        this.reservationRecordRepository = reservationRecordRepository;
    }

    public int reserveProcess(int user_id, int book_id, int library_id){
        try (Connection conn = DatabaseConnector.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // 도서 상태 확인
                String bookStatus = collectionRepository.getStatus(conn, book_id, library_id);
                if (!"BORROWED".equals(bookStatus)){
                    if ("AVAILABLE".equals(bookStatus))
                        throw new IllegalStateException("예약 불가 : 해당 도서는 대출 가능합니다. 대출 기능을 이용해주세요. ");
                    else if ("RESERVED".equals(bookStatus))
                        throw new IllegalStateException("예약 불가 : 해당 도서는 현재 예약 중입니다. 다음 번에 다시 시도해주세요. ");
                }

                // 연체료 미납 여부 확인
                boolean hasOverdue = overdueRecordRepository.hasUnpaidOverdue(conn, user_id);
                if(hasOverdue){
                    throw new IllegalStateException("예약 불가 : 미납된 연체료가 존재하는 회원입니다.");
                }

                LocalDate reserve_date = LocalDate.now();

                ReservationRecordDto newReserve = new ReservationRecordDto(user_id, book_id, library_id, reserve_date, "PROCESSING");
                int generatedReserveId = reservationRecordRepository.insertReservationRecord(conn, newReserve);

                collectionRepository.updateStatus(conn, book_id, library_id, "RESERVED");

                conn.commit();
                return generatedReserveId;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("롤백 중 에러 발생", ex);
                }
                if ( e instanceof IllegalStateException ) {
                    throw (IllegalStateException) e;
                }
                throw new RuntimeException("예약 처리 중 데이터베이스 오류 발생: " + e.getMessage(),e);

            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패", e);
        }
    } // reserveProcess 메서드 종료
}
