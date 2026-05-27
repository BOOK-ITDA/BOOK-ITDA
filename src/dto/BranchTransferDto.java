package dto;
//분관대출신청기록 테이블입니다

public class BranchTransferDto {
    private int transfer_req_id;
    private int user_id;
    private int book_id;
    private int holding_lib_id;
    private int pickup_lib_id;
    private String status;
    private String book_name;

    public BranchTransferDto(int transfer_req_id, int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status, String book_name) {
        this.transfer_req_id = transfer_req_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
        this.book_name = book_name;
    }

    // 책 이름 제외 생성자 (UPDATE, SELECT 시 사용)
    public BranchTransferDto(int transfer_req_id, int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status) {
        this.transfer_req_id = transfer_req_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
    }

    // ID 없이 생성자 (INSERT 시 사용, 분관대출 신청시 사용 -> 원래 db에서는 책 이름이 안들어가지만, 자바에서 출력의 편의성을 위해 책이름 필드를 추가, 그래서 생성자가 여러개)
    public BranchTransferDto(int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
    }

    public BranchTransferDto(int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status, String book_name) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
        this.book_name = book_name;
    }

    // Getters and Setters
    public int getTransfer_req_id() { return transfer_req_id; }
    public void setTransfer_req_id(int transfer_req_id) { this.transfer_req_id = transfer_req_id; }
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public int getBook_id() { return book_id; }
    public void setBook_id(int book_id) { this.book_id = book_id; }
    public int getHolding_lib_id() { return holding_lib_id; }
    public void setHolding_lib_id(int holding_lib_id) { this.holding_lib_id = holding_lib_id; }
    public int getPickup_lib_id() { return pickup_lib_id; }
    public void setPickup_lib_id(int pickup_lib_id) { this.pickup_lib_id = pickup_lib_id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBook_name() {return book_name;}
    public void setBook_name(String book_name) {this.book_name = book_name;}
}

