package dao;
import dto.BranchTransferDto;
import dto.SmartLibReqDto;
import repository.SmartLibReqRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SmartLibReqDao implements SmartLibReqRepository {
    @Override
    public List<SmartLibReqDto> getSmartReq() throws SQLException { //사서 -> 스마트도서관 신청 목록 조회
        //여기서 사서가 스마트도서관신청목록을 조회 -> 여기에 스마트도서관신청건 아이디 조회 -> 아래 updateStatus dao에서 아이디를 입력하거나 넘겨줌(이건 서비스에서)
        //테스트 완료(메인에서)
        String sql =
                "SELECT slb.smt_req_id, slb.user_id, slb.book_id, b.name AS book_name, " +
                        "slb.library_id, lb.name, slb.smart_lib_id, sl.name AS smart_lib_name, slb.status " +
                        "FROM SMART_LIB_REQUEST slb " +
                        "JOIN BOOK b ON slb.book_id = b.book_id " +
                        "JOIN LIBRARY lb ON slb.library_id=lb.library_id " +
                        "JOIN SMART_LIBRARY sl ON slb.smart_lib_id=sl.smart_lib_id";

        List<SmartLibReqDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SmartLibReqDto st = new SmartLibReqDto(
                        rs.getInt("smt_req_id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("library_id"),
                        rs.getString("name"),        // lb.name (별칭 없음)
                        rs.getInt("smart_lib_id"),
                        rs.getString("smart_lib_name"),
                        rs.getString("status")
                );
                list.add(st);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("스마트도서관 신청 목록 조회 중 오류 발생", e);
        }

        return list;
    }

    @Override
    public void updateStatus(int smartReqId) throws SQLException {
        //스마트도서관 신청 상태 변경(사서)
        //메인에서 테스트 완료

        String sql =
                "UPDATE SMART_LIB_REQUEST SET status = 'AVAILABLE' " +
                        "WHERE smt_req_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, smartReqId);
            int rows = pstmt.executeUpdate();
            System.out.println("상태 변경 완료");
        }
    }
}
