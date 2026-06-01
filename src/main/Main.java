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
            new MainUi().showMainScreen();
        } catch (Exception e) {
            System.out.println("오류 발생 " + e.getMessage());
        }
    }
}
