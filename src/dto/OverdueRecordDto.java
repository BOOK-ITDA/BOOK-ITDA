package dto;

public class OverdueRecordDto {
    private int overdue_id;
    private int loan_id;
    private int fine_amount;
    private boolean is_paid;

    // 모든 속성을 포함하는 생성자
    OverdueRecordDto(int overdue_id, int loan_id, int fine_amount, boolean is_paid) {
        this.overdue_id = overdue_id;
        this.loan_id = loan_id;
        this.fine_amount = fine_amount;
        this.is_paid = is_paid;
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
}
