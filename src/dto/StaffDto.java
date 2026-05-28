package dto;

import java.util.Date;

public class StaffDto {
    private int staff_id;
    private String name;
    private Date birthdate;
    private String phone_number;
    private String address;
    private int library_id;

    // 전체 필드 생성자 (UPDATE, SELECT 시 사용)
    public StaffDto(int staff_id, String name, Date birthdate, String phone_number, String address, int library_id) {
        this.staff_id = staff_id;
        this.name = name;
        this.birthdate = birthdate;
        this.phone_number = phone_number;
        this.address = address;
        this.library_id = library_id;
    }

    // ID 없이 생성자 (INSERT 시 사용)
    public StaffDto(String name, Date birthdate, String phone_number, String address, int library_id) {
        this.name = name;
        this.birthdate = birthdate;
        this.phone_number = phone_number;
        this.address = address;
        this.library_id = library_id;
    }

    // Getters and Setters
    public int getStaff_id() { return staff_id; }
    public void setStaff_id(int staff_id) { this.staff_id = staff_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getLibrary_id() {return library_id;}
    public void setLibrary_id(int library_id) {this.library_id = library_id; }
}
