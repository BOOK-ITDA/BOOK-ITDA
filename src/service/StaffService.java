package service;

public class StaffService {

    private static final String STAFF_PASSWORD = "staff1234"; // 팀원들과 협의해서 정하기

    // 직원 비밀번호 검증
    public boolean verifyPassword(String inputPassword) {
        return STAFF_PASSWORD.equals(inputPassword);
    }
}