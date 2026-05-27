package dao;
import dto.SmartLibReqDto;
import repository.SmartLibReqRepository;
import database.DatabaseConnector;
import java.sql.*;

public class SmartLibReqDao implements SmartLibReqRepository {
    @Override
    public int updateStatus(int smartReqId, String status) throws SQLException {
        String sql =
                "UPDATE SMART_LIB_REQUEST SET status = ? " +
                        "WHERE smt_req_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, smartReqId);
            int rows = pstmt.executeUpdate();
            System.out.println("상태 변경 완료: " + status);
            return rows;
        }

    }
}
