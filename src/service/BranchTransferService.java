package service;

import dao.BranchTransferDao;
import dto.BranchTransferDto;
import repository.BranchTransferRepository;

import java.sql.SQLException;
import java.util.Optional;

public class BranchTransferService {

    private final BranchTransferRepository branchTransferReq = new BranchTransferDao();

    public int requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) throws SQLException {
        BranchTransferDto bt = new BranchTransferDto(userId, bookId, holdingLibId, pickupLibId, "PROCESSING");
        return branchTransferReq.requestBranchTransfer(bt);
    }

    public int updateStatus(int transferReqId, String status) throws SQLException {
        return branchTransferReq.updateStatus(transferReqId, status);
    }

    public void getMyBranchTransfers(int userId) throws SQLException {
        branchTransferReq.findByUserId(userId);
    }
}