package dto;

public class SmartLibraryDto {
    private int smart_lib_id;
    private String name;
    private String address;
    private int book_capacity;
    private int book_count;

    public SmartLibraryDto(int smart_lib_id, String name, String address, int book_capacity, int book_count) {
        this.smart_lib_id = smart_lib_id;
        this.name = name;
        this.address = address;
        this.book_capacity = book_capacity;
        this.book_count = book_count;
    }

    public int getSmart_lib_id() {
        return smart_lib_id;
    }

    public void setSmart_lib_id(int smart_lib_id) {
        this.smart_lib_id = smart_lib_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBook_capacity() {
        return book_capacity;
    }

    public void setBook_capacity(int book_capacity) {
        this.book_capacity = book_capacity;
    }

    public int getBook_count() {
        return book_count;
    }

    public void setBook_count(int book_count) {
        this.book_count = book_count;
    }
}
