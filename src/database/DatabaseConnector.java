package database; //다들 패키지(경로) 이름이나 위치 잘 확인해주세용

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//각자의 로컬에서 만들어준 config.properties를 불러와야해용 -> 프로젝트 루트 폴더에 생성했는지 다시 한번 확인하기!!
//cofig.properties를 읽기 위한 import문을 추가해주세용(근데 어차피 깃허브에서 Pull할거니까 별도로 추가해줄 필요는 없을거 같구 참고해주세용)
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;


public class DatabaseConnector {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("config.properties 파일을 찾을 수 없습니다.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}