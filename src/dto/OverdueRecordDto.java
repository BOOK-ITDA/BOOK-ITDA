package dto;

public class OverdueRecordDto {

    // OVERDUE_RECORD 테이블 컬럼
    private int overdue_id;
    private int loan_id;
    private int fine_amount;
    private boolean is_paid;

    // JOIN 조회용 추가 필드 (LOAN_RECORD + BOOK 테이블에서 가져옴)
    private String book_name;   // BOOK.name
    private String loan_date;   // LOAN_RECORD.loan_date
    private String due_date;    // LOAN_RECORD.due_date

    // 모든 속성을 포함하는 생성자
    OverdueRecordDto(int overdue_id, int loan_id, int fine_amount, boolean is_paid) {
        this.overdue_id = overdue_id;
        this.loan_id = loan_id;
        this.fine_amount = fine_amount;
        this.is_paid = is_paid;
    }

    // JOIN 조회용 생성자
    public OverdueRecordDto(int overdue_id, int loan_id, int fine_amount, boolean is_paid,
                            String book_name, String loan_date, String due_date) {
        this.overdue_id = overdue_id;
        this.loan_id = loan_id;
        this.fine_amount = fine_amount;
        this.is_paid = is_paid;
        this.book_name = book_name;
        this.loan_date = loan_date;
        this.due_date = due_date;
    }

    // 기존 getter/setter
    public int getOverdue_id() {
        return overdue_id;
    }

    public void setOverdue_id(int overdue_id) {
        this.overdue_id = overdue_id;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(int fine_amount) {
        this.fine_amount = fine_amount;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    // 추가 필드 getter/setter
    public String getBook_name() { return book_name; }
    public void setBook_name(String book_name) { this.book_name = book_name; }
    public String getLoan_date() { return loan_date; }
    public void setLoan_date(String loan_date) { this.loan_date = loan_date; }
    public String getDue_date() { return due_date; }
    public void setDue_date(String due_date) { this.due_date = due_date; }
}
