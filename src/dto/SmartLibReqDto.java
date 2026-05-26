package dto;

public class SmartLibReqDto {
    // 스마트도서관 대출 신청 처리 상태
    public enum RequestStatus {
        PROCESSING, AVAILABLE
    };

    private int smt_req_id;
    private int user_id;
    private int book_id;
    private int library_id;
    private int smart_lib_id;
    private RequestStatus status;

    // 모든 속성을 포함하는 생성자
    public SmartLibReqDto(int smt_req_id, int user_id, int book_id, int library_id, int smart_lib_id, RequestStatus status) {
        this.smt_req_id = smt_req_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.smart_lib_id = smart_lib_id;
        this.status = status;
    }

    public SmartLibReqDto(int user_id, int book_id, int library_id, int smart_lib_id, RequestStatus status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.library_id = library_id;
        this.smart_lib_id = smart_lib_id;
        this.status = status;
    }

    public int getSmt_req_id() {
        return smt_req_id;
    }

    public void setSmt_req_id(int smt_req_id) {
        this.smt_req_id = smt_req_id;
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

    public int getSmart_lib_id() {
        return smart_lib_id;
    }

    public void setSmart_lib_id(int smart_lib_id) {
        this.smart_lib_id = smart_lib_id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
