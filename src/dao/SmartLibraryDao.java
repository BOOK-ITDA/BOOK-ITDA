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
    public List<SmartLibraryDto> getSmartLibList() throws SQLException {
        String sql = "SELECT smart_lib_id, name, address FROM SMART_LIBRARY";
        List<SmartLibraryDto> smartlibList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SmartLibraryDto lib = new SmartLibraryDto(
                        rs.getInt("smart_lib_id"),
                        rs.getString("name"),
                        rs.getString("address")
                );
                smartlibList.add(lib);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("도서관 목록 조회 중 오류 발생", e);
        }

        return smartlibList;
    }

}
