package dao;
import dto.SmartLibReqDto;
import repository.SmartLibReqRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SmartLibReqDao implements SmartLibReqRepository {

    @Override
    // 스마트도서관 대출 신청 INSERT
    // status는 DB 기본값(PROCESSING) 사용 → SQL에서 컬럼 생략
    public int insertSmartLibReq(Connection conn, SmartLibReqDto dto) {
        String sql = "INSERT INTO smart_lib_request (user_id, book_id, library_id, smart_lib_id) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, dto.getUser_id());
            pstmt.setInt(2, dto.getBook_id());
            pstmt.setInt(3, dto.getLibrary_id());
            pstmt.setInt(4, dto.getSmart_lib_id());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int smt_req_id = generatedKeys.getInt(1);
                    dto.setSmt_req_id(smt_req_id);  // DTO에 생성된 ID 세팅
                    return smt_req_id;
                } else {
                    throw new SQLException("생성된 ID를 가져올 수 없습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("스마트도서관 대출 신청 중 DB 오류 발생", e);
        }
    }

    @Override
    // 스마트도서관 대출 신청 목록 조회 SELECT (회원 대시보드용)
    // book 테이블 JOIN → 도서명
    // smart_library 테이블 JOIN → 스마트도서관명
    public List<SmartLibReqDto> findByUserId(Connection conn, int user_id) {
        String sql = "SELECT r.smt_req_id, r.user_id, r.book_id, r.library_id, r.smart_lib_id, " +
                     "       r.status, b.name AS book_name, s.name AS smart_lib_name " +
                     "FROM smart_lib_request r " +
                     "JOIN book b ON r.book_id = b.book_id " +
                     "JOIN smart_library s ON r.smart_lib_id = s.smart_lib_id " +
                     "WHERE r.user_id = ?";

        List<SmartLibReqDto> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SmartLibReqDto dto = new SmartLibReqDto(
                            rs.getInt("smt_req_id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getInt("library_id"),
                            rs.getInt("smart_lib_id"),
                            rs.getString("status"),
                            rs.getString("book_name"),
                            rs.getString("smart_lib_name")
                    );
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("스마트도서관 신청 목록 조회 중 DB 오류 발생", e);
        }

        return list;
    }
}
