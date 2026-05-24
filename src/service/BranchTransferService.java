package service; // 패키지 확인해주세요

import dto.BranchTransfer;
import repository.BranchTransferRepository;
import java.util.List;
import java.util.Optional;

public class BranchTransferService {

    private final BranchTransferRepository branchTransferRepository;

    public BranchTransferService(BranchTransferRepository branchTransferRepository) {
        this.branchTransferRepository = branchTransferRepository;
    }

    public int requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) {
        BranchTransfer bt = new BranchTransfer(userId, bookId, holdingLibId, pickupLibId, "PROCESSING");
        return branchTransferRepository.requestBranchTransfer(bt);
    }

    public int updateStatus(int transferReqId, String status) {
        return branchTransferRepository.updateStatus(transferReqId, status);
    }

    public List<BranchTransfer> getMyBranchTransfers(int userId) {
        return branchTransferRepository.findByUserId(userId);
    }

    public Optional<BranchTransfer> getBranchTransfer(int transferReqId) {
        return branchTransferRepository.findById(transferReqId);
    }
}