package dto;

import java.time.LocalDate;

public class OverdueRecordDto {
    private int overdue_id;
    private int loan_id;
    private int fine_amount;
    private boolean is_paid;
    private String user_name;
    private String book_name;
    private LocalDate due_date;

    // 모든 속성을 포함하는 생성자
    public OverdueRecordDto(int overdue_id, int loan_id, int fine_amount, boolean is_paid, String user_name, String book_name, LocalDate due_date) {
        this.overdue_id = overdue_id;
        this.loan_id = loan_id;
        this.fine_amount = fine_amount;
        this.is_paid = is_paid;
        this.user_name = user_name;
        this.book_name = book_name;
        this.due_date = due_date;
    }

    OverdueRecordDto(int loan_id, int fine_amount, boolean is_paid) {
        this.loan_id = loan_id;
        this.fine_amount = fine_amount;
        this.is_paid = is_paid;
    }

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

    public String getUser_name() {return user_name;}

    public void setUser_name(String user_name) {this.user_name = user_name;}

    public String getBook_name() {return book_name;}

    public void setBook_name(String book_name) {this.book_name = book_name;}

    public LocalDate getDue_date() {return due_date;}

    public void setDue_date(LocalDate due_date) {this.due_date = due_date;}
}
