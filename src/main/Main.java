package main;

//일단 지금은 jdbc 연결하고 연결상태 확인하는 코드 넣어놨습니당.

import database.DatabaseConnector;


import dto.BookDto;
import service.BookService;
import ui.MainUi;
import ui.SearchUi;
import ui.StaffUi;



import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            System.out.println("DB 연결");

/*            // SearchUi 테스트
            SearchUi searchUi = new SearchUi();
            searchUi.showSearchScreen();

            StaffUi staffUi = new StaffUi();

            // 사서 관리 메뉴 화면 실행
            staffUi.showStaffScreen();*/
            new MainUi().showMainScreen();


            conn.close();
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }
}
