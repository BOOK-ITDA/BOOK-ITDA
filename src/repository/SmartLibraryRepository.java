package repository;
import dto.SmartLibraryDto;

public interface SmartLibraryRepository {

    // 스마트도서관 대출 신청 INSERT (회원)
    int insertSmartLibReq(Connection conn, SmartLibReqDto dto);

    // 스마트도서관 대출 신청 목록 조회 SELECT (회원 - 대시보드)
    List<SmartLibReqDto> findByUserId(Connection conn, int user_id);
}
}
