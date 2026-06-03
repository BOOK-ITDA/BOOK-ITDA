package main;

//일단 지금은 jdbc 연결하고 연결상태 확인하는 코드 넣어놨습니당.

import database.DatabaseConnector;
import dto.BookDto;
import repository.OverdueRecordRepository;
import service.BookService;
import service.OverdueService;
import dao.*;
import dto.SmartLibraryDto;
import service.StaffService;
import session.Session;
import ui.MainUi;
import service.SmartLibReqService;
import ui.DashBoardUi;
import ui.SearchUi;
import ui.StaffUi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            System.out.println("DB 연결");

/*            // SearchUi 테스트
            SearchUi searchUi = new SearchUi();
            searchUi.showSearchScreen();*/

            // userId 1번 (김민준)으로 대시보드 테스트
/*            int testUserId = 1;
            DashBoardUi dashBoardUi = new DashBoardUi(testUserId);
            dashBoardUi.showDashBoardScreen();*/

/*            StaffUi staffUi = new StaffUi();

            // 사서 관리 메뉴 화면 실행
            staffUi.showStaffScreen();*/
/*            new MainUi().showMainScreen();*/


            conn.close();
            new MainUi().showMainScreen();
        } catch (Exception e) {
            System.out.println("오류 발생 " + e.getMessage());
        }
    }
}
