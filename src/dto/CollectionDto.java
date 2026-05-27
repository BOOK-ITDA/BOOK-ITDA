package dto;

public class CollectionDto {
    private int book_id;
    private int library_id;
    private String status;

    // 모든 속성을 포함하는 생성자
    public CollectionDto(int book_id, int library_id, String status) {
        this.book_id = book_id;
        this.library_id = library_id;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
