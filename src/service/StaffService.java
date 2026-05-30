package service;

import dao.BranchTransferDao;
import dao.OverdueRecordDao;
import dao.SmartLibReqDao;
import dto.*;
import dao.UserDao;
import repository.BranchTransferRepository;
import repository.OverdueRecordRepository;
import repository.SmartLibReqRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class StaffService {
    //사서와 관련된 기능
    //1/. 전체 회원 조회(비밀번호 제외)
    //2/. 분관대출 신청 상태 변경 -> BranchTransfer -> 조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정
    //3. 스마트도서관 신청 상태 변경 -> SmartLib -> 조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정
    //4. 연체 기록 상태 변경 -> OverdueRecord ->  조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정

    private UserRepository userDao = new UserDao(); //사서 전용 회원 목록 조회

    public List<UserDto> getAllUsers() throws SQLException {
        return userDao.findUserAll(); //userList 반환 -> staffUI에서 출력하는 코드 추가
    }


    private BranchTransferRepository branchTransferDao = new BranchTransferDao();

    public List<BranchTransferDto> getBranchTransfer() throws SQLException {
        return branchTransferDao.getBranchTransfer();
    }

    public void updateBranchTransferStatus(int transferReqId) throws SQLException {
        branchTransferDao.updateStatus(transferReqId);
    }


    private SmartLibReqRepository  smartLibReqDao = new SmartLibReqDao();

    public List<SmartLibReqDto> getSmartReq() throws SQLException {
        return smartLibReqDao.getSmartReq();
    }

    public void updateSmartLibReqStatus(int smartReqId) throws SQLException {
        smartLibReqDao.updateStatus(smartReqId);
    }


    private OverdueRecordRepository overdueRecordDao = new OverdueRecordDao();

    public List<OverdueRecordDto> getOverdue() throws SQLException {
        return overdueRecordDao.getOverdue();
    }

    public void updateOverdueStatus(int overdueId) throws SQLException {
        overdueRecordDao.updateStatus(overdueId);
    }
}
