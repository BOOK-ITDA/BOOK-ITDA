package service;

import dto.*;
import repository.*;

import java.sql.SQLException;
import java.util.List;

public class StaffService {
    private final UserRepository userDao;
    private final BranchTransferRepository branchTransferDao;
    private final SmartLibReqRepository smartLibReqDao;
    private final ReservationRecordRepository reservationDao;
    private final OverdueRecordRepository overdueRecordDao;

    public StaffService(UserRepository userDao,
                        BranchTransferRepository branchTransferDao,
                        SmartLibReqRepository smartLibReqDao,
                        ReservationRecordRepository reservationDao,
                        OverdueRecordRepository overdueRecordDao) {
        this.userDao = userDao;
        this.branchTransferDao = branchTransferDao;
        this.smartLibReqDao = smartLibReqDao;
        this.reservationDao = reservationDao;
        this.overdueRecordDao = overdueRecordDao;
    }

    public List<UserDto> getAllUsers() throws SQLException {
        return userDao.findUserAll();
    }

    public List<BranchTransferDto> getBranchTransfer() throws SQLException {
        return branchTransferDao.getBranchTransfer();
    }

    public void updateBranchTransferStatus(int transferReqId) throws SQLException {
        branchTransferDao.updateStatus(transferReqId);
    }

    public List<SmartLibReqDto> getSmartReq() throws SQLException {
        return smartLibReqDao.getSmartReq();
    }

    public void updateSmartLibReqStatus(int smartReqId) throws SQLException {
        smartLibReqDao.updateStatus(smartReqId);
    }

    public List<ReservationRecordDto> getReservation() throws SQLException {
        return reservationDao.getReservation();
    }

    public void updateReservationStatus(int reservationId) throws SQLException {
        reservationDao.updateStatus(reservationId);
    }

    public List<OverdueRecordDto> getOverdue() throws SQLException {
        return overdueRecordDao.getOverdue();
    }

    public void updateOverdueStatus(int overdueId) throws SQLException {
        overdueRecordDao.updateStatus(overdueId);
    }
}