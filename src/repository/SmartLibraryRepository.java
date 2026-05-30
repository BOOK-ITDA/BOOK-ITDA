package repository;

import dto.LibraryDto;
import dto.SmartLibraryDto;
import java.sql.Connection;
import java.util.List;

import java.sql.SQLException;
import java.util.List;

public interface SmartLibraryRepository {

    // 신청 가능한 스마트도서관 목록 조회 (book_count < book_capacity 인 곳만)
    List<SmartLibraryDto> findAvailable(Connection conn);

    // 스마트도서관 book_count 1 증가 (신청 완료 시 Service에서 호출)
    void increaseBookCount(Connection conn, int smart_lib_id);

}

