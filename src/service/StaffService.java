package service;

import service.BranchTransferService;

public class StaffService {
    //사서와 관련된 기능
    //1. 전체 회원 조회(비밀번호 제외) -> User
    //2. 분관대출 신청 상태 변경 -> BranchTransfer(완료)
    //3. 스마트도서관 신청 상태 변경 -> SmartLib
    //4. 예약 도서 상태 변경 -> Reservation

    private final BranchTransferService BTService;

    public StaffService(BranchTransferService BTService) {
        this.BTService = BTService;
    }

    public int updateBranchTransferStatus(int transferReqId, String status) {
        return BTService.updateStatus(transferReqId, status);
    }
}
