package service;

import dto.UserDto;
import service.BranchTransferService;
import service.UserService;

import java.sql.SQLException;
import java.util.List;

public class StaffService {
    //사서와 관련된 기능
    //1. 전체 회원 조회(비밀번호 제외) -> User
    //2. 분관대출 신청 상태 변경 -> BranchTransfer(완료)
    //3. 스마트도서관 신청 상태 변경 -> SmartLib
    //4. 연체 기록 상태 변경 -> OverdueRecord


    private final BranchTransferService BTService;
    private final UserService userService;

    public StaffService(BranchTransferService BTService, UserService userService) {
        this.BTService = BTService;
        this.userService = userService;
    }

    public int updateBranchTransferStatus(int transferReqId, String status) {
        return BTService.updateStatus(transferReqId, status);
    }

    public List<UserDto> findUserAll() throws SQLException {
        return userService.findUserAll();
    }
}
