package repository;
import dto.LibraryDto;

import java.sql.SQLException;
import java.util.List;

public interface LibraryRepository {
    List<LibraryDto> getLibList() throws SQLException;
}
