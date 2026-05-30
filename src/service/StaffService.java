package service;

import dao.*;
import dto.*;
import repository.*;

import java.sql.SQLException;
import java.util.List;

public class StaffService {
    //사서와 관련된 기능
    //1/. 전체 회원 조회(비밀번호 제외)
    //2/. 분관대출 신청 상태 변경 -> BranchTransfer -> 조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정
    //3. 스마트도서관 신청 상태 변경 -> SmartLib -> 조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정
    //4. 예약 기록 상태 변경
    //4. 연체 기록 상태 변경 -> OverdueRecord ->  조회 및 상태 업데이트 별개로 분리 및 ui에서 합칠 예정

    private final UserRepository userRepository;
    private final BranchTransferRepository branchTransferRepository;
    private final SmartLibReqRepository smartLibReqRepository;
    private final ReservationRecordRepository reservationRecordRepository;
    private final OverdueRecordRepository overdueRecordRepository;

    public StaffService(UserRepository userRepository,
                        BranchTransferRepository branchTransferRepository,
                        SmartLibReqRepository smartLibReqRepository,
                        ReservationRecordRepository reservationRecordRepository,
                        OverdueRecordRepository overdueRecordRepository) {
        this.userRepository = userRepository;
        this.branchTransferRepository = branchTransferRepository;
        this.smartLibReqRepository = smartLibReqRepository;
        this.reservationRecordRepository = reservationRecordRepository;
        this.overdueRecordRepository = overdueRecordRepository;
    }

    public List<UserDto> getAllUsers() throws SQLException {
        return userRepository.findUserAll();
    }

    public List<BranchTransferDto> getBranchTransfer() throws SQLException {
        return branchTransferRepository.getBranchTransfer();
    }

    public void updateBranchTransferStatus(int transferReqId) throws SQLException {
        branchTransferRepository.updateStatus(transferReqId);
    }

    public List<SmartLibReqDto> getSmartReq() throws SQLException {
        return smartLibReqRepository.getSmartReq();
    }

    public void updateSmartLibReqStatus(int smartReqId) throws SQLException {
        smartLibReqRepository.updateStatus(smartReqId);
    }

    public List<ReservationRecordDto> getReservation() throws SQLException {
        return reservationRecordRepository.getReservation();
    }

    public void updateReservationStatus(int reservationId) throws SQLException {
        reservationRecordRepository.updateStatus(reservationId);
    }

    public List<OverdueRecordDto> getOverdue() throws SQLException {
        return overdueRecordRepository.getOverdue();
    }

    public void updateOverdueStatus(int overdueId) throws SQLException {
        overdueRecordRepository.updateStatus(overdueId);
    }
}
