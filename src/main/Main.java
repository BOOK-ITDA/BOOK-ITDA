package main;

//여기에서 gui 등 기본 화면으로 사용할거에용!
//일단 지금은 jdbc 연결하고 연결상태 확인하는 코드 넣어놨습니당.

import database.DatabaseConnector;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            System.out.println("DB 연결");
            conn.close();
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }
}
