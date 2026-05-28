package repository;
import dto.LibraryDto;
import dto.SmartLibraryDto;

import java.sql.SQLException;
import java.util.List;

public interface SmartLibraryRepository {
    List<SmartLibraryDto> getSmartLibList() throws SQLException;
}
