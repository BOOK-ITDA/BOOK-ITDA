package dto;
//분관대출신청기록 테이블입니다

public class BranchTransfer {
    private int transfer_req_id;
    private int user_id;
    private int book_id;
    private int holding_lib_id;
    private int pickup_lib_id;
    private String status;

    // 전체 필드 생성자 (UPDATE, SELECT 시 사용)
    public BranchTransfer(int transfer_req_id, int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status) {
        this.transfer_req_id = transfer_req_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
    }

    // ID 없이 생성자 (INSERT 시 사용)
    public BranchTransfer(int user_id, int book_id, int holding_lib_id, int pickup_lib_id, String status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.holding_lib_id = holding_lib_id;
        this.pickup_lib_id = pickup_lib_id;
        this.status = status;
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
}

