package dao;
import dto.LibraryDto;
import repository.LibraryRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDao implements LibraryRepository {
    @Override
    public List<LibraryDto> getLibList() throws SQLException {
        String sql = "SELECT library_id, name, address FROM LIBRARY";
        List<LibraryDto> libList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LibraryDto lib = new LibraryDto(
                        rs.getInt("library_id"),
                        rs.getString("name"),
                        rs.getString("address")
                );
                libList.add(lib);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("도서관 목록 조회 중 오류 발생", e);
        }

        return libList;
    }
}
