package ui;

import dto.UserDto;
import service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import service.UserService;

public class SignupUi {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    public SignupUi(UserService userService) {
        this.userService = userService;
    }
    public void showSignupScreen(){
        while(true) {
            System.out.println("              회원가입              ");
            System.out.println("===================================");
            System.out.print("성명 입력: ");
            String username = scanner.nextLine().trim();
            System.out.print("비밀번호 입력: ");
            String password = scanner.nextLine().trim();
            System.out.print("비밀번호 확인: ");
            String checkPassword = scanner.nextLine().trim();
            // 비밀번호 체크
            if (!checkPassword.equals(password)) {
                System.out.println("비밀번호가 일치하지 않습니다.");
                continue;
            }
            System.out.print("생년월일 (YYYY-MM-DD) 입력: ");
            String birthdateInput = scanner.nextLine().trim();

            LocalDate birthdate = null;
            // 생년월일 입력 형식 검증
            try {
                birthdate = LocalDate.parse(birthdateInput);
            } catch (DateTimeParseException e) {
                System.out.println("생년월일 형식이 올바르지 않습니다.");
                continue;
            }
            System.out.print("전화번호 (010-xxxx-xxxx) 입력: ");
            String phoneNumber = scanner.nextLine().trim();
            String phoneRegex = "^010-\\d{3,4}-\\d{4}$";
            if (!phoneNumber.matches(phoneRegex)) {
                System.out.println("올바른 휴대폰 번호 형식이 아닙니다.");
                continue;
            }
            System.out.print("주소 입력: ");
            String address = scanner.nextLine().trim();

            if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                System.out.println("모든 항목은 필수 입력 대상입니다.");
                continue;
            }
            UserDto user = new UserDto(username, birthdate, phoneNumber, address, 0, password);

            try {
                int generatedId = userService.register(user);
                System.out.println("-----------------------------------------------------------");
                System.out.println("회원가입이 완료되었습니다. 로그인을 위해 ID와 비밀번호를 기억해주세요.");
                System.out.println("발급된 회원ID : "+generatedId);

                break;
            } catch (IllegalStateException e) {
                System.out.println("가입 실패: "+e.getMessage());
            } catch (Exception e) {
                System.out.println("시스템 오류가 발생했습니다.: " +e.getMessage());
                break;
            }
            System.out.println("시작 화면으로 돌아갑니다.");
            return;
        }
    }
}
