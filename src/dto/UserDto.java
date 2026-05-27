package dto;
import java.time.LocalDate;

public class UserDto {
    private int user_id;
    private String name;
    private LocalDate birthdate;
    private String phone_number;
    private String address;
    private int loan_count;
    private String password;

    // 모든 속성을 포함하는 생성자
    public UserDto(int user_id, String name, LocalDate birthdate, String phone_number, String address, int loan_count, String password) {
        this.user_id = user_id;
        this.name = name;
        this.birthdate = birthdate;
        this.phone_number = phone_number;
        this.address = address;
        this.loan_count = loan_count;
        this.password = password;
    }

    //사서가 회원 목록 조회할 때 비번까지 조회하게 할수는 없어서 비밀번호 없는 생성자 추가
    public UserDto(int user_id, String name, LocalDate birthdate, String phone_number, String address, int loan_count) {
        this.user_id = user_id;
        this.name = name;
        this.birthdate = birthdate;
        this.phone_number = phone_number;
        this.address = address;
        this.loan_count = loan_count;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLoan_count() {
        return loan_count;
    }

    public void setLoan_count(int loan_count) {
        this.loan_count = loan_count;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
