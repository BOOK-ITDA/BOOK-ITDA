package service; // 패키지 확인해주세요

import dto.BranchTransferDto;
import repository.BranchTransferRepository;
import java.util.Optional;

public class BranchTransferService {

    private final BranchTransferRepository branchTransferRepository;

    public BranchTransferService(BranchTransferRepository branchTransferRepository) {
        this.branchTransferRepository = branchTransferRepository;
    }

    public int requestBranchTransfer(int userId, int bookId, int holdingLibId, int pickupLibId) {
        BranchTransferDto bt = new BranchTransferDto(userId, bookId, holdingLibId, pickupLibId, "PROCESSING");
        return branchTransferRepository.requestBranchTransfer(bt);
    }

    public int updateStatus(int transferReqId, String status) {
        return branchTransferRepository.updateStatus(transferReqId, status);
    }

    public void getMyBranchTransfers(int userId) {
        branchTransferRepository.findByUserId(userId);
    }

    public Optional<BranchTransferDto> getBranchTransfer(int transferReqId) {
        return branchTransferRepository.findById(transferReqId);
    }
}