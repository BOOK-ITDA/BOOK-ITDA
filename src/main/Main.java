package main;

//일단 지금은 jdbc 연결하고 연결상태 확인하는 코드 넣어놨습니당.

import dao.OverdueRecordDao;
import dao.UserDao;
import database.DatabaseConnector;
import dto.BookDto;
import repository.OverdueRecordRepository;
import service.BookService;
import service.OverdueService;
import ui.MainUi;
import ui.SearchUi;
import ui.StaffUi;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        OverdueRecordRepository overdueRepo = new OverdueRecordDao();
        OverdueService overdueService = new OverdueService(overdueRepo);
        overdueService.startDailyOverdueBatch(); // 자정마다 연체 기록 갱신

        // 전화번호 중복 확인 트리거 생성
        try (Connection conn = DatabaseConnector.getConnection()){
            UserDao userDao = new UserDao();
            userDao.createTriggerIfNotExists(conn);
        } catch (SQLException e) {
            System.out.println("\nDB 연결 실패로 트리거를 생성하지 못했습니다.");
        }
    }
}
