package dto;

public class BookDto {
    private int book_id;
    private String name;
    private String author;
    private String publisher;
    private String genre;

    // 모든 속성을 포함하는 기본 생성자
    public BookDto(int book_id, String name, String author, String publisher, String genre) {
        this.book_id = book_id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
