package repository;

import dto.BranchTransferDto;

import java.sql.SQLException;
import java.util.Optional;

public interface BranchTransferRepository {
    //분관대출신청과 관련된 기능
    //1. 분관대출 신청(회원) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(INSERT)
    //2. 분관대출 신청 상태 변경(사서) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(UPDATE)
    //3. 분관대출 목록 조회(회원-대시보드) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(SELECT)
    //4. 단건 조회 (신청ID 기준) -> 사서 처리 시 필요

    int requestBranchTransfer(BranchTransferDto bt) throws SQLException;
    int updateStatus(int transferReqId, String status) throws SQLException;
    void findByUserId(int userId) throws SQLException;
    Optional<BranchTransferDto> findById(int transferReqId) throws SQLException;

}
