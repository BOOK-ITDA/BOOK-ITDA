package dao;

import dto.LibraryDto;
import dto.SmartLibraryDto;
import repository.SmartLibraryRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SmartLibraryDao implements SmartLibraryRepository {

    @Override
    // 신청 가능한 스마트도서관 목록 조회
    // 조건 : book_count < book_capacity (용량 여유 있는 곳만)
    public List<SmartLibraryDto> findAvailable(Connection conn) {
        String sql = "SELECT smart_lib_id, name, address, book_capacity, book_count " +
                "FROM smart_library " +
                "WHERE book_count < book_capacity";

        List<SmartLibraryDto> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SmartLibraryDto dto = new SmartLibraryDto(
                        rs.getInt("smart_lib_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("book_capacity"),
                        rs.getInt("book_count")
                );
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("스마트도서관 목록 조회 중 DB 오류 발생", e);
        }

        return list;
    }

    @Override
    // 스마트도서관 book_count 1 증가
    public void increaseBookCount(Connection conn, int smart_lib_id) {
        String sql = "UPDATE smart_library SET book_count = book_count + 1 " +
                "WHERE smart_lib_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, smart_lib_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new SQLException("해당 스마트도서관 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("스마트도서관 book_count 업데이트 중 DB 오류 발생", e);
        }
    }

}
