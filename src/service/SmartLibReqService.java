package service;

import database.DatabaseConnector;
import dto.SmartLibReqDto;
import dto.SmartLibraryDto;
import repository.CollectionRepository;
import repository.OverdueRecordRepository;
import repository.SmartLibReqRepository;
import repository.SmartLibraryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SmartLibReqService {

    private final SmartLibReqRepository smartLibReqRepository;
    private final SmartLibraryRepository smartLibraryRepository;
    private final OverdueRecordRepository overdueRecordRepository;
    private final CollectionRepository collectionRepository;

    // 생성자를 통한 의존성 주입 (BranchTransferService와 동일한 패턴)
    public SmartLibReqService(SmartLibReqRepository smartLibReqRepository,
                              SmartLibraryRepository smartLibraryRepository,
                              OverdueRecordRepository overdueRecordRepository,
                              CollectionRepository collectionRepository) {
        this.smartLibReqRepository = smartLibReqRepository;
        this.smartLibraryRepository = smartLibraryRepository;
        this.overdueRecordRepository = overdueRecordRepository;
        this.collectionRepository = collectionRepository;
    }

    // 신청 가능한 스마트도서관 목록 조회 (UI에서 목록 출력용)
    public List<SmartLibraryDto> getAvailableSmartLibList(Connection conn) {
        return smartLibraryRepository.findAvailable(conn);
    }

    // 스마트도서관 대출 신청 처리 (트랜잭션)
    // userId     : 신청 회원 ID
    // bookId     : 신청 도서 ID
    // libraryId  : 도서 소장 도서관 ID (COLLECTION 상태 변경에 사용)
    // smartLibId : 수령할 스마트도서관 ID
    public void requestSmartLibReq(int userId, int bookId, int libraryId, int smartLibId) throws SQLException {

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. 연체 여부 확인
                boolean hasOverdue = overdueRecordRepository.hasUnpaidOverdue(conn, userId);
                if (hasOverdue) {
                    System.out.println("미납 연체가 있어 신청이 불가합니다.");
                    conn.rollback();
                    return;
                }

                // 2. 스마트도서관 신청 기록 INSERT
                // status는 DB 기본값 'PROCESSING' 자동 저장
                SmartLibReqDto dto = new SmartLibReqDto(userId, bookId, libraryId, smartLibId);
                smartLibReqRepository.insertSmartLibReq(conn, dto);

                // 3. 선택한 스마트도서관 book_count + 1
                smartLibraryRepository.increaseBookCount(conn, smartLibId);

                // 4. 소장 테이블 도서 상태 → RESERVED
                collectionRepository.updateStatus(conn, bookId, libraryId, "RESERVED");

                conn.commit();
                System.out.println("스마트 도서관 대출 신청이 완료되었습니다.");
                System.out.println("대출 가능 여부는 대시보드에서 확인하실 수 있습니다.");

            } catch (Exception e) {
                conn.rollback();
                throw new SQLException("스마트도서관 대출 신청 중 오류 발생", e);
            }
        }
    }
}
