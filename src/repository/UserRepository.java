package repository;
import dto.UserDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    //회원 조회(사서)
    List<UserDto> findUserAll() throws SQLException;
    // 회원 현재 대출 권수 받아오기 - 일반 대출
    int getLoanCount(Connection conn, int user_id);
    // 현재 대출 권수 1권 증가시키기 - 일반 대출
    void increaseLoanCount(Connection conn, int user_id);
    // 현재 대출 권수 1권 감소시키기 - 일반 반납
    void decreaseLoanCount(Connection conn, int user_id);
    // 회원 데이터 저장하기 - 회원 가입
    int insertUser(Connection conn, UserDto userDto);
    //회원 로그인
    int login(int inputId, String inputPw) throws SQLException;
    // 전화번호 중복 확인 트리거
    void createTriggerIfNotExists(Connection conn);
}
