package dto;
import java.time.LocalDate;

public class ReservationRecordDto {
    private int reserve_id;
    private int user_id;
    private int book_id;
    private int library_id;
    private LocalDate reserve_date;
    private String status;
    private String book_name;

    // 모든 속성을 포함한 생성자
    public ReservationRecordDto(int reserve_id, int user_id, int book_id, String book_name, int library_id, LocalDate reserve_date, String status) {
        this.reserve_id = reserve_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.book_name = book_name;
        this.library_id = library_id;
        this.reserve_date = reserve_date;
        this.status = status;
    }

    public ReservationRecordDto(int reserve_id, int user_id, int book_id, int library_id, LocalDate reserve_date, String status) {
        this.reserve_id = reserve_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.reserve_date = reserve_date;
        this.status = status;
    }

    // JOIN 조회용 필드 추가 (DB 컬럼 아님, SELECT 결과 담는 용도)
    private String book_name;
    private String library_name;

    // JOIN 조회용 생성자 추가 (findByUserId에서 사용)
    public ReservationRecordDto(int reserve_id, String book_name, String library_name,
                                LocalDate reserve_date, String status) {
        this.reserve_id = reserve_id;
        this.book_name = book_name;
        this.library_name = library_name;
        this.reserve_date = reserve_date;
        this.status = status;
    }

    public int getReserve_id() {
        return reserve_id;
    }

    public void setReserve_id(int reserve_id) {
        this.reserve_id = reserve_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }

    public LocalDate getReserve_date() {
        return reserve_date;
    }

    public void setReserve_date(LocalDate reserve_date) {
        this.reserve_date = reserve_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 모든 속성을 포함한 생성자
    ReservationRecordDto(int reserve_id, int user_id, int book_id, int library_id, LocalDate reserve_date, String status) {
        this.reserve_id = reserve_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.reserve_date = reserve_date;
        this.status = status;
    }

    // 추가된 getter/setter
    public String getBook_name() { return book_name; }
    public void setBook_name(String book_name) { this.book_name = book_name; }
    public String getLibrary_name() { return library_name; }
    public void setLibrary_name(String library_name) { this.library_name = library_name; }

}
