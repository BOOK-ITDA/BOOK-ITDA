package repository;
import dto.BranchTransferDto;
import dto.SmartLibReqDto;

import java.sql.SQLException;
import java.util.List;

public interface SmartLibReqRepository {
    List<SmartLibReqDto> getSmartReq() throws SQLException;
    void updateStatus(int smartReqId) throws SQLException;
}
