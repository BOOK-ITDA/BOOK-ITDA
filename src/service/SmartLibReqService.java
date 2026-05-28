package service;

import dao.SmartLibReqDao;
import repository.SmartLibReqRepository;

import java.sql.SQLException;

public class SmartLibReqService {
    private final SmartLibReqRepository smartLibReq = new SmartLibReqDao();

    public void updateStatus(int smartReqId) throws SQLException {
    }
}
