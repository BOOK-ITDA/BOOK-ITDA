package repository;
import dto.BranchTransferDto;

import dto.SmartLibReqDto;
import java.sql.Connection;
import java.util.List;

import java.sql.SQLException;
import java.util.List;

public interface SmartLibReqRepository {

    // 스마트도서관 대출 신청 INSERT (회원)
    int insertSmartLibReq(Connection conn, SmartLibReqDto dto);

    // 스마트도서관 대출 신청 목록 조회 SELECT (회원 - 대시보드)
    List<SmartLibReqDto> findByUserId(Connection conn, int user_id);

    List<SmartLibReqDto> getSmartReq() throws SQLException;
    void updateStatus(int smartReqId) throws SQLException;

    boolean hasAvailableRequest(Connection conn, int userId, int bookId, int libraryId);
}
