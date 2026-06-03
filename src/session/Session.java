package session;

public class Session {
    //계속 가지고 있을 로그인 후 회원 아이디
    private static int loggedInUserId = -1;

    //로그인 성공하면 아이디 번호 업데이트
    public static void loginAsUser(int userId) {
        loggedInUserId = userId;
    }

    // 사서 로그인
    public static void loginAsStaff() {
    }

    // 회원 로그아웃
    public static void logout() {
        loggedInUserId = -1;
    }

    //나중에 다른 기능에서 필요할 때 Sesson.getUserId()로 이용하면 됨
    public static int getUserId() { return loggedInUserId; }
    public static boolean isLoggedIn() { return loggedInUserId != -1; }

}
