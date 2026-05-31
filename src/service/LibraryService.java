package service;

import dto.LibraryDto;
import repository.LibraryRepository;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public List<LibraryDto> getLibList() throws SQLException {
        return libraryRepository.getLibList();
    }
}