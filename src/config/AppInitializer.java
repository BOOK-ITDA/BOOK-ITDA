package config;

import dao.BranchTransferDao;
import dao.OverdueRecordDao;
import dao.UserDao;
import database.DatabaseConnector;
import service.OverdueService;

import java.sql.Connection;
import java.sql.SQLException;

public class AppInitializer {
    public static void init() {
        // 전화번호 중복 확인 트리거 생성
        try (Connection conn = DatabaseConnector.getConnection()) {
            //전화번호 중복 방지 트리거
            UserDao userDao = new UserDao();
            userDao.createTriggerIfNotExists(conn);

            //분관대출 신청 트리거
            BranchTransferDao branchTransferDao = new BranchTransferDao();
            branchTransferDao.createBranchTransferTrigger(conn);
        } catch (SQLException e) {
            throw new RuntimeException("초기화 실패", e);
        }

        // 연체 배치 시작
        OverdueService overdueService = new OverdueService(new OverdueRecordDao());
        overdueService.startDailyOverdueBatch();
    }
}
