package dto;
import java.time.LocalDate;

public class ReservationRecordDto {
    private int reserve_id;
    private int user_id;
    private int book_id;
    private int library_id;
    private LocalDate reserve_date;
    private String status;

    // 모든 속성을 포함한 생성자
    ReservationRecordDto(int reserve_id, int user_id, int book_id, int library_id, LocalDate reserve_date, String status) {
        this.reserve_id = reserve_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.reserve_date = reserve_date;
        this.status = status;
    }

    // 모든 속성을 포함한 생성자
    ReservationRecordDto(int user_id, int book_id, int library_id, LocalDate reserve_date, String status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
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


}
