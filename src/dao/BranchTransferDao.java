package dao;

import dto.BranchTransferDto;
import repository.BranchTransferRepository;
import database.DatabaseConnector;
import java.sql.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
//1. 분관대출 신청(회원) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(INSERT)
//2. 분관대출 신청 상태 변경(사서) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(UPDATE)
//3. 분관대출 목록 조회(회원-대시보드) -> 콘솔창에 버튼 입력하면 해당 함수가 실행될 수 있도록(SELECT)
//4. 단건 조회 (신청ID 기준) -> 사서 처리 시 필요

public class BranchTransferDao implements BranchTransferRepository {
    @Override
    public int requestBranchTransfer(BranchTransferDto bt) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // 트랜잭션.. 보완 필요

            // 도서 상태 확인
            String checkStatusSql =
                    "SELECT status FROM COLLECTION WHERE book_id = ? AND library_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(checkStatusSql)) {
                pstmt.setInt(1, bt.getBook_id());
                pstmt.setInt(2, bt.getHolding_lib_id());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next() || !"AVAILABLE".equals(rs.getString("status"))) {
                        conn.rollback();
                        System.out.println("대출 불가");
                        return 0;
                    }
                }
            }

            //연체료 미납 확인
            String checkOverdueSql =
                    "SELECT COUNT(*) AS overdue_count " +
                            "FROM OVERDUE_RECORD o " +
                            "JOIN LOAN_RECORD lr ON o.loan_id = lr.loan_id " +
                            "WHERE lr.user_id = ? AND o.is_paid = 0";

            try (PreparedStatement pstmt = conn.prepareStatement(checkOverdueSql)) {
                pstmt.setInt(1, bt.getUser_id());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt("overdue_count") > 0) {
                        conn.rollback();
                        System.out.println("대출 불가: 미납 연체료가 있습니다.");
                        return 0;
                    }
                }
            }

            // 분관대출 신청 insert문
            String insertSql =
                    "INSERT INTO BRANCH_TRANSFER_REQUEST " +
                            "(user_id, book_id, holding_lib_id, pickup_lib_id, status) " +
                            "VALUES (?, ?, ?, ?, 'PROCESSING')";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, bt.getUser_id());
                pstmt.setInt(2, bt.getBook_id());
                pstmt.setInt(3, bt.getHolding_lib_id());
                pstmt.setInt(4, bt.getPickup_lib_id());
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bt.setTransfer_req_id(generatedKeys.getInt(1));
                    }
                }
            }

            // 소장도서관 테이블 상태 변경 -> 이후 소장도서관 테이블 관련 파일 만들어지면 추가 수정 예정
            String updateStatusSql =
                    "UPDATE COLLECTION SET status = 'RESERVED' WHERE book_id = ? AND library_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(updateStatusSql)) {
                pstmt.setInt(1, bt.getBook_id());
                pstmt.setInt(2, bt.getHolding_lib_id());
                pstmt.executeUpdate();
            }

            conn.commit(); // 전부 성공하면 커밋
            System.out.println("분관대출 신청 완료! 신청ID: " + bt.getTransfer_req_id());
            return 1;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("분관대출 신청 중 오류 발생", e);
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // 커넥션 반환 전 원복
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int updateStatus(int transferReqId, String status) {
            String sql =
                    "UPDATE BRANCH_TRANSFER_REQUEST SET status = ? " +
                            "WHERE transf_req_id = ?";

            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, transferReqId);
                int rows = pstmt.executeUpdate();
                System.out.println("상태 변경 완료: " + status);
                return rows;
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
    }

    @Override
    public void findByUserId(int userId) {
        String sql =
                "SELECT bt.transf_req_id, b.name AS book_name, " +
                        "hl.name AS holding_lib_name, pl.name AS pickup_lib_name, " +
                        "bt.status " +
                        "FROM BRANCH_TRANSFER_REQUEST bt " +
                        "JOIN BOOK b ON bt.book_id = b.book_id " +
                        "JOIN LIBRARY hl ON bt.holding_lib_id = hl.library_id " +
                        "JOIN LIBRARY pl ON bt.pickup_lib_id = pl.library_id " +
                        "WHERE bt.user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("신청ID: " + rs.getInt("transf_req_id"));
                    System.out.println("책 이름: " + rs.getString("book_name"));
                    System.out.println("소장도서관: " + rs.getString("holding_lib_name"));
                    System.out.println("수령도서관: " + rs.getString("pickup_lib_name"));
                    System.out.println("상태: " + rs.getString("status"));
                    System.out.println("---");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<BranchTransferDto> findById(int transferReqId) {
        String sql =
                "SELECT transf_req_id, user_id, book_id, holding_lib_id, pickup_lib_id, status " +
                        "FROM BRANCH_TRANSFER_REQUEST " +
                        "WHERE transf_req_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transferReqId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new BranchTransferDto(
                            rs.getInt("transf_req_id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getInt("holding_lib_id"),
                            rs.getInt("pickup_lib_id"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }
}
