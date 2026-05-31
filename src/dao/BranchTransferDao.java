package dao;

import dto.BranchTransferDto;
import repository.BranchTransferRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//1. 분관대출 신청(회원) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(INSERT)
//분관대출신청 dao -> insert만, 실제로 분관대출신청기록을 만드는데 필요한 모든것(도서관 선택+분관신청)은 service에서
//2. 분관대출 신청 상태 변경(사서) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(UPDATE)
//3. 분관대출 목록 조회(회원-대시보드) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(SELECT)

public class BranchTransferDao implements BranchTransferRepository {
    //도서 상태 확인(소장 테이블 dao) -> 연체 여부 확인(연체 기록 테이블 dao) -> 도서관 목록 확인 및 선택(도서관 테이블 dao)
    // -> 분관대출 신청기록 생성 insert(여기서) -> 소장 테이블 상태 변경(reserved, 소장 테이블 dao)
    //이 부분 insert 테스트 완료
    @Override
    public int requestBranchTransfer(Connection conn, int userId, int bookId, int holdingLibId, int pickupLibId) throws SQLException {
        String insertSql =
                "INSERT INTO BRANCH_TRANSFER_REQUEST " +
                        "(user_id, book_id, holding_lib_id, pickup_lib_id, status) " +
                        "VALUES (?, ?, ?, ?, 'PROCESSING')";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.setInt(3, holdingLibId);
            pstmt.setInt(4, pickupLibId);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // 생성된 ID 반환
                }
            }
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("분관대출 신청 중 오류 발생", e);
        }
    }

    @Override
    public List<BranchTransferDto> getBranchTransfer() throws SQLException {
        //여기서 사서가 분관대출신청목록을 조회 -> 여기에 분관대출신청건 아이디 조회 -> 아래 updateStatus dao에서 아이디를 입력하거나 넘겨줌(이건 서비스에서)
        //테스트 완료
        String sql =
                "SELECT btr.transf_req_id, btr.user_id, btr.book_id, b.name AS book_name, " +
                        "btr.holding_lib_id, hl.name AS holding_lib_name, btr.pickup_lib_id, pl.name AS pickup_lib_name, btr.status " +
                        "FROM BRANCH_TRANSFER_REQUEST btr " +
                        "JOIN BOOK b ON btr.book_id = b.book_id " +
                        "JOIN LIBRARY hl ON btr.holding_lib_id = hl.library_id " +
                        "JOIN LIBRARY pl ON btr.pickup_lib_id = pl.library_id";

        List<BranchTransferDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BranchTransferDto bt = new BranchTransferDto(
                        rs.getInt("transf_req_id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("holding_lib_id"),
                        rs.getString("holding_lib_name"),
                        rs.getInt("pickup_lib_id"),
                        rs.getString("pickup_lib_name"),
                        rs.getString("status")
                );
                list.add(bt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("분관대출 신청 목록 조회 중 오류 발생", e);
        }

        return list;
    }

    @Override
    //사서 분관대출 상태 변경
    //테스트완료
    public void updateStatus(int transferReqId) throws SQLException {
        String checkSql = "SELECT status FROM BRANCH_TRANSFER_REQUEST WHERE transf_req_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, transferReqId);
            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString("status").equals("AVAILABLE")) {
                        System.out.println("이미 처리된 신청입니다.");
                        return;
                    }
                } else {
                    System.out.println("존재하지 않는 신청ID입니다.");
                    return;
                }
            }
        }

        String sql = "UPDATE BRANCH_TRANSFER_REQUEST SET status = 'AVAILABLE' WHERE transf_req_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transferReqId);
            pstmt.executeUpdate();
            System.out.println("상태 변경 완료");
        }
    }

    @Override
    public List<BranchTransferDto> findByUserId(int userId) throws SQLException { //분관대출기록 조회(회원)
        //테스트 완료
        String sql =
                "SELECT bt.transf_req_id,  bt.user_id, bt.book_id, b.name AS book_name, " +
                        "bt.holding_lib_id, hl.name AS holding_lib_name,  " +
                        "bt.pickup_lib_id, pl.name AS pickup_lib_name, bt.status " +
                        "FROM BRANCH_TRANSFER_REQUEST bt " +
                        "JOIN BOOK b ON bt.book_id = b.book_id " +
                        "JOIN LIBRARY hl ON bt.holding_lib_id = hl.library_id " +
                        "JOIN LIBRARY pl ON bt.pickup_lib_id = pl.library_id " +
                        "WHERE bt.user_id = ?";

        List<BranchTransferDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BranchTransferDto bt = new BranchTransferDto(
                            rs.getInt("transf_req_id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getString("book_name"),
                            rs.getInt("holding_lib_id"),
                            rs.getString("holding_lib_name"),
                            rs.getInt("pickup_lib_id"),
                            rs.getString("pickup_lib_name"),
                            rs.getString("status")

                    );
                    list.add(bt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("분관대출 목록 조회 중 오류 발생", e);
        }

        return list;
    }




}
