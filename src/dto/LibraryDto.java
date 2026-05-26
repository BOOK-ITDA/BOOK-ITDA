package dto;

public class LibraryDto {
    private int library_id;
    private String name;
    private String contact;
    private String address;

    // 모든 속성을 포함하는 생성자
    public LibraryDto(int library_id, String name, String contact, String address) {
        this.library_id = library_id;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
