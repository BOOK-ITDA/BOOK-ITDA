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
}
