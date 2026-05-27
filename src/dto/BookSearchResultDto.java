package dto;

public class BookSearchResultDto {
    private int book_id;
    private String name;
    private String author;
    private String publisher;
    private String genre;
    private String library_name;   // JOIN한 LIBRARY.name
    private String status;         // COLLECTION.status (AVAILABLE / BORROWED / RESERVED)
                                   //  status는 String 타입, 필요시 service 작업할 때 enum으로 변환 로직 추가 가능

    public BookSearchResultDto(int book_id, String name, String author,
                               String publisher, String genre,
                               String library_name, String status) {
        this.book_id = book_id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.library_name = library_name;
        this.status = status;
    }

    // Getters
    public int getBook_id()         { return book_id; }
    public String getName()         { return name; }
    public String getAuthor()       { return author; }
    public String getPublisher()    { return publisher; }
    public String getGenre()        { return genre; }
    public String getLibrary_name() { return library_name; }
    public String getStatus()       { return status; }

    // Setters
    public void setBook_id(int book_id)             { this.book_id = book_id; }
    public void setName(String name)                 { this.name = name; }
    public void setAuthor(String author)             { this.author = author; }
    public void setPublisher(String publisher)       { this.publisher = publisher; }
    public void setGenre(String genre)               { this.genre = genre; }
    public void setLibrary_name(String library_name) { this.library_name = library_name; }
    public void setStatus(String status)             { this.status = status; }
}