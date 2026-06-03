package dao;

import repository.StaffRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StaffDao implements StaffRepository {

    @Override
    public boolean checkPassword(String inputPassword) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
            String correctPassword = props.getProperty("staff.password");

            if (correctPassword == null) {
                throw new RuntimeException("config.properties에 staff.password가 설정되지 않았습니다.");
            }

            return correctPassword.equals(inputPassword);

        } catch (IOException e) {
            throw new RuntimeException("config.properties 파일을 읽는 중 오류 발생", e);
        }
    }
}
