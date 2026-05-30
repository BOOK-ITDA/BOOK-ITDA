package service;

import dao.LibraryDao;
import dto.LibraryDto;
import repository.LibraryRepository;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private LibraryRepository libraryDao = new LibraryDao();

    public List<LibraryDto> getLibList() throws SQLException {
        return libraryDao.getLibList();
    }
}
