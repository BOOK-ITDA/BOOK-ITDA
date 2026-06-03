package repository;

public interface StaffRepository {
    // config.properties의 staff.password와 입력값 비교
    boolean checkPassword(String inputPassword);
}
