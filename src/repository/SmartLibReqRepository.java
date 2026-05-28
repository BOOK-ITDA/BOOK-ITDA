package repository;
import dto.SmartLibReqDto;

import java.sql.SQLException;

public interface SmartLibReqRepository {
    int updateStatus(int smartReqId, String status) throws SQLException;
}
