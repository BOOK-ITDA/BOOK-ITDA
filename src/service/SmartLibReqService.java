package service;

import dao.SmartLibReqDao;
import repository.SmartLibReqRepository;

import java.sql.SQLException;

public class SmartLibReqService {
    private final SmartLibReqRepository smartLibReq = new SmartLibReqDao();

    public int updateStatus(int smartReqId, String status) throws SQLException {
        return smartLibReq.updateStatus(smartReqId, status);
    }
}
