package main;

//일단 지금은 jdbc 연결하고 연결상태 확인하는 코드 넣어놨습니당.

import dao.OverdueRecordDao;
import database.DatabaseConnector;
import dto.BookDto;
import repository.OverdueRecordRepository;
import service.BookService;
import service.OverdueService;
import ui.MainUi;
import ui.SearchUi;
import ui.StaffUi;
import java.sql.Connection;
import java.util.List;
import config.AppInitializer;

public class Main {
    public static void main(String[] args) {
        AppInitializer.init();
    }
}
