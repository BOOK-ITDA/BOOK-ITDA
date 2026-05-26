package repository;
import java.sql.Connection;

public interface UserRepository {
    // 회원 현재 대출 권수 받아오기 - 일반 대출
    int getLoanCount(Connection conn, int user_id);
    // 현재 대출 권수 1권 증가시키기 - 일반 대출
    void increaseLoanCount(Connection conn, int user_id);
}
