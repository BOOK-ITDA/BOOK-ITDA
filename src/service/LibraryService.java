package service;

import dto.LibraryDto;
import repository.LibraryRepository;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final LibraryRepository libraryDao;

    public LibraryService(LibraryRepository libraryDao) {
        this.libraryDao = libraryDao;
    }

    public List<LibraryDto> getLibList() throws SQLException {
        return libraryDao.getLibList();
    }
}