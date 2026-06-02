package dto;
import java.time.LocalDate;

public class LoanRecordDto {
    private int loan_id;
    private int user_id;
    private int book_id;
    private int library_id;
    private LocalDate loan_date;
    private LocalDate due_date;
    private LocalDate return_date;
    private int extension_count;
    private String book_name;
    private String library_name;

    // 전 속성 포함하는 생성자
    public LoanRecordDto(int loan_id, int user_id, int book_id, int library_id, LocalDate loan_date, LocalDate due_date, LocalDate return_date, int extension_count) {
        this.loan_id = loan_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.loan_date = loan_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.extension_count = extension_count;
    }

    // 대출기록ID 제외 생성자 - INSERT 시 사용
    public LoanRecordDto(int user_id, int book_id, int library_id, LocalDate loan_date, LocalDate due_date, LocalDate return_date, int extension_count) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.loan_date = loan_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.extension_count = extension_count;
    }

    // 조회 전용 생성자 (book_name, library_name 포함 / user_id, book_id 제외)
    public LoanRecordDto(int loan_id, int library_id, String book_name, String library_name,
                         LocalDate loan_date, LocalDate due_date, LocalDate return_date, int extension_count) {
        this.loan_id = loan_id;
        this.library_id = library_id;
        this.book_name = book_name;
        this.library_name = library_name;
        this.loan_date = loan_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.extension_count = extension_count;
    }

    //사서 전용 조회 생성자
    public LoanRecordDto(int loan_id, int user_id, int book_id, int library_id,
                         LocalDate loan_date, LocalDate due_date, LocalDate return_date,
                         int extension_count, String book_name, String library_name) {
        this.loan_id = loan_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.loan_date = loan_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.extension_count = extension_count;
        this.book_name = book_name;
        this.library_name = library_name;
    }

    // 아무 필드 초기화도 안 하는 생성자 - 연장하기 전 대출 중인 도서 목록 조회 당시 사용
    public LoanRecordDto(){

    };
    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
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

    public LocalDate getLoan_date() {
        return loan_date;
    }

    public void setLoan_date(LocalDate loan_date) {
        this.loan_date = loan_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    public int getExtension_count() {
        return extension_count;
    }

    public void setExtension_count(int extension_count) {
        this.extension_count = extension_count;
    }

    public String getBook_name() { return book_name;  }

    public void setBook_name(String book_name) { this.book_name = book_name; }

    public String getLibrary_name() { return library_name; }

    public void setLibrary_name(String library_name) { this.library_name = library_name; }
}
