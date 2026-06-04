package service;

import database.DatabaseConnector;
import repository.OverdueRecordRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverdueService {
    private final OverdueRecordRepository overdueRecordRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public OverdueService(OverdueRecordRepository overdueRecordRepository) {
        this.overdueRecordRepository = overdueRecordRepository;
    }

    public void startDailyOverdueBatch(){
        long initialDelayMinutes = computeMinutesUntilNextMidnight();
        System.out.printf("첫 연체 처리는 약 %d 시간 %d 분 후(자정)에 실행됩니다.", initialDelayMinutes/60, initialDelayMinutes%60);

        scheduler.scheduleAtFixedRate(
                () -> {
                    System.out.println("\n 새 연체 기록 확인 및 연체료 업데이트를 시작합니다.");
                    executeOverdueUpdateProcess();
                },
                initialDelayMinutes,
                24 * 60,
                TimeUnit.MINUTES
        );
    }

    public void executeOverdueUpdateProcess(){
        try(Connection conn = DatabaseConnector.getConnection()){
            try {
                conn.setAutoCommit(false);
                overdueRecordRepository.insertOverdueRecord(conn);
                overdueRecordRepository.updateDailyFineAmount(conn);

                conn.commit();
                System.out.println("연체 정산 : 연체료 및 연체 기록 신규 등록 업데이트가 완료되었습니다.");
            } catch (Exception e) {
                try {
                    conn.rollback();
                    System.out.println("연체 정산 실패 : 작업 중 에러 발생으로 인해 롤백 처리합니다.");
                } catch (SQLException ex) {
                    System.out.println("롤백 처리 중 심각한 오류 발생: " +ex.getMessage());
                }
                throw new RuntimeException("연체 기록 업데이트 실패 : "+e.getMessage(), e);
            }
            }
        catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패",e);
        }
    }

    // 자정까지 남은 시간 계산 (첫 실행)
    private long computeMinutesUntilNextMidnight(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT);
        return Duration.between(now, nextMidnight).toMinutes();
    }

}
